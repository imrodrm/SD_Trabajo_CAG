package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

public class AceptarPeticion implements Runnable{

	private boolean ultimo;
	private Socket conexion;
	private BufferedWriter bw;
	private BufferedReader br;
	private CyclicBarrier sincronizador;
	private String nombre;
	
	public AceptarPeticion(Socket s, CyclicBarrier cb) {
		this.conexion=s;
		this.sincronizador=cb;
	}
	
	public void run() {
		try {
			this.bw = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(this.conexion.getOutputStream())));
			this.br = new BufferedReader(new InputStreamReader(new DataInputStream(this.conexion.getInputStream())));
			String[] lol = this.br.readLine().split("-");
			if(lol[0].equalsIgnoreCase("ultimo")) {
				this.ultimo=true;
			}
			this.nombre = lol[1];
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
