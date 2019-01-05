/*
 * Copyright (C) 2019 Magnus Qvarnström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net;

import controller.Controller;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Magnus Qvarnström
 */
public class Node {
    boolean connected;
    private Controller controller;
    private ServerSocketChannel serverChannel;
    private final Selector selector = Selector.open();;
    
    
    public Node(Controller controller) throws IOException{
        this.controller = controller;
        initializeServerChannel();
        connected = false;
    }
    
    public void joinGame(String ipAddress){
        run();
    }
    
    private void initializeServerChannel() throws IOException{
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 5454));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }
    
    private void run(){
        while(connected){
            
        }
        
    }
    
    private void broadcast(String message){
        
    }
}
