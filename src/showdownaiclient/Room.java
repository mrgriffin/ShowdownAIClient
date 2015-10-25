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
public class Room {
    String id;
    String name;
    String whoami;
    String myusername;
    Agent player;
    public Room(String rname, String uname){
        id = rname;
        myusername = uname;
        player = new TestingAgent();
    }
    public void process(String line){
        player.updateWorldState(line);
    }
}
