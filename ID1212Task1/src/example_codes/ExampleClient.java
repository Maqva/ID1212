package example_codes;
import java.io.*;
import java.net.Socket;

public class ExampleClient {
	private static final String SYSTEM_RDY_TO_SEND_MSSG = "Enter text to send: ";
	
	public static void main(String[] args) throws Exception{
		Socket s = new Socket("localhost",1234);
		PrintStream out = new PrintStream(s.getOutputStream());
		BufferedReader indata = new BufferedReader(new InputStreamReader(System.in));
		String text;
		System.out.println(SYSTEM_RDY_TO_SEND_MSSG);
		while((text=indata.readLine()) != null) {
			out.println(text);
			System.out.println(SYSTEM_RDY_TO_SEND_MSSG);
		}
		s.shutdownInput();
	}
}
