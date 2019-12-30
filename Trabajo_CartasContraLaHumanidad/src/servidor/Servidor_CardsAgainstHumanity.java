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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import cartas.Baraja;
import cartas.Carta;
import cartas.Color;

public class Servidor_CardsAgainstHumanity {

	public static void main(String[] args) {
		try (ServerSocket svs = new ServerSocket(5555)) { //"Abro" el sv
			while (true) {
				List<Socket> jug = new ArrayList<Socket>();
				List<BufferedWriter> outputs = new ArrayList<BufferedWriter>();
				List<BufferedReader> inputs = new ArrayList<BufferedReader>();
				Map<String, Integer> puntosJug = new HashMap<String, Integer>();
				List<String> nombresJug = new ArrayList<String>();
				
				List<Socket> jugadores = Collections.synchronizedList(jug);
				List<BufferedWriter> outputStreams = Collections.synchronizedList(outputs);
				List<BufferedReader> inputStreams = Collections.synchronizedList(inputs);
				Map<String, Integer> puntosJugadores = Collections.synchronizedMap(puntosJug);
				List<String> nombresJugadores = Collections.synchronizedList(nombresJug);
				
				//boolean noMasJugadores = false;
				boolean terminado = false;
//				PARTE DE "RECOLECTAR" JUGADORES, HASTA UN MÁXIMO DE 4.  NO FUNCIONA
				while(jugadores.size()<4) {
					try {
						Socket cliente = svs.accept();
//						Se que debería hacerlo con hilos, pero si tengo que esperar en el principal para
//						añadir los jugadores, Readers, Writers, etc, en el mismo orden, creo que no crear los hilos
//						ahorrará memoria.
						System.out.println("Esperando a los 4 jugadores");
						AceptarPeticion hilo_1 = new AceptarPeticion(cliente);
						hilo_1.run();
						System.out.println("TikiTaka");
						hilo_1.join();
						jugadores.add(cliente);
						inputStreams.add(hilo_1.getBr());
						outputStreams.add(hilo_1.getBw());
						puntosJugadores.put(hilo_1.getNombre(), 0);
						nombresJugadores.add(hilo_1.getNombre());
						if(hilo_1.getUlti()) {
//							noMasJugadores=true;
						}
						System.out.println("Jugador " + hilo_1.getNombre() + " anadido");
					}catch(IOException e) {
						e.printStackTrace();
					}
				}
					ExecutorService pool = Executors.newFixedThreadPool(jugadores.size());
					final CyclicBarrier sincronizador_5 = new CyclicBarrier(jugadores.size()+1);
					final CyclicBarrier sincronizador_4 = new CyclicBarrier(jugadores.size());
					System.out.println("Ultimo jugador, empieza el juego");
//					PREPARANDO EL JUEGO...
					System.out.println("Repartiendo...");
					for(int i=0; i<jugadores.size(); i++) {
						pool.execute(new EnviarMensaje(jugadores.size() + "\r\n", outputStreams.get(i), sincronizador_5));
					}
					sincronizador_5.await();
					sincronizador_5.reset();
					
					List<Carta> cartasNegras = Collections.synchronizedList(CrearCartas.crearNegras());
					List<Carta> cartasBlancas = Collections.synchronizedList(CrearCartas.crearBlancas());
					Baraja barajaNegras = new Baraja(Color.NEGRA, cartasNegras);
					Baraja barajaBlancas = new Baraja(Color.BLANCA, cartasBlancas);
					barajaNegras.barajear();
					barajaBlancas.barajear();
					
					Carta enviar;
					List<List<Carta>> manos = crearManos(jugadores.size(), barajaBlancas);
					for (int j = 0; j < jugadores.size(); j++) {
						//Repartir cartas
						pool.execute(new EnviarMano(manos.get(j), outputStreams.get(j), sincronizador_5));
					}
					sincronizador_5.await();
					sincronizador_5.reset();
					System.out.println("Todas las cartas enviadas");
					int turno = 0;
					do {
//					DESIGNO QUIÉN ES EL ZAR
						int zar = turno% 4; //int zar = 4 Para probar cuando no se es zar...
						for (int k = 0; k < jugadores.size(); k++) {
							if (k != zar) {
								pool.execute(new EnviarMensaje("NoZAR\r\n", outputStreams.get(k), sincronizador_5)); //Si no es el zar, le envio un mensaje de que el no lo es
							} else {
								pool.execute(new EnviarMensaje("ZAR\r\n", outputStreams.get(k), sincronizador_5)); //Si es el zar, le aviso de ello.
							}
						}
						sincronizador_5.await();
						sincronizador_5.reset();
//					LES MANDO A TODOS LOS JUGADORES LA CARTA NEGRA
						enviar = barajaNegras.sacarCarta();
						for (int l = 0; l < jugadores.size(); l++) {
							pool.execute(new EnviarCartas(enviar, outputStreams.get(l), sincronizador_5));
						}
						sincronizador_5.await();
						sincronizador_5.reset();
//					RECIBIR LA CARTA BLANCA DE LOS JUGADORES (menos del Zar)
						Map<String, String> textoCartas = new HashMap<String, String>();
						List<RecibirCartas> recibido = new ArrayList<RecibirCartas>();
						System.out.println("Esperando a las cartas de los jugadores...");
						for (int m = 0; m < jugadores.size(); m++) {
							if (m != zar) {
//								RecibirCartas rb = new RecibirCartas(inputStreams.get(m), sincronizador_5);
								RecibirCartas rb = new RecibirCartas(inputStreams.get(m), sincronizador_4); 
								pool.execute(rb);
								recibido.add(rb);
							}
						}
						//Si quiero probar el zar, comentar el for y poner esto descomentado...
//						textoCartas.put("Ima", "no tira soy inutil");
//						textoCartas.put("Javi", "El seguro que lo sabria hacer");
//						Ahora tengo que usar los del sincronizador_5 porque solo consigo un cliente, que sad todo y que useless soy:
						sincronizador_4.await();
						sincronizador_4.reset();
//						sincronizador_5.await();
//						sincronizador_5.reset();
						for(RecibirCartas rc : recibido) {
							String[] a = rc.getTextoCarta().split("-");
							textoCartas.put(a[0], a[1]); // a[0] nombreJugador, a[1] textoCarta
						}
//					MANDARLE LAS CARTAS AL ZAR
						//Si no tengo zar, comentar esto
						for (String s : textoCartas.values()) {
								outputStreams.get(zar).write(s +"\r\n"); //No lo hago con hilos para que no se rompa el orden
								outputStreams.get(zar).flush();
						}
//					RECIBIR GANADOR DEL ZAR Y ENVIAR AL RESTO DE JUGADORES QUIÉN ES EL GANADOR
						System.out.println("Esperando a que el zar elija...");
						String ganador = inputStreams.get(zar).readLine();
						 //Si no tengo zar, para probar sustituir por 0
						System.out.println("El zar eligio");
						int gana = Integer.parseInt(ganador);
						int nueva = (puntosJugadores.get(nombresJugadores.get(gana)));
						String ganadora = textoCartas.get(nombresJugadores.get(gana));
						nueva++;
						puntosJugadores.put(nombresJugadores.get(gana), nueva);
						for (int n = 0; n < jugadores.size(); n++) {
							if (n != zar) {
								pool.execute(new EnviarMensaje(nombresJugadores.get(gana) + "-" + ganadora + "\r\n", outputStreams.get(n), sincronizador_4));
							}
						}
						sincronizador_4.await();
						sincronizador_4.reset();
//					RECUENTO DE PUNTOS
						System.out.println(puntosJugadores.values());
						List<String> puntuaciones = new ArrayList<String>();
						for(int o=0; o<jugadores.size(); o++) {
							puntuaciones.add("El jugador/a " + nombresJugadores.get(o) + " tiene " + puntosJugadores.get(nombresJugadores.get(o)) + " punto(s)" +"\r\n");
						}
						for (int p = 0; p < jugadores.size(); p++) {
							pool.execute(new EnviarPuntuacion(puntuaciones, outputStreams.get(p), sincronizador_5));
						}
						sincronizador_5.await();
						sincronizador_5.reset();
//					ENVIAR UNA CARTA BLANCA A TODOS LOS JUGADORES MENOS AL ZAR
						for (int q = 0; q < jugadores.size(); q++) {
							if (q != zar) {
								pool.execute(new EnviarCartas(barajaBlancas.sacarCarta(), outputStreams.get(q), sincronizador_4));
							}
						}
						sincronizador_4.await();
						sincronizador_4.reset();
						System.out.println("ESTADO DEL JUEGO 1");
						terminado = juegoTerminado(puntosJugadores.values());
						System.out.println("ESTADO DEL JUEGO");
						//ENVIAR ESTADO DEL JUEGO
						if (!terminado) {
							System.out.println("NO TERMINADO");
							for (int r = 0; r < jugadores.size(); r++) {
								pool.execute(new EnviarMensaje("Ronda " + (turno+1) + " terminada \r\n", outputStreams.get(r), sincronizador_5));
							}
							sincronizador_5.await();
							sincronizador_5.reset();
							System.out.println("Siguiente ronda");
						} else {
							for (int s = 0; s < jugadores.size(); s++) {
								pool.execute(new EnviarMensaje("FIN \r\n", outputStreams.get(s), sincronizador_5));
							}
							sincronizador_5.await();
							sincronizador_5.reset();
						}
						turno++;
					} while (!terminado && turno < 10);
					for (int q = 0; q < jugadores.size(); q++) {
						pool.execute(new EnviarMensaje("LA PERSONA QUE HA GANADO ES " + getKeyByValue(puntosJugadores) + "\r\n",
								outputStreams.get(q), sincronizador_5));
					}
					sincronizador_5.await();
					sincronizador_5.reset();
			} 
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean juegoTerminado(Collection<Integer> mensajesCartas) {
		for (Integer i : mensajesCartas) {
			if (i == 4) {
				return true;
			}
		}
		return false;
	}

	public static String getKeyByValue(Map<String, Integer> mapa) {
		for (String s : mapa.keySet()) {
			if (mapa.get(s) == 4) {
				return s;
			}
		}
		return null;
	}
	
	public static List<List<Carta>> crearManos(int tamano, Baraja b){
		List<List<Carta>> cartones = new ArrayList<List<Carta>>();
		for(int i=0; i<tamano; i++) {
			List<Carta> mano = new ArrayList<Carta>();
			for(int j=0; j<(24/tamano); j++) {
				mano.add(b.sacarCarta());
			}
			cartones.add(mano);
		}
		return cartones;
	}
}
