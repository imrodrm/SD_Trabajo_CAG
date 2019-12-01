package cartas;

public class Carta {

	String texto;
	Color color;
	
	public Carta(String txt, Color c) {
		this.texto=txt;
		this.color=c;
	}
	
	public String toString() {
		return "La carta " + this.color.toString() + " es " + this.texto;
	}
}
