package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		List<BufferedWriter> outputs = new ArrayList<BufferedWriter>();
		List<BufferedReader> inputs = new ArrayList<BufferedReader>();
		Map<String, Integer> puntosJugadores = new HashMap<String, Integer>();
		List<String> nombresJugadores = new ArrayList<String>();
		
		List<Socket> jugadores = Collections.synchronizedList(jug);
		List<BufferedWriter> outputStreams = Collections.synchronizedList(outputs);
		List<BufferedReader> inputStreams = Collections.synchronizedList(inputs);
		
		boolean noMasJugadores = false;
		try (ServerSocket svs = new ServerSocket(555)) {
			while (true) {
//				PARTE DE "RECOLECTAR" JUGADORES, HASTA UN MÁXIMO DE 4
				CyclicBarrier sincronizador = new CyclicBarrier(5);
				while (true && jugadores.size() <= 4 && !noMasJugadores) {
					try (Socket cliente = svs.accept()) {

//						¿Como sincronizo todos los hilos para que los elementos 0 de outputStreams e
//						inputStreams correspondan al mismo index 0 de jugadores? Sin hilos es facil,
//						pero con ellos? Quizá con ellos también, si cada vez que quiera escribir en vez de pasar como argumentos los BufferedReader, paso el jugadores.get(i).getOutputStream();
//						O con InputStream, de esta forma no me importa el orden de las listas puesto que solo tengo 1
						AceptarPeticion peti = new AceptarPeticion(cliente, sincronizador);
						pool.execute(peti);
						jugadores.add(cliente);
						puntosJugadores.put(peti.getNombre(), 0);
						nombresJugadores.add(peti.getNombre());
						outputStreams.add(peti.getBufferedWriter());
						inputStreams.add(peti.getBufferedReader());
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
					pool.execute(new EnviarMensaje("ZAR", outputStreams.get(zar)));
					//LES MANDO A TODOS LOS JUGADORES LA CARTA NEGRA
					enviar = barajaNegras.sacarCarta();
					for (int l = 0; l < jugadores.size(); l++) {
						pool.execute(new EnviarCartas(enviar, outputStreams.get(l)));
					}
					//RECIBIR LA CARTA BLANCA DE LOS JUGADORES Y MANDÁRSELA AL ZAR
					for(int m=0; m<jugadores.size(); m++) {
						if(m!=zar) {
							RecibirCartas rb = new RecibirCartas(inputStreams.get(m));
							pool.execute(rb);
							pool.execute(new EnviarCartas(new Carta(rb.getTextoCarta(), Color.BLANCA), outputStreams.get(zar)));
						}
					}
					//RECIBIR GANADOR DEL ZAR Y ENVIAR AL RESTO DE JUGADORES QUIÉN ES EL GANADOR
					String ganador = inputStreams.get(zar).readLine();
					int gana = Integer.parseInt(ganador);
					puntosJugadores.replace(nombresJugadores.get(gana), puntosJugadores.get(nombresJugadores.get(gana)));
					for(int n=0; n<jugadores.size(); n++) {
						if(n!=zar) {
							pool.execute(new EnviarMensaje(ganador, outputStreams.get(n)));
						}
					}
					//RECUENTO DE PUNTOS
					for(int o=0; o<jugadores.size(); o++) {
						for(int p=0; p<jugadores.size(); p++) {
							pool.execute(new EnviarMensaje("El jugador " + nombresJugadores.get(p) + " tiene " + puntosJugadores.get(nombresJugadores.get(p)), outputStreams.get(o)));
						}
					}
				} while (!juegoTerminado(puntosJugadores.values()) && k < 10);
				for(int q=0; q<jugadores.size(); q++) {
					pool.execute(new EnviarMensaje("LA PERSONA QUE HA GANADO ES " , outputStreams.get(q))); //FALTA DEVOLVER EL NOMBRE DEL GANADOR
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean juegoTerminado(Collection<Integer> mensajesCartas) {
		for (Integer i : mensajesCartas) {
			if (i==4) {
				return true;
			}
		}
		return false;
	}
}
