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
    int type;
    int mode;
    public Room(String rname, String uname){
        id = rname;
    }
    public void process(String line){
        System.out.println(line);
    }
}
