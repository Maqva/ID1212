package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Has one thread to each of the clients currently connected 
 * but also one thread to listen for new incoming connections from new clients.
 * @author Magnus Qvarnström
 */
public class ChatServer {
	private ArrayList<ClientHandler> clientList = new ArrayList<ClientHandler>();
	private static final int DEFAULT_LISTENING_PORT = 5000;
	private int serverPort;
	
	public ChatServer(int listeningPort) {
		serverPort = listeningPort;
	}
	public ChatServer() {
		serverPort = DEFAULT_LISTENING_PORT;
	}
	
	/**
	 * 
	 * @param args the port number to start the listening server on, uses DEFAULT_LISTENING_PORT if none provided.
	 */
	public static void main(String[] args) {
		ChatServer server;
		if(args.length >= 1) {
			try {
				int arg = Integer.parseInt(args[0]);
				server = new ChatServer(arg);
			}
			catch(NumberFormatException e) {
				System.out.println("\""+ args[0]+"\" is not a number, using default port...");
				server = new ChatServer();	
			}
		}
		else {
			System.out.println("using default port...");
			server = new ChatServer();
		}
		try {
			System.out.println("Starting Server...");
			server.runServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Something went wrong when initiating server.");
			e.printStackTrace();
		}
	}
	
	public void runServer() throws IOException {
		ServerSocket server = new ServerSocket(serverPort);
		Socket clientSocket;
		System.out.println("Started server on port: "+serverPort);
		while((clientSocket = server.accept()) != null) {
			handleConnection(clientSocket);
		}
		server.close();
	}
	
	/**
	 * receives a newly connected client and deploys a new thread for it
	 * @param client
	 */
	private void handleConnection(Socket client) {
		try {
			ClientHandler handler = new ClientHandler(this, client);
			synchronized (clientList) {
				clientList.add(handler);
			}
			Thread t = new Thread(handler);
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void broadcastMessage(String message, ClientHandler sender) {
		synchronized (clientList){
			for (ClientHandler cH : clientList) {
				if(cH != sender)
					cH.addMsgToQueue(message);
			}
		}
	}
	
	private class ClientHandler implements Runnable{
		public boolean connected = true;
		private ChatServer server;
		private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();
		private BufferedReader clientInputReader;
		private PrintWriter clientOutputWriter;
		
		public ClientHandler (ChatServer server, Socket connectingClient) throws IOException {
			this.server = server;
			clientOutputWriter = new PrintWriter(connectingClient.getOutputStream(), true);
			clientInputReader = new BufferedReader(
			        new InputStreamReader(connectingClient.getInputStream()));
		}

		public void addMsgToQueue(String message) {
			messageQueue.add(message);
		}
		
		@Override
		public void run() {
			String messageToSend, readInput;
			while(connected) {
				try {
					if(clientInputReader.ready()) {	
						if((readInput = clientInputReader.readLine()) != null)
							server.broadcastMessage(readInput, this);
					}
				} catch (IOException e) {
					connected = false;
				}
				if(!messageQueue.isEmpty()) {
					while((messageToSend = messageQueue.poll())!=null)
						clientOutputWriter.println(messageToSend);
				}
			}
		}
	}		
}

