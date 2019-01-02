/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Magnus
 */
    /**
 * Thrown when the expected message could not be received.
 */
public class MessageException extends RuntimeException {
    public MessageException(String msg) {
        super(msg);
    }
    
    public MessageException(Throwable rootCause) {
        super(rootCause);
    }
}