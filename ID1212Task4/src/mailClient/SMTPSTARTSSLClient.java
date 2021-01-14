package mailClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Base64;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SMTPSTARTSSLClient {
	private String hostAddress;
	private  String emailAddress;
	private  String ipAddress;
	private int hostPort;
	private BufferedReader rdr;
	private DataOutputStream os;
	
	public SMTPSTARTSSLClient (String address, int port, String ip, String email) {
		hostAddress = address;
		hostPort = port;
		ipAddress = ip;
		emailAddress = email;
	}
	
	public void writeTestEmail(String UserName, String passWord) {
		try {
	        	//Initialize connection and prepare to switch to TLS
				Socket socket = new Socket(hostAddress, hostPort);
				os = new DataOutputStream(socket.getOutputStream());
			    rdr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.println(readResponse());
				
				//Switch to TLS encryption
		        sendCommand("STARTTLS");
		        SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
		        SSLSocket sslsocket = (SSLSocket) sf.createSocket(socket,socket.getInetAddress().getHostAddress(),socket.getPort(),true);
		        String[] protocol = {"TLSv1.2"};
		        String[] cipher = {"TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384"};
		        sslsocket.setEnabledCipherSuites(cipher);
		        sslsocket.setEnabledProtocols(protocol);
		        sslsocket.startHandshake();
		        os = new DataOutputStream(sslsocket.getOutputStream());
			    rdr = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
			    
			    //Authenticate and log in the user
			    sendCommand("EHLO "+ipAddress);
			    sendCommand("AUTH LOGIN");
			    String message = Base64.getEncoder().encodeToString(UserName.getBytes());
			    sendCommand(message);
			    message = Base64.getEncoder().encodeToString(passWord.getBytes());
			    sendCommand(message);
			    System.out.println("Trying to send mail to "+emailAddress);
			    
			    //Send a email to the user
				message = "MAIL FROM:<"+emailAddress+">";
				sendCommand(message);
				message = "RCPT TO:<"+emailAddress+">";
				sendCommand(message);
			    sendCommand("DATA");
			    message="Subject:Test message from JAVA\n\nDefenitely not a real person\n here's a Test!\n.";
			    sendCommand(message);
			    
			    //Log Out
			    sendCommand("QUIT");
	            os.close();
	            rdr.close();
	            socket.close();
			
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} 
	        	
	}
	private void sendCommand(String command) throws IOException {
		os.writeBytes(command+"\r\n");
		System.out.println(readResponse());
	}
	private String readResponse() throws IOException {
		String output = "";
		String str;
		while((str = rdr.readLine())!=null) {
			output += str;
		//SMTP messages indicate last line of a response by not including a "-" after the control code
			if(str.charAt(3)=='-')
				output += "\n";
			else break;
		}
		return output;
	}
}
