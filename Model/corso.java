package model.dto;

/**
 * @author Dario Nitti
 */
public class corso {
	private int id_c;
        private int id_a;
        int id_prof;
        int anno;
        int semestre;
        int cfu;
        char tipo;

	// Constructors
	public corso () {}
	public corso(int id_c, int id_a, int id_prof, int anno, int semestre, int cfu, char tipo) {
                this.id_c = id_c;
                this.id_a = id_a;
                this.id_prof = id_prof;
                this.anno = anno;
                this.semestre = semestre;
                this.cfu = cfu;
                this.tipo = tipo;
}   
        
       
	// Getter & Setter 
	public int getID_C () {
		return id_c;
	}
	public void setID_C (int id_c) {
		this.id_c = id_c;
	}
        
        
        public int getID_A () {
		return id_a;
	}
	public void setID_A (int id_a) {
		this.id_a = id_a;
	}
        
        public int getID_Prof () {
		return id_prof;
	}
	public void setID_Prof (int id_prof) {
		this.id_prof = id_prof;
	}
        
        public int getAnno () {
		return anno;
	}

   
         public void setAnno (int anno) {
		this.anno = anno;
	}
    
        public int getSemestre () {
		return semestre;
	}
	public void setSemestre (int semestre) {
		this.semestre = semestre;
	}
        
        public int getCfu () {
		return cfu;
	}
	public void setCfu (int cfu) {
		this.cfu = cfu;
	}

        public int getTipo () {
		return tipo;
	}
	public void setID_C (char tipo) {
		this.tipo = tipo;
	}
}



