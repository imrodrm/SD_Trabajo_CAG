package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cartas.Baraja;
import cartas.Carta;
import cartas.Color;

public class Servidor_CardsAgainstHumanity {
	

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(4);
		List<Socket> jugadores = new ArrayList<Socket>();
		List<DataOutputStream> outputStreams = new ArrayList<DataOutputStream>();
		List<DataInputStream> inputStreams = new ArrayList<DataInputStream>();
		boolean noMasJugadores=false;
		try(ServerSocket svs = new ServerSocket(555)){
//			PARTE DE "RECOLECTAR" JUGADORES, HASTA UN MÁXIMO DE 4
			CyclicBarrier sincronizador = new CyclicBarrier(5);
			while(true && jugadores.size()<=4 && !noMasJugadores) {
				try(Socket cliente = svs.accept()){
					
					//¿Como sincronizo todos los hilos para que los elementos 0 de outputStreams e inputStreams correspondan al mismo index 0 de jugadores? Sin hilos es facil, pero con ellos?
					AceptarPeticion peti =new AceptarPeticion(cliente, sincronizador);
					pool.execute(peti);
					jugadores.add(cliente);
					outputStreams.add(peti.getDOS());
					inputStreams.add(peti.getDIS());
				}
			}
			//PREPARANDO EL JUEGO
			ArrayList<Carta> cartasNegras = CrearCartas.crearNegras();
			ArrayList<Carta> cartasBlancas = CrearCartas.crearBlancas();
			Baraja negras = new Baraja(Color.negra, cartasNegras);
			Baraja blancas = new Baraja(Color.blanca, cartasBlancas);
			List<String> numeroCartasNegras = new ArrayList<String>();
			int i=0;
			do {
				
			}while(!juegoTerminado(numeroCartasNegras) && i<10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean juegoTerminado(List<String> mensajesCartas) {
		for(String s: mensajesCartas) {
			if(s.equalsIgnoreCase("4")) {
				return true;
			}
		}
		return false;
	}
}
