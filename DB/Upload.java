/*
 * ATTENZIONE: il codice di questa classe dipende dalla corretta definizione delle
 * risorse presente nei file context.xml (Resource) e web.xml (resource-ref)
 * 
 * WARNING: this class needs the definition of an external data source to work correctly.
 * See the Resource element in context.xml and the resource-ref element in web.xml
 * 
 * The following table must be defined in the database:
 * 
 * CREATE TABLE  `webdb`.`files` (
 * `name` varchar(255) NOT NULL,
 * `type` varchar(255) NOT NULL,
 * `size` int(11) NOT NULL,
 * `localfile` varchar(255) NOT NULL,
 * `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 * `ID` int(11) NOT NULL AUTO_INCREMENT,
 * `digest` varchar(255) NOT NULL,
 * PRIMARY KEY (`ID`)
 * )
 */
package it.univaq.f4i.iw.examples;

import it.univaq.f4i.iw.framework.result.HTMLResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

/**
 *
 * @author Giuseppe Della Penna
 */
public class Upload extends HttpServlet {

    private static final String ADD_FILE_QUERY = "INSERT INTO files(name,type,size,localfile,digest,updated) VALUES(?,?,?,?,?,CURRENT_TIMESTAMP)";

    @Resource(name = "jdbc/webdb2")
    private DataSource ds;

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        //assumiamo che l'eccezione sia passata tramite gli attributi della request
        //we assume that the exception has been passed using the request attributes
        Exception exception = (Exception) request.getAttribute("exception");
        String message;
        if (exception != null && exception.getMessage() != null) {
            message = exception.getMessage();
        } else {
            message = "Unknown error";
        }
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("ERROR");
        result.setBody("<p>" + message + "</p>");
        try {
            result.activate(request, response);
        } catch (IOException ex) {
            //if error page cannot be sent, try a standard HTTP error message
            //se non possiamo inviare la pagina di errore, proviamo un messaggio di errore HTTP standard
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
            } catch (IOException ex1) {
                //if ALSO this error status cannot be notified, write to the server log
                //se ANCHE questo stato di errore non può essere notificato, scriviamo sul log del server
                Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void action_upload(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {

        int fileID = 0;
        Part file_to_upload = request.getPart("filetoupload");

        //we want the sha-1 file digest of the uploaded file
        //vogliamo creare il digest sha-1 del file
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        //create a file (with a unique name) and copy the uploaded file to it
        //creiamo un nuovo file (con nome univoco) e copiamoci il file scaricato
        File uploaded_file = File.createTempFile("upload_", "", new File(getServletContext().getInitParameter("uploads.directory")));
        try (InputStream is = file_to_upload.getInputStream();
                OutputStream os = new FileOutputStream(uploaded_file)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) > 0) {
                //durante la copia, aggreghiamo i byte del file nel digest sha-1
                //while copying, we aggregate the file bytes in the sha-1 digest
                md.update(buffer, 0, read);
                os.write(buffer, 0, read);
            }
        }

        //get the file digest as a string
        //covertiamo il digest in una stringa
        byte[] digest = md.digest();
        String sdigest = "";
        for (byte b : digest) {
            sdigest += String.valueOf(b);
        }

        //now put the file information in the database
        //adesso inseriamo tutte le informazioni sul file nel database
        try (Connection c = ds.getConnection();
                //indichiamo al driver la colonna in cui comparirà la chiave auto-generata dall'inserimento
                PreparedStatement s = c.prepareStatement(ADD_FILE_QUERY, new String[]{"ID"})) {

            s.setString(1, file_to_upload.getSubmittedFileName());
            s.setString(2, file_to_upload.getContentType());
            s.setLong(3, file_to_upload.getSize());
            s.setString(4, uploaded_file.getName());
            s.setString(5, sdigest);
            if (s.executeUpdate() == 1) {
                try ( //get the added record ID
                        ResultSet keys = s.getGeneratedKeys()) {
                    keys.first();
                    fileID = keys.getInt(1);
                }
                HTMLResult result = new HTMLResult(getServletContext());
                result.setTitle("Upload complete");
                result.appendToBody("<h1>Successful upload</h1>");
                result.appendToBody("<p>The file " + file_to_upload.getSubmittedFileName()+ " (" + file_to_upload.getContentType() + ", " + file_to_upload.getSize() + "bytes, digest " + sdigest + ") has been correctly uploaded. The file ID is " + fileID + "</p>");
                result.activate(request, response);
            } else {
                request.setAttribute("exception", new Exception("Upload error"));
                action_error(request, response);
            }
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            if (request.getPart("filetoupload") != null) {
                action_upload(request, response);
            } else {
                request.setAttribute("exception", new Exception("Nothing to upload!"));
                action_error(request, response);
            }
        } catch (NamingException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (SQLException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (FileUploadException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (NoSuchAlgorithmException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (Exception ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
