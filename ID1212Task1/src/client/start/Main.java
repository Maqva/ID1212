/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.start;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Magnus
 */
public class Main {
    public static void main(String[] args){
        try {
            Socket testSock1 = new Socket("Localhost", 8080);
            Socket testSock2 = new Socket("Localhost", 8080);
            InputStream inp1 = testSock1.getInputStream();
            
            OutputStream oup2 = testSock2.getOutputStream();
            BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(oup2));
            BufferedReader br1 = new BufferedReader( new InputStreamReader(inp1));
            
            String attSkriva = "Testblep";
            bw2.write(attSkriva);
            bw2.flush();
            System.out.println("Socket 2 skrev: "+attSkriva);
            System.out.println("Socket 1 läste " + br1.readLine());
            br1.close();
            bw2.close();
            
            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
