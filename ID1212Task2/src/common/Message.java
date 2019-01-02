/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;

/**
 *
 * @author Magnus
 */
public class Message implements Serializable{
    private Serializable body;
    private MessageType type;
    
    public Message (MessageType msgt, Serializable b){
        type = msgt;
        body = b;
    }
    
    public MessageType getType(){
        return type;
    }
    
    public Serializable getBody(){
        return body;
    }
}
