import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Principal {
	
	private static Archivo[] memoria;   //Arreglo de Archivos a utilizar
	private static int tamanioMemoria;  //Tamaño total de la memoria
	private static int tamanioSecciones;//Tamaño total de cada sección 
	private static Scanner scanner;     //Variable para lectura
	
	public static void main(String []args) {
		boolean Fin = true;
		
		InicializaVariables();
		
		while(Fin) {
			switch(Menu()) {
				case 0:
					Fin = false;
					break;
				case 1:
					CrearArchivo();
					break;
				case 2:
					System.out.println("------------------------------------------");
					MostrarMemoria();
					System.out.println("------------------------------------------");
					break;
				case 3:
					EliminarArchivo();
					break;
				case 4:
					Desfragmentar();
					break;
			}
		}
	}
	
	//Muestra menú de opciones y valida el valor ingresado
	private static int Menu() {
		int opcion = 0;
		do {
			System.out.println("*   *   *   * M E N U *   *   *   *\n");
			System.out.println("1: Crear Archivo");
			System.out.println("2: Mostrar Memoria");
			System.out.println("3: Eliminar Archivo");
			System.out.println("4: Desfragmentar");
			System.out.println("0: Salir");
			System.out.println("\nSeleccione una opción:");
			opcion = VerificaEntero();
		}while(opcion < 0 || opcion > 4);
		return opcion;
	}
	
	//Valida que el tipo de dato ingresado sea entero
	private static int VerificaEntero() {
		int num;
		try {
			num = Integer.parseInt(scanner.next());
		}catch(Exception e) {
			num = -1;
			System.out.println("Valor Inválido\n\n");
		}
		return num;
	} 
	
	//Método para inicializar variables Globales
	private static void InicializaVariables() {
		scanner = new Scanner(System.in);
		tamanioMemoria = LeeLimiteTamanio(30,99,"la memoria");
		tamanioSecciones = LeeLimiteTamanio(2,10,"las secciones");
		if(tamanioMemoria%tamanioSecciones != 0) {
			memoria = new Archivo[(tamanioMemoria/tamanioSecciones)+1];
			for(int i = 0; i < memoria.length-1; i++) {
				memoria[i] = new Archivo(tamanioSecciones);
			}
			memoria[memoria.length-1] = new Archivo(tamanioMemoria%tamanioSecciones);
		}else {
			memoria = new Archivo[tamanioMemoria/tamanioSecciones];
			for(int i = 0; i < memoria.length; i++) {
				memoria[i] = new Archivo(tamanioSecciones);
			}
		}
	}
	
	//Método para validar que la memoria tenga un tamaño entre un mínimo y máximo dados
	private static int LeeLimiteTamanio(int min, int max, String cad) {
		int tam = -1;
		while(tam < min || tam > max) {
			System.out.println("Ingresa el tamaño de " + cad + " en bytes:");
			tam = VerificaEntero();
		}
		return tam;
	}	
	
	//Imprime el estádo actual de la memoria 
	private static void MostrarMemoria() {
		System.out.print("La memoria tiene capacidad de " + tamanioMemoria + " bytes dividida en secciones de " + tamanioSecciones + " bytes\n\n");
		for(int i = 0; i < memoria.length; i++) {
			if(i < 10) {
				System.out.print("[0" + i + "]");
			}else {
				System.out.print("[" + i + "]");
			}
			System.out.print(" tamaño " + memoria[i].getTamanio() + " bytes - en uso " + memoria[i].getUsado() + " bytes");
			if(!memoria[i].getUso()) {
				System.out.println(" -> Vacio");
			}else {
				System.out.println(" -> " + memoria[i].getNombre());
			}
		}
	}
	
	//Lee y verifica datos del Archivo nuevo
	private static void CrearArchivo() {
		String nombre;
		int tamanio;
		
		System.out.println("\n\nEl espacio disponible es de " + CalculaTamanioMemoria() + " bytes");
		System.out.print("Ingresa el nombre del Archivo:");
		nombre= LecturaString();
		if(!VerificaExistenciaNombre(nombre)) {
			System.out.print("Ingresa el tamaño del Archivo:");
			if((tamanio = VerificaEntero()) != -1) {
				if(VerificaTamanio(tamanio)) {
					GuardaArchivo(nombre,tamanio);
				}else {
					System.out.println("El espacio disponible no es suficiente.\n");
				}
			}
		}else {
			System.out.println("Ese nombre de Archivo ya está en uso\n");
		}
		
		
	}
	
	//Lectura de cadena completa tipo String 
	private static String LecturaString() {
		InputStreamReader flujo = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(flujo);
		String s = "";
		
		try {
			s = br.readLine();
			return s;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
	}

	//Comprueba que el nombre ingresado no esté ya dado de alta
	private static boolean VerificaExistenciaNombre(String nombre) {
		for(Archivo a : memoria) {
			if(a.getUso() && a.getNombre().equals(nombre)) {
				return true;
			}
		}
		return false;
	}

	//Verifica que el espacio sea apropiado para almacenar el Archivo
	private static boolean VerificaTamanio(int tam) {		
		if(tam > tamanioMemoria || tam < 1) {
			System.out.println("El tamaño del Archivo es inválido\n");
		}else if(CalculaTamanioMemoria() >= tam) {
				return true;
		}
		return false;
	}
	
	//Calcula y retorna la cantidad total de memoria disponible para ser usada
	private static int CalculaTamanioMemoria() {
		int disponible = 0;
		
		for(int i = 0; i < memoria.length; i++) {
			if(!memoria[i].getUso()) {
				disponible+=memoria[i].getTamanio();
			}
		}
		
		return disponible;
	}
	
	//Guarda los datos del archivo en memoria
	private static void GuardaArchivo(String nombre,int tamanio) {
		
		for(int i = 0; i < memoria.length; i++) {
			if(!memoria[i].getUso()) {
				tamanio-=memoria[i].setValores(nombre,tamanio);
			}
			System.out.println("memoria: " + memoria[i].getTamanio() + "  -  tamanio:" + tamanio);
			if(tamanio <= 0)
				break;
		}
		
	}
	
	//Elimina un archivo de memoria
	private static void EliminarArchivo() {
		String nombre;
		
		System.out.print("Ingresa el nombre del Archivo a Eliminar:");
		nombre= LecturaString();
		if(VerificaExistenciaNombre(nombre)) {
			for(int i = 0; i < memoria.length; i++) {
				if(memoria[i].getNombre().equals(nombre)) {
					memoria[i].Limpia();
				}
			}
			System.out.println("El Archivo " + nombre + " fue Eliminado de la memoria\n");
		}else {
			System.out.println("Nombre de Archivo Erroneo\n");
		}
	}

	//Desfragmentar Memoria
	private static void Desfragmentar() {
		Archivo aux;
		
		for(int i = 0; i < (memoria.length-1); i++) {
			//System.out.println("i: "  + i);
			for(int j = i+1; j < memoria.length; j++) {
				//System.out.println("j: "  + j);
				if(memoria[i].getNombre().equals(memoria[j].getNombre())) {
					//System.out.println("if");
					aux = memoria[i+1]; 
					memoria[i+1] = memoria[j];
					memoria[j] = aux;
					break;
				}
			}
		}
	}
	
}
