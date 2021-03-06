import java.util.ArrayList;
import java.util.Scanner;

public class Principal {
	private static SectorRam []ram; //memoria RAM
	private static SectorRam []swap; //memoria SWAP
	private static int TAMANIO; //tama�o de las memorias
	private static Scanner scanner = new Scanner(System.in);
	private static ArrayList<Hueco> huecosRAM; //huecos en RAM
	private static ArrayList<Hueco> huecosSWAP; //huecos en SWAP
	private static int numFila = 1;
	private static int paginacion; //tama�o paginaci�n
	private static boolean RAM_SWAP; //true guarda en RAM - false guarda en SWAP
	
	public static void main(String []args) {
		boolean FIN = false;	
		TamanioRam();
		IniciarValores();
		int espaciosUsados = ActualizarEspacios(ram,huecosRAM);
		int espaciosUsadoswap = ActualizarEspacios(swap,huecosSWAP);
		paginacion = TamanioPaginacion();
		while(!FIN) {
			switch(Menu()) {
				case 1:
					if(IngresarDato(espaciosUsados,espaciosUsadoswap)) {
						espaciosUsados = ActualizarEspacios(ram,huecosRAM);
						espaciosUsadoswap = ActualizarEspacios(swap,huecosSWAP);
					}
					break;
				case 2:
					EliminarValores();
					espaciosUsados = ActualizarEspacios(ram,huecosRAM);
					espaciosUsadoswap = ActualizarEspacios(swap,huecosSWAP);
					break;
				case 3:
					System.out.print("\n----------------------------------------------------------------------------------------------------------------------");
					MostrarRam("RAM",espaciosUsados,ram,huecosRAM);
					System.out.print("\n\n----------------------------------------------------------------------------------------------------------------------");
					MostrarRam("SWAP",espaciosUsadoswap,swap,huecosSWAP);
					System.out.println("\n\n----------------------------------------------------------------------------------------------------------------------\n");
					break;
				case 4:
					Compactacion(ram,huecosRAM);
					Compactacion(swap,huecosSWAP);
					break;
				case 5:
					MapaBit(" R A M",ram);
					System.out.print("\n\n");
					MapaBit(" S W A P",swap);
					break;
				case 6:
					MuestraEspacios("RAM",huecosRAM);
					System.out.print("\n\n");
					MuestraEspacios("SWAP",huecosSWAP);
					break;
				case 7:
					MostrarRamFila("RAM",ram);
					System.out.print("\n");
					MostrarRamFila("SWAP",swap);
					break;	
				case 8:
					MostrarRamLista("RAM",ram);
					System.out.print("\n");
					MostrarRamLista("SWAP",swap);
					break;
				case 9:
					BorrarCola();
					break;
				case 10:
					MuestraPaginas("RAM" , ram);
					System.out.print("\n");
					MuestraPaginas("SWAP" , swap);
					break;	
				case 0:
					FIN = true;
					return;
			}
			Pausa();
		}
	}
	
	//Imprime men� y verifica que la entrada sea v�lida
		private static int Menu() {
			int dec = -1;
			
			do {
			System.out.println("* * * * M E N U * * * *");
			System.out.println(" 1: Ingresar dato a RAM");
			System.out.println(" 2: Eliminar dato de RAM");
			System.out.println(" 3: Mostrar RAM");
			System.out.println(" 4: Compactaci�n");
			System.out.println(" 5: Mapa de Bits");
			System.out.println(" 6: Lista Huecos Disponibles");
			System.out.println(" 7: Mostrar en Lista");
			System.out.println(" 8: Mostrar en Vector");
			System.out.println(" 9: Eliminar por Cola");
			System.out.println(" 10: Mostrar P�ginas");
			System.out.println(" 0: Salir\n");
			System.out.println("Seleccione una opci�n: ");
			try {
				dec = Integer.parseInt(scanner.next());
			}catch(Exception e) {
				dec = -1;
			}
			}while(dec < 0 || dec > 10);
			return dec;
		}
	
	//Lee el tama�o que se har� de las p�ginas
	private static int TamanioPaginacion() {
		int num;
		do {
			System.out.print("Ingrese el tama�o de la paginaci�n:");
			try {
				num = Integer.parseInt(scanner.next());
			}catch(Exception e) {
				num = -1;
			}
		}while(num < 2 || num > TAMANIO);
		return num;
	}
	
	//Le� el tama�o que querra el usuario que tenga la RAM y SWAMP (cuyo tama�o m�nimo ser� 10 y m�ximo 100)
	private static void TamanioRam() {
		do {
			System.out.print("Ingrese el tama�o que tendr� la memoria RAM y SWAP:");
			try {
				TAMANIO = Integer.parseInt(scanner.next());
			}catch(Exception e) {
				TAMANIO = -1;
			}
		}while(TAMANIO < 10 || TAMANIO > 100);
	}
	
