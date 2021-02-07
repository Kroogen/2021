
public class Hueco {
	
	private int inicio;
	private int fin;
	private int tamanio;
	
	public Hueco(int inicio,int fin) {
		this.inicio = inicio;
		this.fin = fin;
		tamanio = (fin - inicio) + 1;
	}
	
	public int getInicio() {
		return this.inicio;
	}
	
	public int getFinal() {
		return this.fin;
	}
	
	public int getTamanio() {
		return this.tamanio;
	}
}
