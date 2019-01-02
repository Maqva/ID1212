/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

import common.Message;
import common.MessageException;
import common.MessageType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.model.HangmanGame;

/**
 *
 * @author Magnus
 */
public class ServerClientSession {
    private ServerSocketListener server;
    private HangmanGame game;
    private SocketChannel clientChannel;
    private final ByteBuffer msgFromClient = ByteBuffer.allocateDirect(8192);
    private final ByteBuffer outputBuffer = ByteBuffer.allocateDirect(8192);
    
    ServerClientSession (ServerSocketListener server, SocketChannel channel){
        clientChannel = channel;
        this.server = server;
        game = new HangmanGame();
    }
    
    /**
     * HandleMessage
     * 
     * read the user's input: Disconnect, Guess, New Game
     */
    public void handleInput(SelectionKey key) throws IOException, ClassNotFoundException{
        byte[] input = getClientInput();
            Message messageInput = unserializeMessageObject(input);
            switch (messageInput.getType()){
                case GUESS:
                    String guess = (String) messageInput.getBody();
                    int state = game.makeGuess(guess);
                    if (state > 0)
                        makeResponseMessage(MessageType.GAME_STATE, "");
                    else
                        makeResponseMessage(MessageType.ERROR, "No active game.");
                    break;
                case NEW_GAME:
                    asyncSetWord();
                    break;
                case DISCONNECT:
                    disconnect();
                    server.closeConnection(key);
                    break;
                default:
                        makeResponseMessage(MessageType.ERROR, "Unreadable Message.");
            }
    }
    
    /**
     * 
     */
    private void asyncSetWord(){
        CompletableFuture.supplyAsync(() -> {         
            return server.getNewWord();
        }).thenAccept( word -> newWord(word));
    }
    
    private void newWord(String word){
        synchronized (game){
            game.newGame(word);
        }
        makeResponseMessage(MessageType.GAME_STATE, "");
    }
    /**
     * 
     * @return
     * @throws IOException 
     */
    private byte[] getClientInput() throws IOException{
        msgFromClient.clear();
        int numOfReadBytes;
        numOfReadBytes = clientChannel.read(msgFromClient);
        if (numOfReadBytes == -1)
            throw new IOException("Client has closed connection.");
        msgFromClient.flip();
        byte[] bytes = new byte[msgFromClient.remaining()];
        msgFromClient.get(bytes);
        return bytes;
    }
    
    private void sendServerOutput() throws IOException{
        clientChannel.write(outputBuffer);
        if (outputBuffer.hasRemaining()) {
            throw new MessageException("Could not send message");
        }
    }
    
    /**
    * 
    * @param input
    * @return
    * @throws IOException
    * @throws ClassNotFoundException 
    */
    private Message unserializeMessageObject(byte[] input) throws IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(input));
        Message mssgInput = (Message) ois.readObject();
        ois.close();
        return mssgInput;
    }
    
    /**
     * 
     * @param output
     * @return
     * @throws IOException 
     */
    private byte[] serializeMessageObject(Message output) throws IOException{
        byte[] written;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(output);
            oos.flush();
            written = baos.toByteArray();
        }
        return written;
    }

    private void makeResponseMessage(MessageType messageType, String content) {
        Message output;
        if(messageType == MessageType.GAME_STATE){
            synchronized(game){
                output = new Message(MessageType.GAME_STATE, game.updateState());
            }
        }
        else{
            output = new Message(MessageType.ERROR, content);
        }   
        try {
            outputBuffer.put(serializeMessageObject(output));
        } catch (IOException ex) {
            System.err.println("Error when processing server-response.");
        }
    }

    private void disconnect() {
        try {
            clientChannel.close();
        } catch (IOException ex) {
            System.err.println("Couldn't close a socket channel");
        }
    }
    
    
}