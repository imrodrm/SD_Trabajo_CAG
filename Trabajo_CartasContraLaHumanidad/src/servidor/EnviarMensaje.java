package servidor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class EnviarMensaje implements Runnable {

	private String mensaje;
	private BufferedWriter bw;
	private CyclicBarrier sincronizador;
	
	public EnviarMensaje(String msg, BufferedWriter b, CyclicBarrier cb) {
		this.bw=b;
		this.mensaje = msg;
		this.sincronizador=cb;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.bw.write(this.mensaje);
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
