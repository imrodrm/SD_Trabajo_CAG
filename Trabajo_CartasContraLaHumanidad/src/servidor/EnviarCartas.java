package servidor;

import java.io.BufferedWriter;
import java.io.IOException;

import cartas.Carta;

public class EnviarCartas implements Runnable {

	private BufferedWriter bw;
	private Carta enviar;
	
	public EnviarCartas(Carta c, BufferedWriter b) {
		this.enviar=c;
		this.bw=b;
	}
	
	
	@Override
	public void run(){
		try {
			this.bw.write(this.enviar.getTexto());
			this.bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
