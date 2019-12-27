package servidor;

import java.io.BufferedReader;
import java.io.IOException;

public class RecibirCartas implements Runnable{

	private BufferedReader br;
	private String textoCarta;
	
	public RecibirCartas(BufferedReader b) {
		this.br=b;
	}
	
	public void run() {
		try {
			this.textoCarta=this.br.readLine();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getTextoCarta() {
		return this.textoCarta;
	}
}
