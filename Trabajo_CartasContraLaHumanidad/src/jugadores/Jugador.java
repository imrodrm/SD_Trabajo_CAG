package jugadores;


import java.util.List;
import java.util.Scanner;

import cartas.Carta;

public class Jugador {
	
	private List<Carta> mano;
	private String nombre;
	private boolean zar;
	
	public Jugador(List<Carta> hand, String name) {
		this.mano=hand;
		this.nombre=name;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void muestraMano() {
		System.out.println("Tus cartas blancas son:\r\n");
		for(int i=0;i<this.mano.size();i++) {
			System.out.println(i + ". " + this.mano.get(i).getTexto() + "\n");
		}
	}
	
	public Carta jugarCarta(int index) {
		return this.mano.remove(index);
	}
	
	public void robaCarta(Carta c) {
		this.mano.add(c);
	}
	
	public int numeroCartas() {
		return this.mano.size();
	}
	
	public Carta elegirCarta(Carta n) {
		Scanner sc=new Scanner(System.in);
		
		//AQUÍ DEBERÍA RECOGER LA CARTA NEGRA QUE ME MANDA EL SV
		this.muestraMano();
		System.out.println("SELECCIONA EL NÚMERO DE LA CARTA QUE QUIERES JUGAR");
		int toPlay=sc.nextInt();
		
		//AQUÍ DEBERÍA ENVIAR LA CARTA QUE QUIERO JUGAR
		sc.close();
		return this.mano.remove(toPlay);
	}
	
	public void setZar(boolean zar) {
		this.zar = zar;
	}
	
	public boolean isZar() {
		return this.zar;
	}
}
