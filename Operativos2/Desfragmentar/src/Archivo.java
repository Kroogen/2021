
public class Archivo {
	
	private String nombre; //Nombre del archivo
	private int tamanio;   //Total de espacio disponible
	private boolean uso;   //Variable de conotrol de uso
	private int usado;     //Cantidad de espacio utilizado
		
	public Archivo(int tamanio) {
		this.nombre = "";
		this.tamanio = tamanio;
		this.uso = false;
		this.usado = 0;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public int getTamanio() {
		return this.tamanio;
	}
	
	public void EliminaEspacio() {
		tamanio = 0;
	}

	public boolean getUso() {
		return this.uso;
	}
	
	public int setValores(String nombre, int usado) {
		this.nombre = nombre;
		this.uso = true;
		if(usado > this.tamanio) {
			this.usado = this.tamanio;
			return this.tamanio;
		}else { 
			this.usado = usado;
			return usado;
		}
	}
	
	public int getUsado() {
		return this.usado;
	}
	
	public void Limpia() {
		this.nombre = "";
		this.uso = false;
		this.usado = 0;
	}
	
}
