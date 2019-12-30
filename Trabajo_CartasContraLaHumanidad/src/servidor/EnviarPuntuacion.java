package servidor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class EnviarPuntuacion implements Runnable {

	private List<String> puntos;
	private BufferedWriter bw;
	private CyclicBarrier sincro;
	
	public EnviarPuntuacion(List<String> puntos, BufferedWriter b, CyclicBarrier cb) {
		this.bw=b;
		this.puntos=puntos;
		this.sincro=cb;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			for(String s : puntos) {
				this.bw.write(s);
				this.bw.flush();
			}
			this.sincro.await();
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
