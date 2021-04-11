import java.util.Scanner;

public class Principal {

	//Memorias Usadas
	private static Memoria RAM;
	private static Memoria HDD;
	
	//Lectura de datos
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String args[]) {
		IniciaValores();
		boolean FIN = false;
		while(!FIN) {
			switch(Menu()) {
				case 0:
					return;
				case 1:
					InsertarValores();
					break;
				case 2:
					MuestraMemorias();
					break;
				case 3:
					EliminaDatos();
					break;
				case 4:
					RAM.Compactacion();
					HDD.Compactacion();
					break;
				case 5:
					MuestraMemorias();
					CambiaEstados();
					MuestraMemorias();
					break;
				case 6:
					MuestraRAM();
					break;
				case 7:
					MuestraHDD();
					break;
				case 8:
					VerProcesos();
					break;
				case 9:
					CambioTamanio();
					break;
			}
		}
	}
	
	//Imprime men� y verifica que la entrada sea v�lida
	private static int Menu() {
		int dec = -1;
		
		do {
			System.out.println("\n\n* * * * M E N U * * * *");
			System.out.println(" 1: Ingresar Tarea");
			System.out.println(" 2: Mostrar Memorias");
			System.out.println(" 3: Eliminar Tarea");
			System.out.println(" 4: Compactaci�n");
			System.out.println(" 5: Cambio de Estado");
			System.out.println(" 6: Mostrar RAM");
			System.out.println(" 7: Mostrar HDD");
			System.out.println(" 8: Ver Tareas");
			System.out.println(" 9: Cambiar Tama�o");
			System.out.println(" 0: Salir\n");
			System.out.println("Seleccione una opci�n: ");
			dec = VerificaDatoIngresado();
		}while(dec < 0 || dec > 9);
		return dec;
	}
	
	//Inicializa el tama�o de la memoria Ram y HDD
	private static void IniciaValores() {
		RAM = new Memoria(LeeTamanioMemorias(), "RAM");
		HDD = new Memoria(RAM.getTamanio(), "HDD");
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
	
	//Captura valor v�lido para memoria Ram y HDD
	private static int LeeTamanioMemorias() {
		int tmanio;
		do {
			System.out.print("Ingrese el tama�o que tendr� la memoria RAM y HDD:");
			tmanio = VerificaDatoIngresado();
		}while(tmanio < 10 || tmanio > 100);
		return tmanio;
	}
	
	private static void CambiaTareaMemoria(Tarea tarea, int tam){
		if(RAM.CalculaMemoriaLiberable() >= tam) { //Hay espacio suficiente para liberar
			while(RAM.getEspacioLibre() < tam) {
				RAM.CambiaEstado(HDD);
			}
			RAM.Compactacion();
			AlmacenaTarea(tarea,tam,RAM);
		}else {
			System.out.print("\nEl proceso tiene estado de Ejecuci�n pero no hay espacio en la RAM\n\n");
		}
	}
	
	private static void InsertarValores() {	
		int id, tam;
		Tarea tarea;
		
		if((id = VerificaExistenciaId()) != -1) { //No existe el proceso
			System.out.print("\nIngrese el tama�o del proceso: ");
			if((tam = VerificaDatoIngresado()) != -1) { //Ingresado un n�mero
				tarea = new Tarea(id);
				if(VerificaTamanio(RAM,tam) != -1) { //Hay espacio en la ram             if 1
					AlmacenaTarea(tarea,tam,RAM);
				}else if(tarea.getEstado() == 'x') { //Verificar estado del proceso
					System.out.println("\nEstado debe quedar en Ram");
					CambiaTareaMemoria(tarea,tam);
				}else if(VerificaTamanio(HDD,tam) != -1){ //se guarda estado en HDD si hay espacio
					AlmacenaTarea(tarea,tam,HDD);
				}else {
					System.out.println("\nEspacio insuficiente");
				}
			}else {
				System.out.print("\nValor inv�lido");
			}
		}
	}
	
	private static void AlmacenaTarea(Tarea tarea, int tam, Memoria memoria) {
		if(memoria.GuardaDatos(tarea,tam)) {
			System.out.println("Tarea en " + memoria.getTitulo());
		}else {
			memoria.Compactacion();
			memoria.GuardaDatos(tarea,tam);
			System.out.println("Tarea en " + memoria.getTitulo());
		}
	}
	
	//Verifica que el id ingresado sea tipo num�rico y no est� ya dado de alta
	private static int VerificaExistenciaId() {
		System.out.print("\nIngrese el Id del proceso: ");
		int id = VerificaDatoIngresado();
		if(id != -1 && !RAM.VerificaIdExistente(id) && !HDD.VerificaIdExistente(id))
			return id;
		else {
			System.out.print("\nEl dato ya fue ingresado, es inv�lido");
			return -1;
		}
	}
	
	//Verifica que el tama�o ingresado sea menor o igual al existente en alguna memoria
	private static int VerificaTamanio(Memoria memoria, int tam){				
		if(memoria.getEspacioLibre() >= tam)
			return tam;
		return -1;
	}
	
	//Imprime los huecos existentes en la memoria	
	private static void EliminaDatos() {
		int id;
		
		RAM.MuestraTareasExistentes();
		HDD.MuestraTareasExistentes();
		
		System.out.print("\nQue tarea quiere borrar:");
		if((id = VerificaDatoIngresado()) != -1) {
			if(RAM.EliminarTarea(id)) { //se elimin� de la ram
				System.out.print("\nLa tarea "+ id + " fue borrada de RAM\n\n");
			}else if(HDD.EliminarTarea(id)) { //se elimin� de hdd
				System.out.print("\nLa tarea "+ id + " fue borrada de HDD\n\n");
			}else {
				System.out.print("\nLa tarea "+ id + " no fue encontrada\n\n");
			}
		}
	}
	
	private static void CambiaEstados() {
		int opcion;
		System.out.print("\nQue tipo de cambio quiere realizar?\n\n");
		System.out.print("1: Aleatorio\n");
		System.out.print("2: Manual");
		
		if((opcion = VerificaDatoIngresado()) != -1) {
			if(opcion == 1) {
				CambioEstadoAleatorio();
			}else if(opcion == 2) {
				CambioEstadoManual();
			}
		}
	}

	private static void CambioEstadoAleatorio() {
		RAM.CambiaEstadosAleatorio();
		HDD.CambiaEstadosAleatorio();
		IntercambioTareas();
	}
	
	private static void IntercambioTareas() {
		Hueco hueco;
		Tarea tarea;
		int pos;
		/*--------------------------------------------------------------------------------------------------------------------------------*/
		while((pos = HDD.BuscaEjecucion()) != -1 ) {
			tarea = new Tarea(HDD.getIDTarea(pos),'x');
			hueco = new Hueco(pos, HDD.getFinalPosTarea(pos));
			HDD.EliminarTarea(tarea.getId());
			if((RAM.getEspacioLibre() + RAM.CalculaMemoriaLiberable()) >= hueco.getTamanio()) { //si se pueden mover suficientes procesos para agregar uno nuevo
				System.out.println("\nif-2");
				HDD.Compactacion();
				RAM.IntercambioTareas(hueco.getTamanio(), HDD);
				RAM.Compactacion();
				RAM.GuardaDatos(tarea,hueco.getTamanio());
				System.out.println("Tarea en RAM");
			}else {
				System.out.println("\nif-3");
				System.out.println("Imposible agregar la Tarea " + tarea.getId() + " en la RAM, espacio insuficiente\n");
			}
		}
		RAM.Compactacion();
		HDD.Compactacion();
	}
	
	private static void CambioEstadoManual() {
		int opcion;
		int id;
		
		System.out.print("\nQue Tarea quiere modificar?\n\n");
		if((id = VerificaDatoIngresado()) != -1) { //Se lee un n�mero
			if(RAM.VerificaIdExistente(id) || HDD.VerificaIdExistente(id)) { //Existe el id dado
				System.out.print("\nQue Estado quiere que tenga?\n\n");
				System.out.print("\n1: Listo\n2: En Espera\n3: En Ejecuci�n\n\n");
				if((opcion = VerificaDatoIngresado()) != -1) { //se ley� un n�mero
					switch(opcion) {
						case 1:
								RAM.setEstadoTarea(id, 'r');
								HDD.setEstadoTarea(id, 'r');
							break;
						case 2:	
								RAM.setEstadoTarea(id, 'w');
								HDD.setEstadoTarea(id, 'w');
							break;
						case 3:	
								RAM.setEstadoTarea(id, 'x');
								HDD.setEstadoTarea(id, 'x');
								CambiaTareasX();
							break;
					}
					//IntercambioTareas();
					
				}else { //El id dado no est� en RAM ni HDD
					System.out.print("La opci�n elegida es invalida");
				}
			}else { //El id dado no est� en RAM ni HDD
				System.out.print("El n�mero de Tarea dado no es v�lido");
			}
		}
	}	
	
	//CambiaTareaMemoria(Tarea tarea, int tam){
	private static void CambiaTareasX() {
		int id = HDD.isTareaEjecutable();
		char est;
		int tam;
		if(id != -1) {
			est = HDD.getEstadoTareaID(id);
			tam = HDD.calculaTamTarea(id);
			HDD.EliminarTarea(id);
			CambiaTareaMemoria(new Tarea(id,est), tam);
		}
	}
	
	private static void MuestraMemorias() {
		System.out.println("\n--------------------------------------");
		RAM.MostrarMemoria();
		System.out.println("\n--------------------------------------");
		HDD.MostrarMemoria();
		System.out.println("\n--------------------------------------\n");
	}
	
	private static void MuestraRAM() {
		int opcion;
		
		System.out.println("Seleccione el modo en que quiere mostrar la RAM\n");
		System.out.println("1: Contenido");
		System.out.println("2: Mapa de Bits");
		System.out.println("3: Lista de Huecos\n");
		System.out.println("Seleccione la opci�n que desea realizar: ");
		if((opcion = VerificaDatoIngresado()) != -1) {
			System.out.println("\n--------------------------------------");
			switch(opcion) {
				case 1:
					RAM.MuestraHuecos();
					break;
				case 2:
					RAM.MapaBit();
					break;
				case 3:
					RAM.ListaDatos();
					break;
			}
			System.out.println("\n--------------------------------------");
		}
	}

	private static void MuestraHDD() {
		System.out.println("\n--------------------------------------");
		HDD.MuestraHuecos();
		System.out.println("\n--------------------------------------\n");
	}

	private static void VerProcesos() {
		Hueco hueco;
		Tarea tarea;
		int pos = 0;
		
		while((pos = HDD.BuscaTarea(pos)) != -1 ) {
			tarea = new Tarea(HDD.getIDTarea(pos),HDD.getEstadoTarea(pos));
			hueco = new Hueco(pos, HDD.getFinalPosTarea(pos));
			HDD.EliminarTarea(tarea.getId());
			if(RAM.IntercambioTareas(hueco.getTamanio(), HDD)) {
				RAM.Compactacion();
				RAM.GuardaDatos(tarea,hueco.getTamanio());
			}
		}
	}
	
	private static void CambioTamanio() {
		int opcion;
		int id;
		int tam;
				
		System.out.print("\nQue Tarea quiere cambiar de tama�o:");
		if((id = VerificaDatoIngresado()) != -1) {
			if(RAM.VerificaIdExistente(id)) {
				CalculaEspacio(RAM,id);
			}else if(HDD.VerificaIdExistente(id)) {
				CalculaEspacio(HDD,id);
			}else {
				System.out.println("\nTarea invalida");
			}
		}else {
			System.out.println("\nError al ingresar el dato");
		}
		
	}
	
	private static void CalculaEspacio(Memoria memoria, int id) {
		int tamanio = memoria.getTamanioTarea(id);
		Hueco hueco;
		Tarea tarea;
		int pos = memoria.getPosicionTarea(id);
		
		tarea = new Tarea(id,memoria.getEstadoTareaID(id));
		hueco = new Hueco(pos, memoria.getFinalPosTarea(pos));
		
		System.out.print("\nEl tama�o de la Tarea " + id + " es: " + hueco.getTamanio());
		System.out.print("\nCual ser� el nuevo tama�o:");
		if((tamanio = VerificaDatoIngresado()) != -1) {
			if((memoria.getEspacioLibre() + hueco.getTamanio()) >= tamanio) {
				CambiaTamanioExistente(memoria, tarea, tamanio);
			}else {
				System.out.println("\nEspacio insuficiente");
			}
		}else {
			System.out.println("\nError al ingresar el dato");
		}
	}
	
	private static void CambiaTamanioExistente(Memoria memoria, Tarea tarea, int tamanio) {
		memoria.EliminarTarea(tarea.getId());
		if(memoria.GuardaDatos(tarea, tamanio)) {
			System.out.println("La tarea " + tarea.getId() + " ahora tiene un tama�o de " + tamanio);
		}else {
			memoria.Compactacion();
			memoria.GuardaDatos(tarea, tamanio);
			System.out.println("La tarea " + tarea.getId() + " ahora tiene un tama�o de " + tamanio);
		}
		
	}
	
}


