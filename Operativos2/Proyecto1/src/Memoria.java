import java.util.ArrayList;

public class Memoria {

	private Tarea []memoria;
	private int libre; //Espacio libre total en memoria
	private ArrayList<Hueco> huecos; //Huecos identificados para poder guardar
	private String titulo; //Nombre de la memoria
	
	public Memoria(int tamanio, String titulo) {
		this.titulo = titulo;
		IniciaMemoria(tamanio);
	}
	
	public void IniciaMemoria(int tamanio) {
		memoria = new Tarea[tamanio];
		for(int i = 0; i < tamanio; i++) {
			memoria[i] = new Tarea();
		}
		huecos = new ArrayList<Hueco>();
		calculaEspacioLibre();
		CalculaHuecos();
	}
	
	public int getEspacioLibre() {
		return libre;
	}
	
	public int getTamanio() {
		return memoria.length;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	//Suma todos los huecos libres para saber cuantos pueden ser usados
	public void calculaEspacioLibre() {
		libre = 0;
		for(int i = 0; i < memoria.length; i++)
			if(!memoria[i].getUso())
				libre++;
		CalculaHuecos();
	}
	
	//Busca que el id dado no esté ya en memoria
	public boolean VerificaIdExistente(int id) {
		for(int i = 0; i < memoria.length; i++)
			if(memoria[i].getUso() && memoria[i].getId() == id)
				return true;
		return false;
	}
	 	
	private void CalculaHuecos() {
		int inicio = -1, fin = -1, huecosActuales = huecos.size();
		
		for(int i = 0; i < huecosActuales; i++) {
			huecos.remove(0);
		}
		
		for(int i = 0; i < memoria.length; i++)
			if(!memoria[i].getUso()) {
				if(inicio == -1) {
					inicio = i;
				}else {
					fin = i;
				}
			}else if(inicio != -1){
				if(fin == -1) {
					fin = i - 1;
				}
				huecos.add(new Hueco(inicio, fin));
				inicio = fin = -1;
			}
		
		if(inicio != -1) {
			huecos.add(new Hueco(inicio, fin));
		}
	}
	
	public ArrayList<Hueco> getHuecos(){
		return huecos;
	}
	
	public int totalHuecos() {
		return huecos.size();
	}
	
	public boolean GuardaDatos(Tarea tarea, int tamanio) {
		Hueco h;
		h = buscaHueco(tamanio);
		if(h != null) {
			for(int i = h.getInicio(); tamanio != 0; i++) {
				memoria[i] = tarea;
				tamanio--;
			}
			calculaEspacioLibre();
			CalculaHuecos();
			return true;
		}
		return false;
	}

	public boolean IntercambioDato(int id, char estado, int tamanio) {
		Hueco h;
		h = buscaHueco(tamanio);
		if(h != null) {
			for(int i = h.getInicio(); tamanio != 0; i++) {
				memoria[i] = new Tarea(id,estado);
				tamanio--;
			}
			calculaEspacioLibre();
			CalculaHuecos();
			return true;
		}
		return false;
	}
	
	private Hueco buscaHueco(int tamanio) {
		for(Hueco h : huecos) {
			if(h.getTamanio() >= tamanio)
				return h;
		}
		return null;
	}

	public void MostrarMemoria() {
		System.out.println("\n\n" + titulo + " total " + getTamanio() +" Bloques:\tBloques usados:" + (getTamanio() - libre) + "\tBloques libres:" + libre);
		MuestraEspacios();
		
		//Imprime encabezado (posición en meoria)
		for(int i = 0; i < memoria.length; i++) {
			if(i < 10) {
				System.out.print("[ " + i + "]");
			}
			else {
				System.out.print("[" + i + "]");
			}
		}
		System.out.print("\n");
		
		//Imprime contenido de Memoria
		for(Tarea r : memoria) {
			if(r.getUso()) {
				if(r.getId() < 10) {
					System.out.print("[ " + r.getId() + "]");
				}
				else {
					System.out.print("[" + r.getId() + "]");
				}
			}else {
				System.out.print("[ 0]");
			}
		}
		
		MuestraDatosTareas();
	}
	
	//Imprime los huecos existentes, Inicio - Final : Tamaño
	private void MuestraEspacios() {
		System.out.println(titulo);
		for(Hueco h : huecos)
			System.out.println("Inicio " + h.getInicio() + " - Final " + h.getFin() + " : Total " + h.getTamanio());
	}
	
	private void MuestraDatosTareas() {
		int tarea = -1;
		System.out.print("\n");
		for(Tarea t : memoria) {
			if(t.getUso()) {
				if(t.getId() != tarea) {
					tarea = t.getId();
					System.out.print("Tarea " + tarea);
					if(t.getEstado() == 'r') {
						System.out.println(" lista");
					}else if(t.getEstado() == 'w') {
						System.out.println(" en espera");
					}else {
						System.out.println(" en ejecución");
					}
				}
			}
		}
	}
	
	public boolean EliminarTarea(int id) {
		boolean borra = false;
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getId() == id) {
				memoria[i].Limpia();
				borra = true;
			}
		}
		
