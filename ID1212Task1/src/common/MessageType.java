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
public enum MessageType{
    //Message denoting that the client is disconnecting and the server should terminate
    DISCONNECT, 
    //the updated state of the game sent to the client after they've made a guess.
    GAME_STATE,
    //A guess being made by the user to a ongoing game of hangman
    GUESS,
    //A request from the User to start a new game
    NEW_GAME,
    //A error message to be displayed on the client's console
    ERROR
}