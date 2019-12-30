package servidor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import cartas.Carta;

public class EnviarMano implements Runnable{

	private BufferedWriter bw;
	private List<Carta> enviar;
	private CyclicBarrier sincronizador;
	
	public EnviarMano(List<Carta> lc, BufferedWriter b, CyclicBarrier cb) {
		this.enviar=lc;
		this.bw=b;
		this.sincronizador=cb;
	}
	
	
	@Override
	public void run(){
		try {
			for(Carta c : this.enviar) {
				this.bw.write(c.getTexto() + "\r\n");
				this.bw.flush();
			}
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
