package jugadores;

import java.util.ArrayList;
import java.util.List;

import cartas.Carta;

public class Jugador {
	
	private List<Carta> mano = new ArrayList<Carta>();
	private String nombre;
	private boolean zar;
	
	public Jugador(List<Carta> hand, String name) {
		this.mano=hand;
		this.nombre=name;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String muestraMano() {
		String mano = "Tus cartas blancas son: \n";
		for(int i=0;i<this.mano.size();i++) {
			mano+= i + ". " + this.mano.get(i).getTexto() + "\n";
		}
		return mano;
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
	
	public void elegirCarta() {
		
	}
	
	public void cambiarZar() {
		this.zar= !this.zar;
	}
	
	public boolean isZar() {
		return this.zar;
	}
}
