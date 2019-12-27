package servidor;

import java.io.DataOutputStream;
import java.io.IOException;

import cartas.Carta;

public class EnviarCartas implements Runnable {

	private DataOutputStream dos;
	private Carta enviar;
	
	public EnviarCartas(Carta c, DataOutputStream d) {
		this.enviar=c;
		this.dos=d;
	}
	
	
	@Override
	public void run(){
		try {
			this.dos.writeChars("Negra-" + this.enviar.getTexto());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
