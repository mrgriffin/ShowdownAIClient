/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class ForwardModel {
    public ForwardModel(ArrayList<Pokemon> team, Pokemon popponent){
        myteam = new ArrayList<Pokemon>();
        for(int i = 0; i < team.size(); i++){
            myteam.add(new Pokemon(team.get(i)));
        }
        myact = myteam.get(0);
        opponent = new Pokemon(popponent);
        alphaworld = new WorldState(myact, myteam, opponent);
    }
    public ForwardModel(WorldState w){
        alphaworld = w;
        myact = alphaworld.mine;
        myteam = alphaworld.team;
        opponent = alphaworld.theirs;
    }
    public void updateWorldState(String line){
        //This is where we get things like weather and field effects
        //parse input
        //apply volatiles, etc.
        //placeholders
        if(line.startsWith("World")){
            
        }
        else if(line.startsWith("Pokemon")){
            
        }
    }
    public void updatePokemon(ArrayList<Pokemon> team){
        //This is where take in the JSON files after turn 1
    }
    
    //I won't be affecting alphaworld ever
    public WorldState whatif(String mychoice, String theirchoice){
        WorldState betaworld = new WorldState(alphaworld);
        betaworld.takeTurn(mychoice, theirchoice);
        return betaworld;
    }
    
    
    
    Pokemon myact;
    ArrayList<Pokemon> myteam;
    Pokemon opponent;
    WorldState alphaworld;
}
