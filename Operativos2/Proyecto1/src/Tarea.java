
public class Tarea {
	
	private int id;
	private char estado; //r(listo)  -  x(ejecución)  -  w(espera)
	private boolean uso; //true tiene información  -  false espacio en blanco
	
	public Tarea(int id) {
		this.id = id;
		this.estado = setEstadoRandom();
		uso = true;
	}
	
	public Tarea(int id, char estado) {
		this.id = id;
		this.estado = estado;
		uso = true;
	}
	
	public Tarea() {
		uso = false;
	}
	
	public char setEstadoRandom() {
		double estado = Math.random();
		if(estado <= 0.2)
			return 'r';
		else if(estado <= 0.5)
			return 'w';
		else
			return 'x';
	}
	
	public void setEstado(char estado) {
		this.estado = estado;
	}
	
	public boolean getUso() {
		return uso;
	}
	
	public int getId() {
		return id;
	}
	
	public void Limpia() {
		uso = false;
	}
	
	public char getEstado() {
		return estado;
	}
	
	public void setValores(int id, char estado, boolean uso) {
		this.id = id;
		this.estado = estado;
		this.uso = uso;
	}
	
}
