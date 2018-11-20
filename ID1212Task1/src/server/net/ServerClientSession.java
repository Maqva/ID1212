/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

import common.GameState;
import common.Message;
import common.MessageException;
import common.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.Socket;
import server.controller.GameHandler;

/**
 *
 * @author Magnus
 */
public class ServerClientSession implements Runnable {
    private Socket clientSocket;
    private volatile boolean connected = false;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private GameHandler controller;
    
    public ServerClientSession (Socket incomingSocket) throws IOException{
        clientSocket = incomingSocket;
        controller = new GameHandler();
    }
    
    @Override
    public void run() {
        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());
        } 
        catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
        try {
            Message msg;
            connected = true;
            updateGameState();
            while(connected){
                msg = (Message) input.readObject();
                switch(msg.getType()){
                    case GUESS:
                        if(controller.makeGuess((String) msg.getBody()))
                            updateGameState();
                        else
                            sendMessage(MessageType.ERROR, "The current game is Over.");
                        break;
                    case NEW_GAME:
                        if(controller.gameIsRunning())
                            sendMessage(MessageType.ERROR, "There is already a game running.");
                        else
                            controller.newGame();
                        break;
                    case DISCONNECT:
                        disconnect();
                        break;
                    default:
                        throw new MessageException("Received corrupt message: " + msg);
                }
            }
        } 
        catch (IOException|ClassNotFoundException ex) {
            disconnect();
            throw new MessageException(ex);
        }
    }
    
    private void sendMessage(MessageType type, Serializable body){
         try {
            Message toSend = new Message(type, body);
            output.writeObject(toSend);
            output.flush();
            output.reset();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        
    }
    private void updateGameState(){
         GameState state = (GameState) controller.getState();
         sendMessage(MessageType.GAME_STATE, state);
    }
    
    private void disconnect(){
        try {
            clientSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        connected = false;
    }
}