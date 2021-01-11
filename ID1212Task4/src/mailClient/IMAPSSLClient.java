package mailClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class IMAPSSLClient {
	private int messageIndex = 1;
	private String hostAddress;
	private int hostPort;
	
	public IMAPSSLClient(String address, int port){
		hostPort = port;
		hostAddress = address;
	}
	
	public void readInbox(String userName, String userPass) {
        try{
        	SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
	        String[] cipher = {"TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384"};
	        SSLSocket socket = (SSLSocket)sf.createSocket(hostAddress,hostPort);
	    	socket.setEnabledCipherSuites(cipher);
	    	PrintWriter writer = new PrintWriter(socket.getOutputStream());
	        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        socket.startHandshake();
            System.out.println(reader.readLine());

            //Handle login credentials
            String loginCommand = "LOGIN "+userName+" "+userPass;
            //add additional commands to inspect inbox, read one full email and log out
            String[] commands = {loginCommand, "select inbox", "fetch 1 full", "logout"};
            //itterate through commands
            for(String command : commands) {
            	sendIMAPCommand(command, writer);
                System.out.println(readIMAPResponse(reader));
            }
            
            //Close connection after logout
            writer.close();
            reader.close();
            socket.close();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
	}
	
	private void sendIMAPCommand(String command, PrintWriter writer) {
		String toSend = "A"+messageIndex+" "+command+"\r\n";
		messageIndex ++;
		writer.write(toSend);
		writer.flush();
	}
	
	private String readIMAPResponse(BufferedReader reader) throws IOException {
		String output = "";
		String str;
		while( (str=reader.readLine()) != null) {
            output += str;
            if(str.charAt(0)=='A')
            	return output;
            else
            	output += "\n";
		}
		return "";
	}
}
