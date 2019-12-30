package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class RecibirCartas implements Runnable{

	private BufferedReader br;
	private String textoCarta;
	private CyclicBarrier sincronizador;
	
	public RecibirCartas(BufferedReader b, CyclicBarrier cb) {
		this.br=b;
		this.sincronizador=cb;
	}
	
	public void run() {
		try {
			this.textoCarta=this.br.readLine();
			this.sincronizador.await();
		}catch(IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getTextoCarta() {
		return this.textoCarta;
	}
}
