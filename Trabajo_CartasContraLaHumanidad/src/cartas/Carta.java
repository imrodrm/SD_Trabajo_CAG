package cartas;

public class Carta {

	private String texto;
	private Color color;
	
	public Carta(String txt, Color c) {
		this.texto=txt;
		this.color=c;
	}
	
	public String getTexto() {
		return this.texto;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public String toString() {
		return "La carta " + this.color.toString() + " es " + this.texto;
	}
}
