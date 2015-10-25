/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Admin
 */
public class WorldState {
    private String weather;
    private int weatherturns;
    private HashMap<String, Integer> fieldEffects;
    private boolean ignoreWeather;
    
    Pokemon mine;
    ArrayList<Pokemon> team;
    Pokemon theirs;
    
    HashMap<String, Integer> mysideeffects;
    HashMap<String, Integer> theirsideeffects;
    
    //Make the first world state
    public WorldState(Pokemon myactive, ArrayList<Pokemon> myteam, Pokemon opp){
        weather = "";
        weatherturns = 0;
        fieldEffects = new HashMap<String,Integer>();
        ignoreWeather = false;
    
        mine = new Pokemon(myactive);
        team = new ArrayList<Pokemon>();
        for(Pokemon p:myteam){
            team.add(new Pokemon(p));
        }
        theirs = new Pokemon(opp);
        mysideeffects = new HashMap<String, Integer>();
        theirsideeffects = new HashMap<String, Integer>();
    }
    
    //deep copy constructor
    public WorldState(WorldState w){
        weather = w.weather;
        weatherturns = w.weatherturns;
        fieldEffects = (HashMap<String, Integer>) w.fieldEffects.clone();
        ignoreWeather = w.ignoreWeather;
        
        mine = new Pokemon(w.mine);
        team = new ArrayList<Pokemon>();
        for(Pokemon p: w.team){
            team.add(new Pokemon(p));
        }
        theirs = new Pokemon(w.theirs);
        mysideeffects = (HashMap<String, Integer>) w.mysideeffects.clone();
        theirsideeffects = (HashMap<String, Integer>) w.theirsideeffects.clone();
    }
    
    //How the forward model works:
    //In any turn:
    //Call all onTurnStart lambda functions
    //Call all onUpdatePokemon lambda functions
    //Process Effects
    //Prompt for decisions
    //call onBeforeMove, onSwitch
    //Decide turn order
    //Waking up also happens here
    //check resistances
    //First pokemon moves
    //call onBeforeDamage
    //call damage calc
    //call onAfterDamage
    //call onResidual, 
    //decrement pokemon volatiles
    //Decrement world values
    //End Turn
    
    
    //For boosts
    //move is declared
    //boost is applied
    //onboost is called
    
    //enacts a turn given the two choices here
    public void takeTurn(String mychoice, String theirchoice){
        
    }
    
    //"", sunny, rain, sandstorm, deltastream, deolateland, etc.
    
    
    /** Force the engine to ignore weather effects. Resets at the end of turn.**/
    public void ignoreWeather(){
        ignoreWeather = true;
    }
    
    public String getWeather(){
        return weather;
    }
    
    public void setWeather(String w, int turns){
        weather = w;
        weatherturns = turns;
    }
    
    public void useMove(Pokemon source, Pokemon target, Move m){
        
    }
}