		if(borra) {
			calculaEspacioLibre();
			CalculaHuecos();
		}
		
		return borra;
	}
	
	public void MuestraTareasExistentes() {
		System.out.println("\nTareas en Memoria " + titulo);
		int num = -1;
		for(Tarea t : memoria) {
			if(t.getUso()) {
				if(num != t.getId()) {
					num = t.getId();
					System.out.print(num + "   ");
				}
			}
		}
		System.out.print("\n");
	}
	
	public void MuestraHuecos() {
		System.out.print("Huecos en Memoria " + titulo + ": " + memoria.length + "\n");
		for(Hueco h : huecos) {
			if(h.getInicio() < 10) {
				System.out.print("Inicio [ " + h.getInicio() + "]     ");
			}else {
				System.out.print("Inicio [" + h.getInicio() + "]     ");
			}
			
			if(h.getFin() < 10) {
				System.out.print("Final [ " + h.getFin() + "]     " );
			}else {
				System.out.print("Final [" + h.getFin() + "]     " );
			}
			
			System.out.println("Tamaño: " + h.getTamanio());
		}
	} 
	
	public int CalculaMemoriaLiberable() {
		int tamanio = 0;
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getUso() && memoria[i].getEstado() != 'x') {
				tamanio++;
			}
		}
		return tamanio;
	}
	
	public boolean IntercambioTareas(int tam, Memoria memoria) {
		ArrayList<Hueco> procesosParaIntercambiar = ProcesosListosyEspera();
		
		while(libre < tam) {
			if(procesosParaIntercambiar.size() >= 1) {
				if(memoria.getEspacioLibre() >= procesosParaIntercambiar.get(0).getTamanio()) {
					memoria.IntercambioDato(this.memoria[procesosParaIntercambiar.get(0).getInicio()].getId(), //Id del proceso a intercambiar 
											this.memoria[procesosParaIntercambiar.get(0).getInicio()].getEstado(), //Estado del proceso a intercambiar 
											procesosParaIntercambiar.get(0).getTamanio()); //Tamaño del proceso a intercambiar
					EliminarTarea(this.memoria[procesosParaIntercambiar.get(0).getInicio()].getId());
					procesosParaIntercambiar.remove(0);
				}
			}else
				break;
		}
		
		if(libre >= tam)
			return true;
		return false;
	}
	
	public void Compactacion() {
		Tarea []mem2 = new Tarea[memoria.length];
		int rec = 0;
		
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getUso()) { //hueco
				mem2[rec] = memoria[i];
				rec++;
			}
		}
		
		for(int i = rec; i < mem2.length; i++) {
			mem2[i] = new Tarea(); 
		}

		memoria = mem2;
		
		calculaEspacioLibre();
		CalculaHuecos();
	}
	
	private ArrayList<Hueco> ProcesosListosyEspera(){
		ArrayList<Hueco> lista = new ArrayList<Hueco>();
		int proc = -1;
		int inicio = -1;
		int fin = -1;
		
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getUso() && memoria[i].getEstado() != 'x') { //si está en uso y no en ejecución
				if(proc == -1) {
					inicio = fin = i;
					proc = memoria[i].getId();
				}else if(proc == memoria[i].getId()){
					fin = i;
				}else {
					lista.add(new Hueco(inicio,fin));
					inicio = fin = i;
					proc = memoria[i].getId();
				}
				
				if(i == (memoria.length-1)) {
					lista.add(new Hueco(inicio,fin));
				}
			}
		}
		return lista;
	}

	public void CambiaEstadosAleatorio() {
		int tarea = -1;
		char estado = 'l';
		for( int i = 0; i < memoria.length; i++) {
			if(memoria[i].getId() != tarea) {
				tarea = memoria[i].getId();
				estado = memoria[i].setEstadoRandom();
			}
			memoria[i].setEstado(estado);
		}
	}

	public int VerificaTareas() {
		int tam = 0;
		return tam;
	}
	
	public int BuscaEjecucion() {
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getUso() && memoria[i].getEstado() == 'x') {
				return i;
			}
		}
		return -1;
	}

	public int BuscaTarea(int pos) {
		for(int i = pos; i < memoria.length; i++) {
			if(memoria[i].getUso()) {
				return i;
			}
		}
		return -1;
	}
	
	public int getIDTarea(int pos) {
		return memoria[pos].getId();
	}
	
	public char getEstadoTarea(int pos) {
		return memoria[pos].getEstado();
	}
	
	public char getEstadoTareaID(int id) {
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getId() == id)
				return memoria[i].getEstado();
		}
		return ' ';
	}
	
	public int getPosicionTarea(int id) {
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getId() == id && memoria[i].getUso()){
				return i;
			}
		}
		return -1;
	}
	
	public int getFinalPosTarea(int pos) {
		for(int i = pos; i < memoria.length; i++) {
			if(memoria[i].getId() != memoria[pos].getId() || !memoria[i].getUso()) {
				return i-1;
			}
		}
		return memoria.length-1;
	}
	
	public void setEstadoTarea(int id, char estado) {
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getId() == id) {
				memoria[i].setEstado(estado);
			}
		}
	}
	
	//Imprime la RAM como mapa de bits 
	public void MapaBit() { //8 pos por fila
		System.out.println("* * * M A P A   D E   B I T S " + titulo + " * * *\n");
		for(int i = 0; i < memoria.length; i++) {
			if( i != 0 && ((i % 8) == 0))
				System.out.print("\n");
			if(memoria[i].getUso()) {
				System.out.print("1 ");
			}else {
				System.out.print("0 ");
			}
		}
	}
	
	public void ListaDatos() {
		int proc = -1;
		System.out.print("[0]-");
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getUso()) { //Memoria en uso
				if(proc == -1) { //Vengo de un hueco
					proc = memoria[i].getId();
					if(i == 0) { //Apenas inicio el for
						System.out.print(proc);
					}else {
						System.out.print("-[" + (i-1) + "] -> [" + i + "]-" + proc);
					}
				}else {
					if(memoria[i].getId() != proc) { //Ver que el valor encontrado sea diferente a uno encontrado anteriormente
						proc = memoria[i].getId();
						System.out.print("-[" + (i-1) + "] -> [" + i + "]-" + proc);
					}
				}
			}else {
				if(proc != -1) {
					proc = -1;
					System.out.print("-[" + (i-1) + "] -> [" + i + "]-H");
				}
			}
		}
		if(proc == -1)
			System.out.print("H");
		System.out.print("-["+ (memoria.length-1) +"]");
	}
	
	public int getPosInicialTarea(int id) {
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	public int getTamanioTarea(int id) {
		int tam = 0;
		for(int i = 0; i < memoria.length; i++) {
			if(memoria[i].getId() == id) {
				tam++;
			}
		}
		return tam;
	}
	
}

