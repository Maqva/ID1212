package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer{
	 public static void main(String[] args) throws IOException{
		int id = 1;
		System.out.println("Creating Serversocket");
		ServerSocket ss = new ServerSocket(8080);
		Thread t;
		while(true){
		    System.out.println("Waiting for client number "+id+"...");
		    Socket s = ss.accept();
		    t = new Thread(new SocketSessionHandler(s, id));
		    t.start();
		    id++;
		}
			    
	}
}