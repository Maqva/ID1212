/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.net;

import common.Message;
import common.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Magnus
 */
public class ClientSocket{
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    private volatile boolean connected;
    
   public void connect(String host, int port, ClientInputHandler listener) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        connected = true;
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        new Thread(new InputListener(listener)).start();
    }
    
    public void disconnect() throws IOException {
        sendMessage(MessageType.DISCONNECT, "");
        socket.close();
        socket = null;
        connected = false;
    }
    
    private void sendMessage (MessageType type, Serializable body){
        Message toSend = new Message(type, body);
        try {
            output.writeObject(toSend);
            output.flush();
            output.reset();
        } catch (IOException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class InputListener implements Runnable{
        private final  ClientInputHandler inputHandler;        

        private InputListener(ClientInputHandler cih) {
            inputHandler = cih;
        }
        
        @Override
        public void run() {
            try {
                for (;;) {
                    inputHandler.handleInput((Message) input.readObject());
                }
            } catch (Throwable connectionFailure) {
                if (connected) {
                    inputHandler.handleInput(new Message(MessageType.ERROR, "Lost Connection"));
                }
            }
        }   
    }
}