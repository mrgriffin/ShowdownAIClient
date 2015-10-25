/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class Databases{
    //0 normal, 1 twice, 2 half, 3 none
    public static HashMap<String, HashMap<String, Integer>> typechart;
    public static HashMap<String, JSONObject> pokedex;
    public static HashMap<String, JSONObject> movedex;
    

/**
 * Created with IntelliJ IDEA.
 * User: Diego
 * Date: 04/10/13
 * Time: 16:56
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */


    /**
     * Reads a file and returns its content as a String[]
     * @param filename file to read
     * @return file content as String[], one line per element
     */

    
    public static void init(){
        try{
            File f = new File("typechart.js");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = null;
            String tcproto = "";
            while((line = br.readLine()) != null){
                tcproto+=line;
            }
            JSONObject jsono = new JSONObject(tcproto);
            String[] types = JSONObject.getNames(jsono);
            typechart = new HashMap<String, HashMap<String, Integer>>();
            for(String s : types){
                typechart.put(s, new HashMap<String, Integer>());
                JSONObject jtype = jsono.getJSONObject(s);
                String[] types2 = JSONObject.getNames(jtype);
                for(String st : types){
                    typechart.get(s).put(st, jtype.getInt(st));
                }
            }
            System.out.println("Typechart initialized");
        }
        catch(Exception e){
            e.printStackTrace(System.out);
            System.out.println("Error initializing typechart");
        }
        try{
            File f = new File("pokedex.js");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = null;
            pokedex = new HashMap<String, JSONObject>();
            while((line = br.readLine()) != null){
                String name = line.substring(0, line.indexOf(":"));
                JSONObject data = new JSONObject(line.substring(line.indexOf(":")+1, line.length() - 1));
                pokedex.put(name, data);
            }
            System.out.println("Pokedex initialized");
        }
        catch(Exception e){
            e.printStackTrace(System.out);
            System.out.println("Error initializing pokedex");
        }
        try{
            File f = new File("moves.js");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = null;
            String tcproto = "";
            int ignoreline = 0;
            //We need to ignore every line between function and the next },
            while((line = br.readLine()) != null){
                if(ignoreline > 0 || line.contains("function (")){
                    ignoreline += line.replaceAll("\\}", "").length() - line.replaceAll("\\{", "").length();
                    //System.out.println(ignoreline + ": " +line);
                }
                else if(ignoreline == 0){
                    if(line.contains("//")){
                        line = line.substring(0, line.indexOf("//"));
                    }
                    tcproto+=line;
                }
            }
            JSONObject jsono = new JSONObject(tcproto);
            //System.out.println(tcproto);
            String[] moves = JSONObject.getNames(jsono);
            movedex = new HashMap<String, JSONObject>();
            for(String s : moves){
                //System.out.println(jsono.getJSONObject(s));
                //String[] keys = JSONObject.getNames(jsono.getJSONObject(s));
                //System.out.println(s + ": " + jsono.get(s));
                
                movedex.put(s, jsono.getJSONObject(s));
                //System.out.println(s + ": " + movedex.get(s).get("basePower"));
            }
            
            System.out.println("Movedex initialized");
            br.close();
        }
        catch(Exception e){
            e.printStackTrace(System.out);
            System.out.println("Error initializing movedex");
        }
    }
}

