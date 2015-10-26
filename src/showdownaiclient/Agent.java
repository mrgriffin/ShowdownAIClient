/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public abstract class Agent {
    String roomname;
    PSDAISocket sock;
    public Agent(String r, PSDAISocket socks){
        roomname = r;
        sock = socks;
    }
    public abstract String decide(ArrayList<String> choices);
    public abstract void updateWorldState(String input);
}
