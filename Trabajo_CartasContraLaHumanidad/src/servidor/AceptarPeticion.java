package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class AceptarPeticion extends Thread{

	private boolean ultimo;
	private Socket conexion;
	private BufferedWriter bw;
	private BufferedReader br;
	private String nombre;
	
	public AceptarPeticion(Socket s) {
		this.conexion=s;
	}
	
	public void run() {
		try {
			System.out.println("consigo outputStream");
			this.bw = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(this.conexion.getOutputStream())));
			System.out.println("consigo inputStream");
			this.br = new BufferedReader(new InputStreamReader(new DataInputStream(this.conexion.getInputStream())));
			System.out.println("leo");
			String lo = this.br.readLine();
			System.out.println("spliteo");
			String[] lol = lo.split("-");
			System.out.println(lol[0]);
			if(lol[0].equalsIgnoreCase("ultimo")) {
				this.ultimo=true;
			}
			this.nombre = lol[1];
			System.out.println("Matahteami paaadre");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getultimo() {
		return this.ultimo;
	}
	
	public BufferedWriter getBufferedWriter() {
		return this.bw;
	}
	
	public BufferedReader getBufferedReader() {
		return this.br;
	}
	
	public String getNombre() {
		return this.nombre;
	}
}
