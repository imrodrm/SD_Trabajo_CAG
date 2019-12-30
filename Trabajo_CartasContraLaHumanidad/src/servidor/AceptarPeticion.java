package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class AceptarPeticion extends Thread {

	private Socket socket;
	private BufferedWriter bw;
	private BufferedReader br;
	private String nombre;
	private boolean ulti;
	
	public AceptarPeticion(Socket cliente) {
		this.socket = cliente;
	}
	
	public void run() {
		try {
			this.bw = (new BufferedWriter(new OutputStreamWriter(new DataOutputStream(this.socket.getOutputStream()))));
			this.br = new BufferedReader(new InputStreamReader(new DataInputStream(this.socket.getInputStream())));
			String mensajeBienvenida = br.readLine();
			String[] mensaje = mensajeBienvenida.split("-");
			this.nombre = mensaje[1];
			if(mensaje[0].equals("ultimo")) {
				this.ulti = true;
			}
			else {
				this.ulti = false;
			}
			System.out.println("LEGO");
			System.out.println("NOLEGO");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedReader getBr() {
		return br;
	}


	public boolean getUlti() {
		return ulti;
	}

	public String getNombre() {
		return nombre;
	}

	public BufferedWriter getBw() {
		return bw;
	}

}
