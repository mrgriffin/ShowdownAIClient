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
    String lastchoice;
    public TestingAgent(String room, PSDAISocket socks){
        super(room, socks);
        currentWorld = null;
        myteam = new ArrayList<Pokemon>();
        opponent = null;
    }

    @Override
    public String decide(ArrayList<String> choices) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    
    //I need to add code to update oppnent stuff.  It's easy, but I don;t have the internet right now, so i can't connect to the server to parse the opcodes
    @Override
    public void updateWorldState(String input) {
        if(input.startsWith("|request|") && myteam.size() == 0){
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
                    currentWorld = new ForwardModel(myteam, pval);
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
    else if(input.startsWith("|request|")){
        try{
            boolean trapped =false;
            ArrayList<String> choices = new ArrayList<String>();
            ArrayList<String> mchoices = new ArrayList<String>();
            ArrayList<String> schoices = new ArrayList<String>();
            input = input.substring("|request|".length());
            JSONObject jsono = new JSONObject(input);
            String[] fields = JSONObject.getNames(jsono);
            if(jsono.has("wait") == false || jsono.getBoolean("wait") == false){
            for(String s: fields){
                if(s.equals("active")){
                    System.out.println("You can choose one of the following:");

                    System.out.println(jsono.get(s));
                    JSONArray active = jsono.getJSONArray("active");
                    JSONObject inner = active.getJSONObject(0);
                    if(inner.has("trapped") && inner.getBoolean("trapped") == true){
                        trapped = true;
                    }
                    if(inner.has("maybeTrapped") && inner.getBoolean("maybeTrapped") == true){
                        trapped = true;
                    }
                    JSONArray movechoices = inner.getJSONArray("moves");
                    for(int i = 0; i < movechoices.length(); i++){
                        if(movechoices.getJSONObject(i).has("trapped")){
                            choices.add(lastchoice);
                            break;
                        }
                        else if(movechoices.getJSONObject(i).has("disabled") == false || movechoices.getJSONObject(i).getBoolean("disabled") == false){
                            String movename = movechoices.getJSONObject(i).getString("move");
                            System.out.println((i + 1) + ": " +movename);
                            mchoices.add("move " + (i+1));
                        }
                    }
                }
                else if(s.equals("side")){
                    System.out.println("You can switch to one of the following:");
                    System.out.println(jsono.get("side"));
                    JSONObject side = jsono.getJSONObject("side");
                    JSONArray t = side.getJSONArray("pokemon");
                    for(int i = 0; i < t.length(); i++){
                        String pdet = t.getJSONObject(i).getString("details");
                        String pcon = t.getJSONObject(i).getString("condition");
                        if(pcon.contains("fnt") == false && i > 0){
                            schoices.add("switch " + (i+1));
                        }
                        System.out.println((i+1)+": " + pdet + ", " + pcon);
                    }
                }

            }
            System.out.println("Making testing choice");
            for(String s: mchoices){
                choices.add(s);
            }
            if(!trapped){
                for(String s:schoices){
                    choices.add(s);
                }
            }
            
            }
        }
        catch(Exception e){
            e.printStackTrace(System.out);
            System.exit(1);
        }

    }   
    else if(currentWorld != null){
        currentWorld.updateWorldState(input);
    }
    else{
        System.out.println(input);
    }
}


}