/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author Magnus
 */
public class ServerSocketListener {
    private static int portNo = 8080;
    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    
    
    public ServerSocketListener(){
        
    }
    /**
     *
     */
    public void listen() {
        try {
            ServerSocket listeningSocket = new ServerSocket(portNo);
            while (true) {
                Socket clientSocket = listeningSocket.accept();
                try {
                    startHandler(clientSocket);
                    System.out.println("Started a new session.");
                }
                catch (IOException e){
                    System.err.println("Failed to open connection.");
                }
            }
        } catch (IOException e) {
            System.err.println("Server failure.");
        }
    }
    
    /**
     * Starts a new thread to communicate with a client
     * Ongoing threads have maximized priority so the program
     * prioritises maintaining current session over starting new ones.
     * 
     * @param clientSocket The Socket object from the client
     * @throws SocketException 
     */
    private void startHandler(Socket clientSocket) throws SocketException, IOException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR);
        ServerClientSession handler = new ServerClientSession(clientSocket);
        Thread handlerThread = new Thread(handler);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }
    
    
}
