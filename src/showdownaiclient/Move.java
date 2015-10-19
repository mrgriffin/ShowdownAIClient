/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import java.util.HashMap;

/**
 *
 * @author Admin
 */
public class Move {
    double stab = 1.5;
    String type;
    String name;
    int base;
    boolean crit;
    HashMap<String, String> flags;
    boolean nullified;
    
    double tMultiplier = 1.0;
    
    public void nullify(){
        nullified = true;
    }
}
