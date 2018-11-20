/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.io.Serializable;
import server.model.HangmanGame;
import common.GameState;

/**
 *
 * @author Magnus
 */
public class GameHandler {
    private HangmanGame game;
    
    public GameHandler(){
        game = new HangmanGame();
    }
    
    public boolean makeGuess(String guess){
        return game.makeGuess(guess) > 0;
    }
    
    public Serializable getState(){
        return game.updateState();
    }

    public boolean gameIsRunning() {
        GameState state = (GameState) game.updateState();
        return state.getWinLose() == 0;
    }

    public void newGame() {
        game.newGame();
    }
}
