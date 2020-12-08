package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class SocketSessionHandler implements Runnable{
	private Socket clientSocket;
	private int sessionID;
	private int numberToGuess;
	private int numberOfGuesses = 0;
	
	public SocketSessionHandler(Socket s, int id) {
		clientSocket = s;
		sessionID=id;
	}

	@Override
	public void run() {
		try {
			initializeConnection();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	private void initializeConnection() throws IOException {
		BufferedReader request =
				new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    String str = request.readLine();
	    System.out.println(str);
	    StringTokenizer tokens =
		new StringTokenizer(str," ?");
	    tokens.nextToken(); // The word GET
	    String requestedDocument = tokens.nextToken();
	    while( (str = request.readLine()) != null && str.length() > 0){
	    	System.out.println(str);
	    }
	    System.out.println("Förfrågan klar. fick request: "+requestedDocument);
	    
	    PrintStream response =
		new PrintStream(clientSocket.getOutputStream());
	    response.println("HTTP/1.1 200 OK");
	    response.println("Server: Blep 0.1 Alpha");
	    if(requestedDocument.indexOf(".html") != -1)
	    	response.println("Content-Type: text/html");
	    if(requestedDocument.indexOf(".gif") != -1)
	    	response.println("Content-Type: image/gif");
	    
	    response.println("Set-Cookie: clientId="+sessionID+"; expires=Wednesday,31-Dec-20 21:00:00 GMT");

	    response.println();
        if(!"\favicon.ico".equals(requestedDocument)){
            response.println("<!DOCTYPE html>\r\n"
            		+ "<html>\r\n"
            		+ "    <head>\r\n"
            		+ "        <title>ID1212 assignment 2</title>\r\n"
            		+ "    </head>\r\n"
            		+ "    <body>\r\n"
            		+ "        <p>Your cookie .</p>\r\n"
            		+ "		    <form action=\"/action_page.php\">\r\n"
            		+ "  			<label for=\"fname\">First name:</label><br>\r\n"
            		+ "  			<input type=\"text\" id=\"fname\" name=\"fname\" value=\"\"><br>\r\n"
            		+ "  			<input type=\"submit\" value=\"Submit\">\r\n"
            		+ "			</form> "
            		+ "    </body>\r\n"
            		+ "</html>\r\n"
            		+ "");

		    System.out.println("Client connected");
		}
	}
}