package chat.client;

/**
 * Has two threads, 
 * one to listen for incoming messages from the server 
 * and one to send messages to the server.
 * @author Magnus Qvarnström
 */
public class ChatClient {
	private final int DEFAULT_CONNECTING_PORT = 5000;
	private int port;
	
	public ChatClient(int serverPort) {
		port = serverPort;
	}
	public ChatClient() {
		port = DEFAULT_CONNECTING_PORT;
	}
	public static void main (String[] args) {
		/**
		 * Asks for the address and port to connect the client to.
		 */
	}
}
