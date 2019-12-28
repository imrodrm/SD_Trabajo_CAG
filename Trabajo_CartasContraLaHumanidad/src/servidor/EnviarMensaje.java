package servidor;

import java.io.BufferedWriter;
import java.io.IOException;

public class EnviarMensaje implements Runnable {

	private String mensaje;
	private BufferedWriter bw;
	
	public EnviarMensaje(String msg, BufferedWriter b) {
		this.bw=b;
		this.mensaje = msg;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.bw.write(this.mensaje);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
