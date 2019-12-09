package cartas;

import java.util.ArrayList;
import java.util.List;

public class Mesa {
	
	private List<Carta> cartasBlancas;
	private Carta cartaNegra;
	
	public Mesa() {
		this.cartasBlancas = new ArrayList<Carta>();
	}
	
//	C ha de ser una carta negra
	public void aniadirNegra(Carta c) {
		this.cartaNegra=c;
	}
//	C ha de ser una carta blanca
	public void juegaBlanca(Carta c) {
		this.cartasBlancas.add(c);
	}
	
	public String cartasBlancas() {
		String devuelve = "Las cartas de esta ronda son: \n";
		for(int i=0; i<this.cartasBlancas.size(); i++) {
			devuelve+= i + ". " + this.cartasBlancas.get(i).getTexto();
		}
		return devuelve;
	}
	
	public String getCartaNegra() {
		return "La carta negra de esta ronda es: " + this.cartaNegra.getTexto();
	}

	
	public Carta elegirGanadora(int i) {
		return this.cartasBlancas.get(i);
	}
	
	public void nuevoTurno() {
		this.cartasBlancas.clear();
		this.cartaNegra = null;
	}
}
