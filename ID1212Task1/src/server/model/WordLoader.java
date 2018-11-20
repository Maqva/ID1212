/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Magnus
 */
public class WordLoader {
    private String path;
    
    public WordLoader(String input){
        path = input;
    }
    
    public String getWord(){
        String word = "";
        ArrayList<String> wordList = generateList();
        int length = wordList.size();
        if (length > 0){
            int random = ThreadLocalRandom.current().nextInt(0, length);
            word = wordList.get(random);
        }
        return word;
    }
    
    private ArrayList<String> generateList(){
        BufferedReader br = new BufferedReader(new InputStreamReader(loadFile()));
        ArrayList<String> words = new ArrayList();
        String temp;
        try {
            temp = br.readLine();
            while(temp != null){
                words.add(temp);
                temp = br.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(WordLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return words;
    }
    
    private FileInputStream loadFile(){
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
           System.err.println("Word-file not found.");
        }
        return fs;
    }
    
}
