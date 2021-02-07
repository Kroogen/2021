package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PantallaPrincipal extends JFrame{

	private JPanel panelRam;
	
	public PantallaPrincipal(){
		super("Proyecto RAM");
		IniciarValores();
		
		this.setSize(1300,500);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void IniciarValores(){
		panelRam = (JPanel) this.getContentPane();
	}
	
}