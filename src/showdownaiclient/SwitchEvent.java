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
public interface SwitchEvent{
    void onSwitch(WorldState w, Pokemon p1, Pokemon p2);
}