	//Elimina los valores asociados al proceso especificado
	private static void EliminarValores() {
		int proceso;
		System.out.println("Ingrese el n�mero del Proceso a eliminar: ");
		if((proceso = VerificaDatoIngresado()) > 0) {
			for(int i = 0; i < TAMANIO; i++) {
				if(ram[i].getProceso() == proceso) {
					ram[i].Limpia();
				}else if(swap[i].getProceso() == proceso) {
					swap[i].Limpia();
				}
			}
		}
	}
	
	//Inicializa la RAM vacia y la lista para control de huecos
	private static void IniciarValores() {
		ram = new SectorRam[TAMANIO];
		swap = new SectorRam[TAMANIO];
		huecosRAM = new ArrayList<Hueco>();
		huecosSWAP = new ArrayList<Hueco>();
		RAM_SWAP = true;
		for(int i = 0; i < ram.length; i++) {
			ram[i] = new SectorRam();
			swap[i] = new SectorRam();
		}
	}
	
	//Imprime el contenido de la RAM as� como detalles de su espacio
	private static void MostrarRam(String titulo,int espaciosUsados, SectorRam[] memoria, ArrayList<Hueco> hueco) {
		System.out.println("\n\n" + titulo + " total " + TAMANIO +" Bloques:\tBloques usados:" + espaciosUsados + "\tBloques libres:" + (TAMANIO - espaciosUsados));
		MuestraEspacios(titulo,hueco);
		
		for(int i = 0; i < memoria.length; i++) {
			if(i < 10) {
				System.out.print("[ " + i + "]");
			}
			else {
				System.out.print("[" + i + "]");
			}
		}
		System.out.print("\n");
		
		for(SectorRam r : memoria) {
			if(r.getEstado()) {
				if(r.getProceso() < 10) {
					System.out.print("[ " + r.getProceso() + "]");
				}
				else {
					System.out.print("[" + r.getProceso() + "]");
				}
			}else {
				System.out.print("[ 0]");
			}
		}
	}
	
	//Controla el fjujo en el que se hace la validaci�n de los valores
	private static boolean IngresarDato(int usadoRAM, int usadoSwap) {
		int proceso,tamanio;
		if((proceso = VerificaProcesoExistente()) >= 1) {
			if((tamanio = VerificaTamanioProceso(usadoRAM, usadoSwap)) >= 1) { /////------------------------------------------------------------------------------------------------
				if(RAM_SWAP) //guarda RAM
					SeleccionaLugarInsertar(proceso, tamanio, ram, huecosRAM);
				else //guarda SWAP
					SeleccionaLugarInsertar(proceso, tamanio, swap, huecosSWAP);
			}else{
				System.out.println("No se puede guardar el proceso " + proceso + " el tama�o es mayor al espacio disponible");
				return false;
			}
		}else { //error n�mero de proceso
			System.out.println("No se puede guardar el proceso " + proceso);
			return false;
		}
		return true;
	}

	//Men� para elegir si insertar al inicio o al final RAM
	private static void SeleccionaLugarInsertar(int proceso, int tamanio, SectorRam []memoria, ArrayList<Hueco> huecosMemoria) {
		int dec;
		System.out.println("Selecciona modo de guardado\n");
		System.out.println("1: Inicio");
		System.out.println("2: Final");
		if((dec = VerificaDatoIngresado()) > 0) {
			switch(dec) {
				case 1:
					InsertarDatoValido(proceso,tamanio, memoria, huecosMemoria);//Inserta en el primer hueco encontrado
					break;
				case 2:
					InsertarDatoFinal(proceso,tamanio, memoria, huecosMemoria);
					break;
					default:
						System.out.println("\n\nOpci�n inv�lida");
			}
		}
	} 
	
	//Buscar el espacio donde guardar al final
	private static void InsertarDatoFinal(int proceso, int tamanio, SectorRam []memoria, ArrayList<Hueco> huecosMemoria) {
		if(InsertarEnHuecoFinal(proceso,tamanio, memoria, huecosMemoria)) {
			return;
		}
		//Hacer espacio para el valor a insertar
		Compactacion(memoria, huecosMemoria);
		InsertarEnHuecoFinal(proceso,tamanio, memoria, huecosMemoria);
	}
	
