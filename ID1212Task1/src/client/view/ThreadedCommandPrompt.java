package client.view;

import client.net.ClientInputHandler;
import common.GameState;
import common.Message;
import java.util.Scanner;

/**
 * A thread safe Command Prompt for the client to use when running the application
 * allows the user to connect to- and play games from the server using predetermined
 * commands.
 * Has to be thread safe to allow the client to terminate without having to wait on a confirmation from the server.
 * 
 * @author Magnus Qvarnström
 */
public class ThreadedCommandPrompt implements Runnable{
    private final String WELCOME_MESSAGE = "Welcome to the Hangman game!\n\nUSAGE: ";
    private boolean running = true;
    private final ThreadSafeSystemOutput printr = new ThreadSafeSystemOutput();
    private final Scanner userInput = new Scanner(System.in);

     public void startUp() {
        if (running) {
            return;
        }
        running = true;
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        printr.println(WELCOME_MESSAGE);
        while(running){
            String inputLine = userInput.nextLine();
            inputLine = inputLine.toLowerCase();
            String[] commands = inputLine.split(" ");
            //check for "guess 'word'" , "start game" , "connect IP Port"
            if (commands.length >= 2){
                //guess
                //start game
                //connect
            }
            else if (commands.length == 1){
                //"Disconnect"
            }
            else{
                //wrong command
            }
        }
    }
    
    private class ClientSideObserver extends ClientInputHandler{
        
        @Override
        public void handleInput(Message recieved){
            switch (recieved.getType()){
                case GAME_STATE:
                    GameState game = (GameState) recieved.getBody();
                    printr.println(generateGameStage(game));
                    break;
                case ERROR:
                    printr.println("Error from server: "+(String) recieved.getBody());
                    break;
                default:
                    
            }
        }
        
        private String generateGameStage(GameState gameStatus){
            int status = gameStatus.getWinLose();
            //If the game is ongoing
            if (status == 0){
                
            }
            //If the game was lost
            else if(status < 0){
            
            }
            //If the game is won
            else{
                
            }
            return "";
        }
        
    }
    
}
