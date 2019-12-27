package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cartas.Baraja;
import cartas.Carta;
import cartas.Color;

public class Servidor_CardsAgainstHumanity {

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(4);
		
		List<Socket> jug = new ArrayList<Socket>();
		List<DataOutputStream> outputs = new ArrayList<DataOutputStream>();
		List<DataInputStream> inputs = new ArrayList<DataInputStream>();
		
		List<Socket> jugadores = Collections.synchronizedList(jug);
		List<DataOutputStream> outputStreams = Collections.synchronizedList(outputs);
		List<DataInputStream> inputStreams = Collections.synchronizedList(inputs);
		
		boolean noMasJugadores = false;
		try (ServerSocket svs = new ServerSocket(555)) {
			while (true) {
//				PARTE DE "RECOLECTAR" JUGADORES, HASTA UN MÁXIMO DE 4
				CyclicBarrier sincronizador = new CyclicBarrier(5);
				while (true && jugadores.size() <= 4 && !noMasJugadores) {
					try (Socket cliente = svs.accept()) {

						// ¿Como sincronizo todos los hilos para que los elementos 0 de outputStreams e
						// inputStreams correspondan al mismo index 0 de jugadores? Sin hilos es facil,
						// pero con ellos?
						AceptarPeticion peti = new AceptarPeticion(cliente, sincronizador);
						pool.execute(peti);
						jugadores.add(cliente);
						outputStreams.add(peti.getDOS());
						inputStreams.add(peti.getDIS());
					}
				}
				// PREPARANDO EL JUEGO
				List<Carta> cartasNegras = Collections.synchronizedList(CrearCartas.crearNegras());
				List<Carta> cartasBlancas = Collections.synchronizedList(CrearCartas.crearBlancas());
				Baraja barajaNegras = new Baraja(Color.NEGRA, cartasNegras);
				Baraja barajaBlancas = new Baraja(Color.BLANCA, cartasBlancas);
				barajaNegras.barajar();
				barajaBlancas.barajar();
				Carta enviar;
				List<String> numeroCartasNegras = new ArrayList<String>();
				for (int i = 0; i < jugadores.size(); i++) {
					//pool.execute(repartir()); Como lo hago con hilos?. Al ser synchronizedList no hay problema en pasarles la baraja, o si lo hay?
//					for (int j = 0; j < 60; j++) {
//						enviar = barajaBlancas.sacarCarta();							ESTO HACERLO CON CADA HILO
//						outputStreams.get(i).writeChars("Blanca-" + enviar.getTexto());
//					}
				}
				int k = 0;
				do {
					//DESIGNO QUIÉN ES EL ZAR
					int zar = k % 4;
					outputStreams.get(zar).writeChars("ZAR");
					//LES MANDO A TODOS LOS JUGADORES LA CARTA NEGRA
					enviar = barajaNegras.sacarCarta();
					for (int l = 0; l < jugadores.size(); l++) {
						pool.execute(new EnviarCartas(enviar, outputStreams.get(l)));
					}
					
					//RECIBIR LA CARTA BLANCA DE LOS JUGADORES
					
					//MANDAR AL ZAR LAS BLANCAS
					
					//RECIBIR GANADOR DEL ZAR
					
					//RECUENTO DE PUNTOS
					
					//MIRO A VER SI ALGUIEN HA GANADO YA
					
				} while (!juegoTerminado(numeroCartasNegras) && k < 10);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean juegoTerminado(List<String> mensajesCartas) {
		for (String s : mensajesCartas) {
			if (s.equalsIgnoreCase("4")) {
				return true;
			}
		}
		return false;
	}
}
