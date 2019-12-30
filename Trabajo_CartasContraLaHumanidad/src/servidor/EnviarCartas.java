package servidor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import cartas.Carta;

public class EnviarCartas implements Runnable {

	private BufferedWriter bw;
	private Carta enviar;
	private CyclicBarrier sincronizador;
	
	public EnviarCartas(Carta c, BufferedWriter b, CyclicBarrier cb) {
		this.enviar=c;
		this.bw=b;
		this.sincronizador=cb;
	}
	
	
	@Override
	public void run(){
		try {
			this.bw.write(this.enviar.getTexto() + "\r\n");
			this.bw.flush();
			this.sincronizador.await();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
