package jugadores;


import java.util.List;

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
		System.out.println("Tus cartas blancas son:");
		for(int i=0;i<this.mano.size();i++) {
			System.out.println(i + ". " + this.mano.get(i).getTexto());
		}
	}
	
	public Carta jugarCarta(int index) {
		return this.mano.remove(index);
	}
	
	public void robaCarta(Carta c) {
		this.mano.add(c);
	}
	
	public void setZar(boolean zar) {
		this.zar = zar;
	}
	
	public boolean isZar() {
		return this.zar;
	}
}
