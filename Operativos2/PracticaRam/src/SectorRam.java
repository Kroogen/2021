
public class SectorRam {
	
	private int proceso;
	private boolean estado;
	private int inicio;
	private int fin;
	private boolean orilla;
	private int numFila;
	
	public SectorRam() {
		Limpia();
	}
	
	public int getProceso() {
		return this.proceso;
	}
	
	
	public boolean getEstado() {
		return this.estado;
	}
	
	public void setValores(int proceso) {
		this.proceso = proceso;
		this.estado = true;
	}
	
	public void Limpia() {
		this.proceso = -1;
		this.estado = false;
		this.inicio = -1;
		this.fin = -1;
		this.orilla = false;
		numFila = -1;
	}
	
	public int getInicio() {
		return this.inicio;
	}
	
	public void setInicio(int inicio, int numFila) {
		this.inicio = inicio;
		this.numFila = numFila;
		orilla = true;
	}
	
	public int getFinal() {
		return this.fin;
	}
	
	public void setFin(int fin) {
		this.fin=fin;
		orilla = true;
	}
	
	public boolean isCorner() {
		return this.orilla;
	}
	
	public int getNumFila() {
		return this.numFila;
	}
	
	public void setValores(int proceso,boolean estado, int inicio, int fin, boolean orilla,int numFila) {
		this.proceso = proceso;
		this.estado = estado;
		this.inicio = inicio;
		this.fin = fin;
		this.orilla = orilla;
		this.numFila = numFila;
	}
	
}