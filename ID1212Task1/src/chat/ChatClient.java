package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
	private PrintWriter serverWriter;
	private Scanner inputScanner = new Scanner(System.in);
	private String hostAddress;
	private int port;
	private Thread listenerThread;
	private ServerListener listener;
	
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
				client.connectToServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void connectToServer() throws IOException {
		System.out.println("Connecting to "+hostAddress+" on port "+port);
		Socket toConnect = new Socket(hostAddress, port);
		serverWriter = new PrintWriter(toConnect.getOutputStream(), true);
		listenerThread = new Thread(listener = new ServerListener(toConnect));
		listenerThread.start();
		System.out.println("Connection succesfull! type \"exit\" to close application");
		String message;
		while (true) {
			message = inputScanner.nextLine();
			if(message.equalsIgnoreCase("exit")) {
				System.out.println("Exiting...");
				break;
			}
			else {
				serverWriter.println(message);
				serverWriter.flush();
			}
		}
		try {
			listener.close();
			listenerThread.join();
			toConnect.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class ServerListener implements Runnable{
		private BufferedReader serverReader;
		private final String FRM_SRVR_MSSG = "From Server: ";
		private boolean running = true;
		
		public ServerListener (Socket socket) throws IOException {
			serverReader = new BufferedReader(
			        new InputStreamReader(socket.getInputStream()));	
		}
		public void close() {
			running = false;
		}
		@Override
		public void run() {
			String msgFrmServer;
			while (running) {
				try {
					if(serverReader.ready()&&(msgFrmServer=serverReader.readLine())!=null) {
						System.out.println(FRM_SRVR_MSSG+msgFrmServer);
					}
				} catch (IOException e) {
					System.err.println("A I/O Error occured while communicating with the server, disconnecting...");
					break;
				}
			}
		}
		
	}
}
