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
public class ManualAgent extends Agent {
    public ManualAgent(String room, PSDAISocket socks){
        super(room, socks);
    }
    
    @Override
    public String decide(ArrayList<String> choices) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateWorldState(String input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
