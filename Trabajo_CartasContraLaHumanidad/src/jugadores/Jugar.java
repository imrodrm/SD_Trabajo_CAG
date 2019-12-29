package jugadores;

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
			System.out.println("Eres el ultimo jugador? S/N");
			String ultimo= sc.nextLine();
			System.out.println("Cual es tu nombre?");
			String nombre = sc.nextLine();
			String estadoJuego="";
			if(ultimo.equalsIgnoreCase("s")) {
				bw.write("ultimo-" + nombre);
			}
			else {
				bw.write("no-" + nombre);
			}
			int numJugadores = br.read();
			List<Carta> mano = new ArrayList<Carta>();
			String leerCarta;
			Carta c;
			for(int i=0; i<10; i++) {
				leerCarta = br.readLine();
				c = new Carta(leerCarta, Color.BLANCA);
				mano.add(c);
			}
			Jugador jugador = new Jugador(mano, nombre);
			do { 
				if(br.readLine().equals("ZAR")) {
					jugador.setZar(true);
				}
				else {
					jugador.setZar(false);
				}
				System.out.println("La carta NEGRA de esta ronda es " + br.readLine());
				if(!jugador.isZar()) {
					jugador.muestraMano();
					System.out.println("Introduce el numero de la carta a jugar");
					int a = sc.nextInt();
					Carta enviar = jugador.jugarCarta(a);
					bw.write(jugador.getNombre() + "-" + enviar.getTexto());
					System.out.println("El zar esta eligiendo la carta ganadora...");
					//Recibir la carta ganadora
					String ganadoraRonda = br.readLine();
					String[] nombreYCartaGanadora = ganadoraRonda.split("-");
					System.out.println("La persona que ha ganado la ronda ha sido " + nombreYCartaGanadora[0] + ", y su carta ha sido " + nombreYCartaGanadora[1]);
				}
				else {
					int jug = br.read();
					List<String> cartasBlancasJugadas = new ArrayList<String>();
					String cartaJugada;
					System.out.println();
					for(int j=0; j<jug; j++) {
						cartaJugada=br.readLine();
						System.out.println(j + ". " + cartaJugada);
						cartasBlancasJugadas.add(cartaJugada);
					}
					System.out.println("Por favor, elija el numero asociado a la carta ganadora");
					int ganadora = sc.nextInt();
					bw.write(cartasBlancasJugadas.get(ganadora));
				}
				System.out.println("El recuento de puntos es:");
				for(int k=0; k<numJugadores; k++) {
					System.out.println(br.readLine());
				}
				Carta robar = new Carta(br.readLine(), Color.BLANCA);
				mano.add(robar);
				estadoJuego = br.readLine();
				System.out.println(estadoJuego);
			}while(estadoJuego.equals("Ronda"));
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
