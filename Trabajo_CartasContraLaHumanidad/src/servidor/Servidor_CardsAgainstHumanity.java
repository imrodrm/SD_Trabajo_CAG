package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
		try (ServerSocket svs = new ServerSocket(5555)) {
			while (true) {
//				CAMBIAR VALOR CAMBIAR VALOR CAMBIAR VALOR CAMBIAR VALOR CAMBIAR VALOR 
				ExecutorService pool = Executors.newFixedThreadPool(2);
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
				final CyclicBarrier sincronizador = new CyclicBarrier(2);
				boolean noMasJugadores = false; // Para que si un jugador me dice que es el ultimo, salir del bucle
				boolean terminado = false;
//				PARTE DE "RECOLECTAR" JUGADORES, HASTA UN MÁXIMO DE 4
				try (Socket cliente = svs.accept()) {
					while (jugadores.size() <= 4 && !noMasJugadores) {
//						Se que debería hacerlo con hilos, pero si tengo que esperar en el principal para
//						añadir los jugadores, Readers, Writers, etc en el mismo orden, creo que no crear los hilos
//						ahorrará memoria.
						jugadores.add(cliente);
						BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(cliente.getInputStream())));
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(cliente.getOutputStream())));
						inputStreams.add(br);
						outputStreams.add(bw);
						String mensajeBienvenida = br.readLine();
						String[] mensaje = mensajeBienvenida.split("-");
						puntosJugadores.put(mensaje[1], 0);
						nombresJugadores.add(mensaje[1]);
						if(mensaje[0].equals("ultimo")) {
							noMasJugadores=true;
						}
					}
					System.out.println("Ultimo jugador");
//					PREPARANDO EL JUEGO...
					for(int j=0; j<jugadores.size(); j++) {
						pool.execute(new EnviarMensaje(jugadores.size() + "\r\n", outputStreams.get(j), sincronizador));
					}
					sincronizador.await();
					sincronizador.reset();
					List<Carta> cartasNegras = Collections.synchronizedList(CrearCartas.crearNegras());
					List<Carta> cartasBlancas = Collections.synchronizedList(CrearCartas.crearBlancas());
					Baraja barajaNegras = new Baraja(Color.NEGRA, cartasNegras);
					Baraja barajaBlancas = new Baraja(Color.BLANCA, cartasBlancas);
					barajaNegras.barajar();
					barajaBlancas.barajar();
					Carta enviar;
					//CAMBIAR VALOR CAMBIAR VALOR CAMBIAR VALOR CAMBIAR VALOR CAMBIAR VALOR
					for (int j = 0; j < 4; j++) {
						//Repartir cartas carta
						pool.execute(new EnviarCartas(barajaBlancas.sacarCarta(), outputStreams.get(0), sincronizador));
					}
					//HACER EL AWAIT
					System.out.println("Todas las cartas enviadas");
					sincronizador.await();
					sincronizador.reset();
					int turno = 0;
					do {
//					DESIGNO QUIÉN ES EL ZAR
						int zar = turno % 4;
						for (int z = 0; z < jugadores.size(); z++) {
							if (z != zar) {
								pool.execute(new EnviarMensaje("NoZAR\r\n", outputStreams.get(z), sincronizador));
							} else {
								pool.execute(new EnviarMensaje("nZAR\r\n", outputStreams.get(z), sincronizador));
							}
						}
//					LES MANDO A TODOS LOS JUGADORES LA CARTA NEGRA
						enviar = barajaNegras.sacarCarta();
						for (int l = 0; l < jugadores.size(); l++) {
							pool.execute(new EnviarCartas(enviar, outputStreams.get(l), sincronizador));
						}
//					RECIBIR LA CARTA BLANCA DE LOS JUGADORES (menos el Zar)
						Map<String, String> textoCartas = new HashMap<String, String>();
						for (int m = 0; m < jugadores.size(); m++) {
							if (m != zar) {
								RecibirCartas rb = new RecibirCartas(inputStreams.get(m));
								pool.execute(rb);
								String[] a = rb.getTextoCarta().split("-");
								textoCartas.put(a[0], a[1]); // a[0] nombreJugador, a[1] textoCarta
							}
						}
//					MANDARLE LAS CARTAS AL ZAR
						for (String s : textoCartas.values()) {
							outputStreams.get(zar).write(s);
						}
//					RECIBIR GANADOR DEL ZAR Y ENVIAR AL RESTO DE JUGADORES QUIÉN ES EL GANADOR
						String ganador = inputStreams.get(zar).readLine();
						int gana = Integer.parseInt(ganador);
						puntosJugadores.replace(nombresJugadores.get(gana),
								puntosJugadores.get(nombresJugadores.get(gana)));
						for (int n = 0; n < jugadores.size(); n++) {
							if (n != zar) {
								pool.execute(new EnviarMensaje(ganador + "\r\n", outputStreams.get(n), sincronizador));
							}
						}
//					RECUENTO DE PUNTOS
						for (int o = 0; o < jugadores.size(); o++) {
							for (int p = 0; p < jugadores.size(); p++) {
								pool.execute(new EnviarMensaje("El jugador/a " + nombresJugadores.get(p) + " tiene "
										+ puntosJugadores.get(nombresJugadores.get(p)) + "\r\n", outputStreams.get(o), sincronizador));
							}
						}
//					ENVIAR UNA CARTA BLANCA A TODOS LOS JUGADORES MENOS AL ZAR
						for (int q = 0; q < jugadores.size(); q++) {
							if (q != zar) {
								pool.execute(new EnviarCartas(barajaBlancas.sacarCarta(), outputStreams.get(q), sincronizador));
							}
						}
						terminado = juegoTerminado(puntosJugadores.values());
						if (terminado) {
							for (int r = 0; r < jugadores.size(); r++) {
								pool.execute(new EnviarMensaje("Ronda " + turno + " terminada \r\n\"", outputStreams.get(r), sincronizador));
							}
						} else {
							for (int s = 0; s < jugadores.size(); s++) {
								pool.execute(new EnviarMensaje("FIN \r\n", outputStreams.get(s), sincronizador));
							}
						}
					} while (!terminado && turno < 10);
					for (int q = 0; q < jugadores.size(); q++) {
						pool.execute(new EnviarMensaje("LA PERSONA QUE HA GANADO ES " + getKeyByValue(puntosJugadores) + "/r/n",
								outputStreams.get(q), sincronizador));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
}
