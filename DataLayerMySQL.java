package model.Dal.mysql;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import model.Dal.DataLayer;


/**
 * @author Dario Nitti
 */
public abstract class DataLayerMySQL implements DataLayer {

	// MySQL database connection details
	// ?useSSL=false is here just for testing purpose, it can be removed
	private static final String URL = "jdbc:mysql://localhost:3306/corso?zeroDateTimeBehavior=convertToNull";
	private static final String USR = "root";
	private static final String PWD = "";

	/**
	 * Get a Connection to the MySQL database
	 *
	 * @return Connection object
	 * @throws SQLException in case of error
	 */
	public static Connection getConnection () throws SQLException {
		try {
			return DriverManager.getConnection(URL, USR, PWD);
		} catch (SQLException e) {
			throw new SQLException("Error connecting to MySQL database.", e);
		}
	}
}