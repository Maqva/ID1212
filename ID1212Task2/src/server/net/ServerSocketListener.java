package server.net;

import java.nio.channels.SelectionKey;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Magnus
 */
public class ServerSocketListener {

    public String getNewWord() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void closeConnection(SelectionKey key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    /**
     * Basic loop to handle each selector-ready key
     * 
     * Accept() retrieve a approaching client and make a associated key
     * 
     * read makes a clientchannel retrieve and handle a clients message
     * 
     * write makes a clientchannel send the current GameState
     * /
    
    /**
     * Accept
     * 
     * Generate key with attached Client object?
     */
    
    /**
     * LoadWord
     * 
     * loads a word from file.
     */
    
    /**
     * Client
     * 
     * Object saved in each key representing one client, each holds its own GameState (controller isn't necessary here)
     */
    
    /**
     * guessingTime
     */
    
    /**
     * sendUpdate
     */
}
