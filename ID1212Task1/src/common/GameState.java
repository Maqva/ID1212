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
public class GameState implements Serializable{
    private int guesses;
    private int score;
    private int lostWonOngoing;
    private char[] currentLetters;
    
    /**
     *
     * @param g The amount of guesses remaining.
     * @param s The current score.
     * @param lW the current win state of the game since update
     * 1 indicates the user guessed right and got a point
     * 0 indicates the game's ongoing
     * -1 means the user lost and is now trying a new game
     * @param l a char array representing the letters correctly guessed by the user.
     */
    public GameState(int g, int s, int lW, char[] l){
        guesses = g;
        score = s;
        lostWonOngoing = lW;
        currentLetters = l;
    }
    
    public char[] getLetters(){
        return currentLetters;
    }
    
    public int getScore(){
        return score;
    }
    
    public int getWinLose() {
        return lostWonOngoing;
    }
    
    public int getGuesses(){
        return guesses;
    }
}
