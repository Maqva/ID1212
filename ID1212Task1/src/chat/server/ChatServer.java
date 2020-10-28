package chat.server;

import java.net.Socket;

/**
 * Has one thread to each of the clients currently connected 
 * but also one thread to listen for new incoming connections from new clients.
 * @author Magnus Qvarnström
 */
public class ChatServer {
	private final int DEFAULT_LISTENING_PORT = 5000;
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
		
	}
	
	public void runServer() {
		
	}
	
	/**
	 * receives a newly connected client and deploys a new thread for it
	 * @param client
	 */
	private void handleConnection(Socket client) {
		
	}
	
	prvate
}
