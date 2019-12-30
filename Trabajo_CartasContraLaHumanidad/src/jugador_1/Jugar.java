package jugador_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cartas.Carta;
import cartas.Color;

public class Jugar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try(Socket s = new Socket("localhost", 5555)){
			Scanner sc = new Scanner(System.in);
			BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(s.getInputStream())));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(s.getOutputStream())));
			System.out.println("Cual es tu nombre?");
			String nombre = sc.nextLine();
			System.out.println("Eres el ultimo jugador? S/N");
			String ultimo= sc.nextLine();
			String estadoJuego="";
			if(ultimo.equalsIgnoreCase("s")) {
				bw.write("ultimo-" + nombre + "\r\n");
				bw.flush();
			}
			else {
				bw.write("no-" + nombre + "\r\n");
				bw.flush();
			}
			int numJugadores = Integer.parseInt(br.readLine());
			System.out.println("El numero de jugadores es: " + numJugadores);
			List<Carta> mano = new ArrayList<Carta>();
			String leerCarta;
			Carta c;
			for(int i=0; i<(24/numJugadores); i++) {
				leerCarta = br.readLine();
				c = new Carta(leerCarta, Color.BLANCA);
				mano.add(c);
			}
			Jugador jugador = new Jugador(mano, nombre);
			do { 
				if(br.readLine().equals("ZAR")) {
					jugador.setZar(true);
					System.out.println("Eres el ZAR de esta ronda");
				}
				else {
					jugador.setZar(false);
				}
				System.out.println("La carta NEGRA de esta ronda es: \r\n" + br.readLine());
				if(!jugador.isZar()) {
					jugador.muestraMano();
					System.out.println("Introduce el numero de la carta a jugar");
					int a = sc.nextInt();
					Carta enviar = jugador.jugarCarta(a);
					bw.write(jugador.getNombre() + "-" + enviar.getTexto() + "\r\n");
					bw.flush();
					System.out.println("El zar esta eligiendo la carta ganadora...");
					//Recibir la carta ganadora
					String ganadoraRonda = br.readLine();
					String[] nombreYCartaGanadora = ganadoraRonda.split("-");
					System.out.println("La persona que ha ganado la ronda ha sido " + nombreYCartaGanadora[0] + ", y su carta ha sido " + nombreYCartaGanadora[1]);
				}
				else {
					List<String> cartasBlancasJugadas = new ArrayList<String>();
					String cartaJugada;
					
					for(int j=0; j<numJugadores; j++) {
						cartaJugada=br.readLine();
						System.out.println(j + ". " + cartaJugada);
						cartasBlancasJugadas.add(cartaJugada);
					}
					System.out.println("Por favor, elija el numero asociado a la carta ganadora");
					int ganadora = sc.nextInt();
					bw.write(Integer.toString(ganadora)+ "\r\n");
					bw.flush();
				}
				
				System.out.println("El recuento de puntos es:");
				for(int k=0; k<numJugadores; k++) {
					System.out.println(br.readLine());
				}
				
				if(!jugador.isZar()) {
					leerCarta = br.readLine();
					System.out.println("La carta que has robado es: " + leerCarta);
					c = new Carta(leerCarta, Color.BLANCA);
					mano.add(c);
				}
				estadoJuego = br.readLine();
				System.out.println(estadoJuego);
			}while(estadoJuego.startsWith("Ronda"));
			System.out.println(br.readLine());
			sc.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
