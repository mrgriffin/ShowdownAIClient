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
    ArrayList<Pokemon> myTeam;
    ArrayList<Pokemon> oppTeam;
    
    //How the forward model works:
    //In any turn:
    //Call all onTurnStart lambda functions
    //Call all onUpdatePokemon labda functions
    //Process Effects
    //Prompt for decisions
    //Decide turn order
    //Call onBeforeMove, switchEvent if applicable
    //Call check resistances
    //Call calculate move
    //Call onBeforeDamage
    //Call check for nullification
    //Call onAfterDamage, boost event, itemevent if applicable
    //Repeat
    //Call onResidual/decrement all effects
    
    
    //For boosts
    //move is declared
    //boost is applied
    //onboost is called
    
    //"", sunny, rain, sandstorm, deltastream, deolateland, etc.
    private String weather;
    private int weatherturns;
    private HashMap<String, Integer> fieldEffects;
    private boolean ignoreWeather;
    
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
