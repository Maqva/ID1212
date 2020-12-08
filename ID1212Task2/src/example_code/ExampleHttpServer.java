package example_code;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;


public class ExampleHttpServer {
    public static void main(String[] args) throws IOException{
		System.out.println("Creating Serversocket");
		ServerSocket ss = new ServerSocket(8080);
		while(true){
		    System.out.println("Waiting for client...");
		    Socket s = ss.accept();
		    System.out.println("Client connected");
		    BufferedReader request =
			new BufferedReader(new InputStreamReader(s.getInputStream()));
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
		    s.shutdownInput();
		    
		    PrintStream response =
			new PrintStream(s.getOutputStream());
		    response.println("HTTP/1.1 200 OK");
		    response.println("Server: Blep 0.1 Alpha");
		    if(requestedDocument.indexOf(".html") != -1)
		    	response.println("Content-Type: text/html");
		    
		    if(requestedDocument.indexOf(".gif") != -1)
		    	response.println("Content-Type: image/gif");
		    
		    response.println("Set-Cookie: clientId=1; expires=Wednesday,31-Dec-20 21:00:00 GMT");
	
		    response.println();
	        if(!"\favicon.ico".equals(requestedDocument)){
	            response.println("<!DOCTYPE html>\r\n"
	            		+ "<html>\r\n"
	            		+ "    <head>\r\n"
	            		+ "        <title>ID1212 assignment 2</title>\r\n"
	            		+ "    </head>\r\n"
	            		+ "    <body>\r\n"
	            		+ "        <p>This is an example of a simple HTML page with one paragraph.</p>\r\n"
	            		+ "		    <form action=\"/action_page.php\">\r\n"
	            		+ "  			<label for=\"fname\">First name:</label><br>\r\n"
	            		+ "  			<input type=\"text\" id=\"fname\" name=\"fname\" value=\"John\"><br>\r\n"
	            		+ "  			<input type=\"submit\" value=\"Submit\">\r\n"
	            		+ "			</form> "
	            		+ "    </body>\r\n"
	            		+ "</html>\r\n"
	            		+ "");
	            s.shutdownOutput();
	            s.close();
	        }
		}
	}
}