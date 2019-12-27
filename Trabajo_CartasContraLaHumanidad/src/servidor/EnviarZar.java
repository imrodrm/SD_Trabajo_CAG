package servidor;

import java.io.BufferedWriter;
import java.io.IOException;

public class EnviarZar implements Runnable {

	private BufferedWriter bw;
	
	public EnviarZar(BufferedWriter b) {
		this.bw=b;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.bw.write("ZAR");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