	//Inserta los datos al final del espacio
	private static boolean InsertarEnHuecoFinal(int proceso, int tamanio, SectorRam []memoria, ArrayList<Hueco> huecosMemoria) {
		int limite;
		
		for(int i = huecosMemoria.size()-1; i >= 0; i--)
			if(huecosMemoria.get(i).getTamanio() >= tamanio) {
				limite = huecosMemoria.get(i).getFinal() - tamanio;
				for(int j = huecosMemoria.get(i).getFinal(); j > limite; j--) {
					if(j == huecosMemoria.get(i).getFinal()) {
						memoria[j].setFin(j);
					}else if(j == (limite+1)) {
						memoria[j].setInicio(j,numFila++);
					}
					memoria[j].setValores(proceso);
				}
				return true;
			}
		return false;
	}
	
	//Inserta en "la RAM" los valores especificados 
	private static void InsertarDatoValido(int proceso, int tamanio, SectorRam []memoria, ArrayList<Hueco> huecosMemoria) {
		if(InsertarEnHueco(proceso,tamanio, memoria, huecosMemoria)) {
			return;
		}
		//Hacer espacio para el valor a insertar
		Compactacion(memoria,huecosMemoria);
		InsertarEnHueco(proceso,tamanio,memoria,huecosMemoria);
	}
	
	//Inserta en alg�n hueco disponible el proceso indicado
	private static boolean InsertarEnHueco(int proceso, int tamanio, SectorRam []memoria, ArrayList<Hueco> huecosMemoria) {
		int limite;
		for(int i = 0; i < huecosMemoria.size(); i++) {
			if(huecosMemoria.get(i).getTamanio() >= tamanio) { //insertar en hueco
				limite = huecosMemoria.get(i).getInicio() + tamanio;
				for(int j = huecosMemoria.get(i).getInicio(); j < limite; j++) {
					if(j == huecosMemoria.get(i).getInicio()) {
						memoria[j].setInicio(j,numFila++);
					}else if(j == (limite-1)) {
						memoria[j].setFin(j);
					}
					memoria[j].setValores(proceso);
				}
				return true;
			}
		}
		return false;
	}
	
	//Agrupa los espacios en uno solo para aprovechar memoria
	private static void Compactacion(SectorRam []memoria,ArrayList<Hueco> huecos) {
		int inicio = -1;
		
		for(int i = 0; i < memoria.length; i++) {
			if(!memoria[i].getEstado()) {
				if(inicio == -1) {
					inicio = i;
				}
			}else {
				if(inicio != -1) { //hueco
					memoria[inicio].setValores(memoria[i].getProceso(),memoria[i].getEstado(),memoria[i].getInicio(),memoria[i].getFinal(),memoria[i].isCorner(),memoria[i].getNumFila());
					memoria[i].Limpia();
					inicio++;
				}
			}
		}
		ActualizarEspacios(memoria,huecos);
	}

	//Verifica que el n�mero del proceso no est� ya ingresado en la ram y que ese valor sea uno v�lido
	private static int VerificaProcesoExistente() {
		int proceso;
		System.out.println("Ingrese el n�mero del Proceso: ");
		if((proceso = VerificaDatoIngresado()) > 0) {
			for(SectorRam r : ram) {
				if(r.getEstado() && r.getProceso() == proceso) { //ya existe en RAM
					proceso = -1;
				}
			}
			for(SectorRam s : swap) {
				if(s.getEstado() && s.getProceso() == proceso) { //ya existe en SWAP
					proceso = -1;
				}
			}
		}
		return proceso;
	}
	
	//Verifica que el tama�o del proceso sea valido
	private static int VerificaTamanioProceso(int usadoRAM, int usadoSWAP) {
		int tamanio;
		System.out.println("Ingrese el tama�o del Proceso: ");
		if((tamanio = VerificaDatoIngresado()) > 0) {
			if(TAMANIO - usadoRAM >= tamanio) { //Ram Suficiente
				if((TAMANIO * 0.7) <= usadoRAM){
					//Random para saber donde guarda
					if(Math.round(Math.random()) == 0) { //guarda en SWAP
						RAM_SWAP = false;
					}else { //guarda en RAM
						RAM_SWAP = true;
					}
				}
				return tamanio;
			}else if(TAMANIO - usadoSWAP >= tamanio){ //se guarda en RAM
				RAM_SWAP = false;
				return tamanio;
			}else{//Ram y SWAP Insuficiente
				return -1;
			}
		}
		return -1;
	}
	
	//Verifica que el dato ingresado sea n�mero entero
	private static int VerificaDatoIngresado() {
		int dato;
		try {
			dato = Integer.parseInt(scanner.next());
		}catch(Exception e) {
			dato = -1;
			System.out.println("Valor inv�lido");
		}
		return dato;
	}
	
