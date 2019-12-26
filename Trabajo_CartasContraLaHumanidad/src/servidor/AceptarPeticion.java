package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

public class AceptarPeticion implements Runnable{

	private boolean ultimo;
	Socket conexion;
	DataOutputStream dos;
	DataInputStream dis;
	CyclicBarrier sincronizador;
	
	public AceptarPeticion(Socket s, CyclicBarrier cb) {
		this.conexion=s;
		this.sincronizador=cb;
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
		try {
			this.dis = new DataInputStream(this.conexion.getInputStream());
			this.dos = new DataOutputStream(this.conexion.getOutputStream());
			String ultimo = this.dis.readLine();
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
	
	public DataOutputStream getDOS() {
		return this.dos;
	}
	
	public DataInputStream getDIS() {
		return this.dis;
	}
}
