package model.dto;

/**
 * @author Dario Nitti
 */
public class corso_di_laurea {

    private int id_a;
    private char nome;
    private char codice;

//Constructor
    public corso_di_laurea() {
    }

    public corso_di_laurea(int id_a, char nome, char codice) {
        this.id_a = id_a;
        this.nome = nome;
        this.codice = codice;
    }

    //Getter and Setter
    public int getID_A() {
        return id_a;
    }

    public void setID_A(int id_a) {
        this.id_a = id_a;

    }

    public int getNome() {
        return nome;
    }

    public void setNome(char nome) {
        this.nome = nome;

    }

    public int getCodice() {
        return codice;
    }

    public void setCodice(char codice) {
        this.codice = codice;

    }
}
