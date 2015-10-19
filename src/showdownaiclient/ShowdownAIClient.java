/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package showdownaiclient;

import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.util.Scanner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

//


/**
 *
 * @author Scott
 */
public class ShowdownAIClient {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        PSDAISocket sock = null;
        try{
            URI uri = new URI("ws://localhost:8000/showdown/websocket");
            sock = new PSDAISocket(uri);
            sock.connect();
            while(true){
                Scanner in = new Scanner(System.in);
                String s = in.nextLine();
                sock.send(s);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
}