	//Almacena en un arreglo los huecos disponibles y regresa el total de espacios usados
	//Limpia la lista cada vez que es modificado el espacio
	private static int ActualizarEspacios(SectorRam []memoria,ArrayList<Hueco> huecos) {
		int inicio = -1, fin = -1, espacios = 0, huecosActuales = huecos.size();
		
		for(int i = 0; i < huecosActuales; i++) {
			huecos.remove(0);
		}
		
		for(int i = 0; i < memoria.length; i++)
			if(!memoria[i].getEstado()) {
				espacios++;
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
		return TAMANIO - espacios;
	}

	
	//Congela la pantalla para ver los datos obtenidos
	private static void Pausa() {
		System.out.print("\n\n0 para continuar:");
		scanner.next();
	}
	
	//Imprime los huecos existentes, Inicio - Final : Tama�o
	private static void MuestraEspacios(String titulo, ArrayList<Hueco> huecos) {
		System.out.println(titulo);
		for(Hueco h : huecos)
			System.out.println("Inicio " + h.getInicio() + " - Final " + h.getFinal() + " : Total " + h.getTamanio());
	}
	
	//Imprime la RAM como mapa de bits 
	private static void MapaBit(String titulo, SectorRam[] memoria) { //8 pos por fila
		System.out.println("* * * M A P A   D E   B I T S " + titulo + " * * *\n");
		for(int i = 0; i < memoria.length; i++) {
			if( i != 0 && ((i % 8) == 0))
				System.out.print("\n");
			if(memoria[i].getEstado()) {
				System.out.print("1 ");
			}else {
				System.out.print("0 ");
			}
		}
	}
	
	//Imprime la RAM como una fila de procesos
	private static void MostrarRamFila(String titulo, SectorRam[] memoria) {
		System.out.println("* * * L I S T A    D E   P R O C E S O S * * *\n\n");
		System.out.println(titulo + ":");
		for(SectorRam s : memoria) {
			if(s.isCorner()) {
				if(s.getInicio() != -1) { //Es inicio
					System.out.print(" --> [" + s.getInicio() + "] " + s.getProceso());
				}else {
					System.out.print(" [" + s.getFinal() + "]");
				}
			}
		}
	}
	
	//Imprime la RAM como una lista de procesos
	private static void MostrarRamLista(String titulo, SectorRam[] memoria) {
			System.out.println("* * * V E C T O R    D E   P R O C E S O S " + titulo +" * * *\n");
			for(SectorRam s : memoria) {
				if(s.isCorner()) {
					if(s.getInicio() != -1) { //Es inicio
						if(s.getInicio() < 10) {
							System.out.print("Proceso: " + s.getProceso() + "  -->  Inicio: [0" + s.getInicio() + "]");
						}else
							System.out.print("Proceso: " + s.getProceso() + "  -->  Inicio: [" + s.getInicio() + "]");
					}else {
						if(s.getFinal() < 10) {
							System.out.println("  -  Final: [0" + s.getFinal() + "]");
						}else {
							System.out.println("  -  Final: [" + s.getFinal() + "]");
						}
					}
				}
			}
		}
		
	//Borra el primero en haber entrado 
	private static void BorrarCola() {
			int menor = -1;
			int inicio = -1;
			for(SectorRam s : ram) {
				if(s.getNumFila() != -1) {
					if(menor == -1) {
						menor = s.getNumFila();
						inicio = s.getInicio();
					}else if(s.getNumFila() < menor) {
				    	menor = s.getNumFila();
				    	inicio = s.getInicio();
					}
				}
			}
			for(int i = inicio; i < TAMANIO && ram[i].getProceso() == menor; i++) {
				ram[i].Limpia();
			}
		}
	
	private static void MuestraPaginas(String titulo, SectorRam[] memoria) {
		System.out.println("Sectores de la memoria " + titulo + "\n");
		int sector = 1;
		int inicio = 0;
		for(int i = 0; i < memoria.length; i++) {
			if(i == 0) {
				System.out.print("P�gina " + sector++ + ": Inicio " + i);
			}else if(i == memoria.length-1) {
				System.out.print("   -   Final " + i);
			}else if((i % paginacion) == 0 ) {
				System.out.print("   -   Final " + (i-1) + MuestraEstados(inicio,i,memoria) + "\nP�gina " + sector++ + ": Inicio " + i);
				inicio = i+1;
			}
		}
	}
	
	private static String MuestraEstados(int inicio, int fin, SectorRam[] memoria) {
		int num = -1;
		String cad = "  -  Procesos: ";
		for(int i = inicio; i < fin; i++) {
			if(memoria[i].getProceso() != -1 && memoria[i].getProceso() != num) {
				cad+= (memoria[i].getProceso() + "  ");
				num = memoria[i].getProceso();
			}
		}
		return cad;
	} 
	
}
