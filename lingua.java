package model.dto;

/**
 * @author Dario Nitti
 */
public class lingua {

    private int id_lingua;
    private char nome;

    //Constructor
    public lingua() {}

    public lingua(int id_lingua, char nome) {
        this.id_lingua = id_lingua;
        this.nome = nome;
    }
    
    //Getter and Setter
    
       public int getID_Lingua () {
		return id_lingua;
	}
	public void setID_Lingua (int id_lingua) {
		this.id_lingua = id_lingua;
	}
        
           public int getNome () {
		return nome;
	}
	public void setNome (char nome) {
		this.nome = nome;
	}

}
