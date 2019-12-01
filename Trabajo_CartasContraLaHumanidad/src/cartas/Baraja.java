package cartas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Baraja {
	private Color color;
	private List<Carta> cartas = new ArrayList <>();
	
	private Baraja(Color c, ArrayList<Carta> listaCartas) {
		this.color=c;
		this.cartas=listaCartas;
	}
	
	public void barajar() {
		Random r = new Random();
		Carta aux;
		for(int i=0; i<50; i++) {
			int j = r.nextInt(this.cartas.size());
			int k = r.nextInt(this.cartas.size());
			aux=this.cartas.get(j);
			this.cartas.set(j, this.cartas.get(k));
			this.cartas.set(k, aux);
		}
	}
	
	public Color getColor() {
		return this.color;
	}
	public Carta sacarCarta() {
		return this.cartas.remove(this.cartas.size()-1);
	}
	
	public boolean vacio() {
		return (this.cartas.size()==0);
	}
}
