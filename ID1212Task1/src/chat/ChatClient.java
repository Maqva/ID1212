package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Has two threads, 
 * one to listen for incoming messages from the server 
 * and one to send messages to the server.
 * @author Magnus Qvarnström
 */
public class ChatClient {
	private final int DEFAULT_CONNECTING_PORT = 5000;
	private String hostAddress;
	private int port;
	
	public ChatClient(String address, int serverPort) {
		hostAddress = address;
		port = serverPort;
	}
	public ChatClient(String address) {
		hostAddress = address;
		port = DEFAULT_CONNECTING_PORT;
	}
	
	public static void main (String[] args) {
		ChatClient client = null;
		if(args.length >= 2) {
			try {
				int arg = Integer.parseInt(args[1]);
				client = new ChatClient(args[0], arg);
			}
			catch(NumberFormatException e) {
				System.out.println("\""+ args[1]+"\" is not a number, using default port...");
				client = new ChatClient(args[0]);	
			}
		}
		else if(args.length == 1){
			System.out.println("using default port...");
			client = new ChatClient(args[0]);
		}
		else {
			System.out.println("Usage:: ChatClient HOST_ADDRESS [PORT_NUMBER]");
		}
		if(client != null) {
			try {
				Socket s = client.connectToServer();
				Thread listenerThread = client.initializeListenerThread(s);
				listenerThread.start();
				client.startMessageLoop(s);
				listenerThread.interrupt();
				s.close();
			} 
			catch(ConnectException e) {
				System.out.println("Could not connect to the host, closing...");
			}
			catch (IOException e) {
				System.out.println("A Input/Output Error has occured, closing...");
			}
		}
	}
	
	private void startMessageLoop(Socket s) throws IOException {
		PrintWriter serverWriter;
		Scanner inputScanner = new Scanner(System.in);
		String message;
		System.out.println("Entered the chatroom, write message, or type \'exit\' to exit");
		serverWriter = new PrintWriter(s.getOutputStream(), true);
		while (true) {
			message = inputScanner.nextLine();
			if(message.equalsIgnoreCase("exit")) {
				System.out.println("Exiting...");
				break;
			}
			else {
				serverWriter.println(message);
			}
		}
		inputScanner.close();
	}
	
	private Thread initializeListenerThread(Socket s) throws IOException {
		BufferedReader input = new BufferedReader(
		        new InputStreamReader(s.getInputStream()));
		Thread t = new Thread(() -> 
		{
			String msgFrmServer;
			while (!Thread.interrupted()) {
				try {
					if(input.ready()&&(msgFrmServer=input.readLine())!=null) {
						System.out.println("From Server: "+msgFrmServer);
					}
				} catch (IOException e) {
					System.err.println("A I/O Error occured while communicating with the server, please restart the application...");
					break;
				}
			}
		});
		return t;
	}
	
	private Socket connectToServer() throws IOException {
		System.out.println("Connecting to "+hostAddress+" on port "+port);
		Socket toConnect = new Socket(hostAddress, port);
		System.out.println("Connection succesfull!");
		
		return toConnect;
	}
		
}
