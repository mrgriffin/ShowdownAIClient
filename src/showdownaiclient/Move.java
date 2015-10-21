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
    String category;
    double stab = 1.5;
    String type;
    String name;
    int base;
    HashMap<String, String> flags;
    int priority;
    //volatile
    boolean crit;
    boolean nullified;
    boolean disabled;
    int disint;
    //Reset to 1 or 0 at the end of every turn
    double tMultiplier = 1.0;
    double aMultiplier = 1.0;
    int pmod = 0;
    boolean infiltrate;
    boolean ignoreeva;
    boolean oozed;
    boolean bounced;
    
    
    
    public void nullify(){
        nullified = true;
    }
}
