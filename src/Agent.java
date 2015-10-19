/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

/**
 *
 * @author Admin
 */
public abstract class Agent {
    String pnum;
    Pokemon[] mypokes;
    Pokemon[] opponent;
    public abstract String decide();
    public abstract void updateWorldState(String input);
}
