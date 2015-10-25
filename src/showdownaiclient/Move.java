/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class Move {
    //Things that need to be accounted for: boost, secondary, drain, target, 
    
    String category;
    
    String type;
    String name;
    boolean usesPhys;
    boolean targetsPhys;
    int base;
    HashMap<String, Integer> flags;
    int priority;
    int accuracy;
    //persistent volatile
    boolean disabled;
    //If this move is disabled, how many turns is it diabled for
    int disint;
    //Very volatile
    boolean crit;
    boolean nullified;
    //Reset to 1 or 0 at the end of every turn
    double tMultiplier = 1.0;
    double aMultiplier = 1.0;
    int pmod = 0;
    boolean infiltrate;
    boolean ignoreeva;
    boolean oozed;
    boolean bounced;
    //resets to 1.5 each turn
    double stab = 1.5;
    String target;
    JSONObject secondary = null;
    JSONArray drains = null;
    JSONObject boosts = null;
   
    public void printinfo(){
        System.out.println("Name: " + name + " (" + category + ")");
        System.out.println("Base: " + base);
        System.out.println("Accuracy: " + accuracy);
        System.out.println("Type: " + type);
        System.out.println();
    }
    
    public Move(Move m){
        category = m.category;
        type = m.type;
        name = m.name;
        base = m.base;
        flags = (HashMap<String, Integer>) m.flags.clone();
        priority = m.priority;
        disabled = m.disabled;
        disint = m.disint;
        usesPhys = m.usesPhys;
        targetsPhys = m.targetsPhys;
        
        crit = false;
        nullified = false;
        tMultiplier = 1.0;
        aMultiplier = 1.0;
        pmod = 0;
        infiltrate = false;
        ignoreeva = false;
        oozed = false;
        bounced = false;
        stab = 1.5;
        target = m.target;
        
        drains = m.drains;
        secondary = m.secondary;
        boosts = m.boosts;
    }
    
    public Move(String n){
        try{
            String hpcase = "";
            if(n.startsWith("hiddenpower")){
                hpcase = n.substring("hiddenpower".length(), n.length() - 2);
                hpcase = Character.toUpperCase(hpcase.charAt(0)) + hpcase.substring(1);
                n = "hiddenpower";
            }
            name = n;
            flags = new HashMap<String, Integer>();
            JSONObject movedata = Databases.movedex.get(n.replaceAll(" ", "").replaceAll("-", ""));
            category = movedata.getString("category");
            targetsPhys = category.equals("Physical");
            usesPhys = category.equals("Physical");
            type = movedata.getString("type");
            if(name.equals("hiddenpower")){
                type = hpcase;
            }
            accuracy = movedata.optInt("accuracy");
            if(accuracy == 0){
                accuracy = 10000;
            }
            base = movedata.getInt("basePower");
            priority = movedata.getInt("priority");
            JSONObject fjson = movedata.getJSONObject("flags");
            String[] fnames = JSONObject.getNames(fjson);
            if(fnames != null){
                for(String s: fnames){
                    flags.put(s, fjson.getInt(s));
                }
            }
            disabled = false;
            disint = 0;
            crit = false;
            nullified = false;
            tMultiplier = 1.0;
            aMultiplier = 1.0;
            pmod = 0;
            infiltrate = false;
            ignoreeva = false;
            oozed = false;
            bounced = false;
            stab = 1.5;
            target = movedata.getString("target");
            if(movedata.has("drain")){
                drains = movedata.getJSONArray("drain");
            }
            if(movedata.has("secondary")){
                secondary = movedata.optJSONObject("secondary");
            }
            if(movedata.has("boosts")){
                boosts = movedata.getJSONObject("boosts");
            }
        }
        catch(Exception e){
            e.printStackTrace(System.out);
        }
    }
    
    //Generic moves for the opponent model
    public Move(String utype, boolean physical){
        if(physical){
            category = "Physical";
            targetsPhys = true;
            targetsPhys = true;
        }
        else{
            category = "Special";
            targetsPhys = false;
            targetsPhys = false;
        }
        type = utype;
        name = "generic" + utype + physical;
        base = 75;
        flags = new HashMap<String, Integer>();
        priority = 0;
        accuracy = 100;
        disabled = false;
        disint = 0;
        crit = false;
        nullified = false;
        tMultiplier = 1.0;
        aMultiplier = 1.0;
        pmod = 0;
        infiltrate = false;
        ignoreeva = false;
        oozed = false;
        bounced = false;
        stab = 1.5;
        target = "Any";
    }
    
    public void nullify(){
        nullified = true;
    }
    
    
}
