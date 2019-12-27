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
	Socket conexion;
	BufferedWriter bw;
	BufferedReader br;
	CyclicBarrier sincronizador;
	
	public AceptarPeticion(Socket s, CyclicBarrier cb) {
		this.conexion=s;
		this.sincronizador=cb;
	}
	
	public void run() {
		try {
			this.bw = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(this.conexion.getOutputStream())));
			this.br = new BufferedReader(new InputStreamReader(new DataInputStream(this.conexion.getInputStream())));
			String ultimo = this.br.readLine();
			if(ultimo.equalsIgnoreCase("ultimo")) {
				this.ultimo=true;
			}
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
}
