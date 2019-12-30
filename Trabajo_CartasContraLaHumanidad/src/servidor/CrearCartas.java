package servidor;

import java.util.ArrayList;

import cartas.Carta;
import cartas.Color;

public class CrearCartas {
	
	public static ArrayList<Carta> crearNegras(){
		ArrayList<Carta> negras = new ArrayList<>();
		Carta uno = new Carta("La razon de que haya venido sin hacer el ejercicio es ____________" ,Color.NEGRA);
		negras.add(uno);
		Carta dos = new Carta("Hoy el tio de Bricomania construira ____________" ,Color.NEGRA);
		negras.add(dos);
		Carta tres = new Carta("Que mejora con los años?" ,Color.NEGRA);
		negras.add(tres);
		Carta cuatro = new Carta("Lo siento, acabo de ____________ otra vez" ,Color.NEGRA);
		negras.add(cuatro);
		Carta cinco = new Carta("Ojala no hubiese perdido el manual de ____________" ,Color.NEGRA);
		negras.add(cinco);
		Carta seis = new Carta("En vez de carbón, este ano Papa Noel le va a traer a los ninos malos ____________" ,Color.NEGRA);
		negras.add(seis);
		Carta siete = new Carta("Para celebrar sus ventas, el nuevo producto de BIC es ____________" ,Color.NEGRA);
		negras.add(siete);
		Carta ocho = new Carta("Bebo para olvidar ____________" ,Color.NEGRA);
		negras.add(ocho);
		Carta nueve = new Carta("La (inútil) medicina alternativa usa las propiedades curativas de ____________" ,Color.NEGRA);
		negras.add(nueve);
		Carta diez = new Carta("Que ayuda a Obama a relajarse?" ,Color.NEGRA);
		negras.add(diez);
		Carta once = new Carta("EE.UU ha empezado a enviar ____________ a los ninos de Afganistan" ,Color.NEGRA);
		negras.add(once);
		Carta doce = new Carta("El paraiso esta lleno de ____________" ,Color.NEGRA);
		negras.add(doce);
		Carta trece = new Carta("Cual es el vicio secreto de Batman?" ,Color.NEGRA);
		negras.add(trece);
		Carta catorce = new Carta("Que es lo mas crujiente?" ,Color.NEGRA);
		negras.add(catorce);
		Carta quince = new Carta("A los blancos les gusta ____________" ,Color.NEGRA);
		negras.add(quince);
		Carta dieciseis = new Carta("La excursion al campo fue completamente arruinada por ____________" ,Color.NEGRA);
		negras.add(dieciseis);
		Carta diecisiete = new Carta("Que mejora con los anos?" ,Color.NEGRA);
		negras.add(diecisiete);
		Carta dieciocho = new Carta("Que es ese sonido?" ,Color.NEGRA);
		negras.add(dieciocho);
		Carta diecinueve = new Carta("It's a trap" ,Color.NEGRA);
		negras.add(diecinueve);
		Carta veinte = new Carta("A que no puedes comer solo uno!" ,Color.NEGRA);
		negras.add(veinte);
		
		return negras;
	}

	public static ArrayList<Carta> crearBlancas(){
		ArrayList<Carta> blancas = new ArrayList<>();
		Carta uno = new Carta("Pobres decisiones de vida", Color.BLANCA);
		blancas.add(uno);
		Carta dos = new Carta("Keanu Reeves", Color.BLANCA);
		blancas.add(dos);
		Carta tres = new Carta("Abstintenca", Color.BLANCA);
		blancas.add(tres);
		Carta cuatro = new Carta("", Color.BLANCA);
		blancas.add(cuatro);
		Carta cinco = new Carta("Kanye West", Color.BLANCA);
		blancas.add(cinco);
		Carta seis = new Carta("El conejo de Nesquick", Color.BLANCA);
		blancas.add(seis);
		Carta siete = new Carta("Obesidad", Color.BLANCA);
		blancas.add(siete);
		Carta ocho = new Carta("BATMAN", Color.BLANCA);
		blancas.add(ocho);
		Carta nueve = new Carta("Frank de la Jungla", Color.BLANCA);
		blancas.add(nueve);
		Carta diez = new Carta("Ir a catequesis", Color.BLANCA);
		blancas.add(diez);
		return blancas;
	}
}
