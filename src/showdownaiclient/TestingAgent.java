/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class TestingAgent extends Agent {
    ArrayList<Pokemon> myteam;
    Pokemon opponent;
    String pval;
    ForwardModel currentWorld;
    public TestingAgent(){
        currentWorld = null;
        myteam = new ArrayList<Pokemon>();
        opponent = null;
    }

    @Override
    public String decide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    
    //I need to add code to update oppnent stuff.  It's easy, but I don;t have the internet right now, so i can't connect to the server to parse the opcodes
    @Override
    public void updateWorldState(String input) {
        //For the first turn, we initialize our team
        if(input.startsWith("|request|") && currentWorld == null && myteam.size() == 0){
            try {
                input = input.substring("|request|".length());
                JSONObject jsono = new JSONObject(input);
                String[] fields = JSONObject.getNames(jsono);
                for(String s: fields){
                    if(s.equals("side")){
                        JSONObject j2 = jsono.getJSONObject("side");
                        String sfields[] = JSONObject.getNames(j2);
                        for(String s2:sfields){
                            if(s2.equals("pokemon")){
                                JSONArray jarr = j2.getJSONArray("pokemon");
                                for(int i = 0; i < jarr.length(); i++){
                                    System.out.println("Pokemon " + i + "\n");
                                    JSONObject poke = jarr.getJSONObject(i);
                                    String pfields[] = JSONObject.getNames(poke);
                                    Pokemon npo = new Pokemon(poke);
                                    npo.printinfo(); 
                                    myteam.add(npo);
                                }
                            }
                            else{
                                System.out.println(s2 + ": " + j2.get(s2));
                            }
                        }
                    }
                    else{
                        System.out.println(s + ": " + jsono.get(s));
                    }
                }
                //System.out.println(input);
            } catch (JSONException ex) {
                Logger.getLogger(TestingAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            System.out.println(input);
        }
    }
}