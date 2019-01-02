/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import common.GameState;

/**
 *
 * @author Magnus
 */
public class HangmanGame{
    private int guesses;
    private int score;
    private int lostWon;
    private String word;
    private char[] correct;
    
    public HangmanGame(){
        score = 0;
        lostWon = 2;
    }
    
    public GameState updateState(){
        GameState state = new GameState(guesses, score, lostWon, correct);
        return state; 
    }
    
    /**
     * 
     * @param input the guess made by the client in the form of a String
     * @return 1 if the guess was registered in a ongoing game, 0 if no game is currently running.
     */
    public int makeGuess(String input){
        //Doesn't do anything if the current game is won or lost
        if(lostWon != 0)
            return 0;
        //If the client guessed a full word
        if (input.length() > 1){
            //If the guessed word is corrrect fill in the entire word and increase score
            if(input.equalsIgnoreCase(word)){
                win();
                for(int i = 0; i < word.length(); i++)
                    correct[i] = word.charAt(i);
            }
            //If the guessed word is Incorrect
            else{
                guesses --;
            }
        }
        //If the client guessed a single letter
        else{
            char letter = input.charAt(0);
            boolean guessedWrong = true;
            for(int i = 0; i < word.length();i ++){
                //If client guessed the Right letter
                if(word.charAt(i)==letter){
                    correct[i] = letter;
                    guessedWrong = false;
                }
            }
            //If client guessed the Wrong letter
            if(guessedWrong)
                guesses --;
            //If Client guessed correct: check to see if word is complete
            else{
                boolean complete = true;
                for(char c : correct){
                    if(c == 0) complete = false;
                    break;
                }
                //If word is complete: set win state and increase score
                if (complete)
                    win();
                    
            }
        }
        //Check for game over
        if(guesses == 0){
            lostWon = -1;
            guesses --;
            word = "";
            score --;
        }
        return 1;
    }
    
    private void win(){
        guesses = -1;
        lostWon = 1;
        word = "";
        score ++;
    }
    
    public void newGame(String input){
        word = input;
        guesses = word.length();
        correct = new char[guesses];
        lostWon = 0;
    }
    
}
