
public class Hueco {
	private int tamanio;
	private int inicio;
	private int fin;
	
	public Hueco(int inicio, int fin) {
		this.inicio = inicio;
		this.fin = fin;
		tamanio = (fin - inicio) + 1;
	}
	
	public int getInicio() {
		return inicio;
	}
	
	public int getFin() {
		return fin;
	}
	
	public int getTamanio() {
		return tamanio;
	}
	
}
