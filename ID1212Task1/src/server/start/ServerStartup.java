/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.start;

import server.net.ServerSocketListener;

/**
 *
 * @author Magnus
 */
public class ServerStartup {
    public static void main(String[] args){
        ServerSocketListener server = new ServerSocketListener();
        server.listen();
    }
}
