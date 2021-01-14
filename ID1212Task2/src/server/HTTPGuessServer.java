package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.StringTokenizer;
import guess_game.GuessClass;

public class HTTPGuessServer{
	private Hashtable<Integer, GuessClass> activeGames = new Hashtable<Integer, GuessClass>();
	private int nextId = 1;
	private boolean running = true;
	private final String COOKIE_EXPIRATION_STRING = "Wednesday,20-Jan-21 21:00:00 GMT";
	private final String FAVICO_PATH = "src/favicon.ico";
	
	public static void main(String[] args) throws IOException{
		HTTPGuessServer server = new HTTPGuessServer();
		server.run(Integer.parseInt(args[0]));
		
	}
	public void run(int socket){
			try {
				ServerSocket ss = new ServerSocket(socket);
				System.out.println("Starting server...");
				while(running){
					try {
					    handleClient(ss.accept());					
					}
					catch(IOException e) {
						System.err.println("There was a communication error with a client.");
					}
		        }
				ss.close();
			}
			catch(IOException e) {
				System.err.println("The server encountered a Exception, shutting down...");
				return;
			}
	}
	public void stop() {
		running = false;
	}
	private void handleClient(Socket s) throws IOException {
		BufferedReader request =
		new BufferedReader(new InputStreamReader(s.getInputStream()));
		String str = request.readLine();
		StringTokenizer tokens = new StringTokenizer(str," ?");
		String method = tokens.nextToken();
		String requestedDocument = tokens.nextToken();
		String cookies = "";
		while( (str = request.readLine()) != null && str.length() > 0){
			if(str.contains("Cookie: "))
				cookies = str;
		}
		//Recieve POST Data
		String postContent = "";
		if(method.equals("POST")) {
			while(request.ready()){
				postContent+=((char) request.read());
			}
		}
		s.shutdownInput();
		int cookieValue = (cookies.length()>0)?(Integer.parseInt(cookies.split(" ")[1].split("=")[1].split(";")[0])):-1;
		HTMLMessage response = new HTMLMessage();
		
		//User is playing the game
		if(requestedDocument.equalsIgnoreCase("/Lab_Task_2/Guess")) {
			response.setContentType("text/html");
			
			//If no cookie, generate a new one for a new user
			if(cookieValue == -1) {
				int id = getNextId();
				response.setCookie("userId", String.valueOf(id));
				activeGames.put(id, new GuessClass());
				response.setBody(generatePlayScreen(0,0).getBytes());
			}
			
			//User has cookie and is ready to Play
			else {
				GuessClass guess = activeGames.get(cookieValue);
				if(guess == null) {
					guess = new GuessClass();
					activeGames.put(cookieValue, guess);
				}
				//A post call is made, meaning the user made a guess
				if(method.equals("POST")&&postContent.length() > 0) {
					int guessedNumber = Integer.parseInt(postContent.split("=")[1]);
					int result = guess.makeGuess(guessedNumber);
					if(result == 0)
						response.setBody(generateEndScreen(guess.getGuesses()).getBytes());
					else
						response.setBody(generatePlayScreen(guess.getGuesses(), result).getBytes());
				}
				//Get is called, meaning the player needs to restart
				else {
					guess = new GuessClass();
					activeGames.put(cookieValue, guess);
					response.setBody(generatePlayScreen(0, 0).getBytes());
				}
			}
			response.sendMessage(s.getOutputStream(), true);
		}
		else if(requestedDocument.equals("/favicon.ico")){
			response.setContentType("image/gif");
			byte[] bytes = null;
			BufferedInputStream fileInputStream = null; 
			try{
				File file = new File(FAVICO_PATH); 
				fileInputStream = new BufferedInputStream( new FileInputStream(file)); 
				//fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(this.filePath); 
				bytes = new byte[(int) file.length()]; fileInputStream.read(bytes); 
				response.setBody(bytes);
			} 
			catch(FileNotFoundException ex){ 
				System.err.println("A error occured when reading \"favicon.ico\"");
			}
			response.sendMessage(s.getOutputStream(), true);
		}
		else {
			response.setBody(generateErrorPage().getBytes());
			response.setContentType("text/html");
			response.sendMessage(s.getOutputStream(), false);
		}
		
		s.shutdownOutput();
		s.close();
	}
	private int getNextId() {
		int out;
		while(activeGames.containsKey(out = nextId)) {
			nextId ++;
		}
		nextId ++;
		return out;
	}
 	private String generatePlayScreen(int guesses, int hiLo) {
		 String output ="<!DOCTYPE html>\r\n"
			 		+ "<html>\r\n"
			 		+ "    <head><title>Number Guessing Game</title></head>\r\n"
			 		+ "    <body>\r\n";
		 if(guesses > 0) {
			 output+="Nope! guess "+((hiLo>0)?"lower":"higher")+" You have made "+guesses+" guess(es).<br>\r\n";
		 }
		 else {
			 output +="Welcome to the number guessing game!<br>\r\n"
					 +"Im thinking of a number between 1 and 100.<br>\r\n";
		 }
 		output += "        <form action=\"/Lab_Task_2/Guess\" method=\"POST\">\r\n"
		 		+ "				<label for=\"userGuess\">Enter your guess: </label>\r\n"
		 		+ "  			<input type=\"number\" step=\"1\" id=\"userGuess\" name=\"guess\">"
		 		+ "        </form>\r\n"
		 		+ "    </body>\r\n"
		 		+ "</html>";
		 return output;
	 } 
	private String generateEndScreen(int guesses) {
		 String output ="<!DOCTYPE html>\r\n"
		 		+ "<html>\r\n"
		 		+ "    <head><title>Success!</title></head>\r\n"
		 		+ "    <body>\r\n"
		 		+ "        Well done! You made it in "+guesses+" guess(es)!<br>\r\n"
		 		+ "        Press the button to play again.<br>\r\n"
		 		+ "        <form action=\"/Lab_Task_2/Guess\">\r\n"
		 		+ "			<input type=\"submit\" value=\"New Game\">\r\n"
		 		+ "        </form>\r\n"
		 		+ "    </body>\r\n"
		 		+ "</html>";
		 return output;
	 }
	private String generateErrorPage() {
		String output ="<!DOCTYPE html>\r\n"
		 		+ "<html>\r\n"
		 		+ "    <head><title>404</title></head>\r\n"
		 		+ "    <body>\r\n"
		 		+ "        404 Page could not be Found.\r\n"
		 		+ "    </body>\r\n"
		 		+ "</html>";
		return output;
	}
	private class HTMLMessage{
		private String contentType;
		private String cookieValue;
		private String cookieName;
		private byte[] body;
		
		public void sendMessage(OutputStream o, boolean pageFound) throws IOException {
			PrintStream response = new PrintStream(o);
			String responseString = "HTTP/1.1 "+((pageFound)?"200 OK":"404 Not Found")+"\r\n"
						+ "Server: Trash 0.1 Beta\r\n"
						+"\"Content-Type: "+contentType+"\r\n";
			if(pageFound && cookieName != null && cookieValue != null) {
				responseString+="Set-Cookie: "+cookieName+"="+cookieValue+"; expires="+COOKIE_EXPIRATION_STRING+"\r\n";
			}
			responseString+="\r\n";
			response.write(responseString.getBytes());
			response.write(body);
		}
		
		public void setContentType(String type) {
			contentType = type;
		}
		
		public void setCookie(String name, String value) {
			cookieName = name;
			cookieValue = value;
		}
		
		public void setBody(byte[] input) {
			body = input;
		}
	}
}