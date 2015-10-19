/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class Pokemon {
    //Intrinsic values
    String species;
    ArrayList<String> types;
    HashMap<String, Integer> Stats;
    Ability abi;
    double height;
    double weight;
    ArrayList<Move> moves;
    int maxHp;
    
    //Battle values
    int hp;
    HashMap<String, Integer> boost;
    // "", "sleep", "poison", "burn", "paralysis", etc.
    String status;
    int statusRemaining;
    //0 for immunity.  types, statdown ("statdown"), etc
    HashMap<String, Integer> addTypeResistances;
    HashMap<String, Integer> addEffectResistances;
    
    //Volatile values (decremented each turn)
    //vtrapped: Volatile trapping (arena trap)
    //aurabreak: affected by aura break
    HashMap<String, Integer> volatiles;
    boolean moved;
    
    //Generic pokemon (opponent's)
    public Pokemon(JSONObject speciesData, int level){
        
    }
    //Specific Pokemon (ours)
    public Pokemon(JSONObject speciesData, int level, int[] ivs, int[] evs, Ability abi){
        
    }
    
    public void addVolatile(String name, int value){
        volatiles.put(name, value);
    }
    
    public void addEffectResist(String name, int value){
        addEffectResistances.put(name, value);
    }
    
    /**Deducts health from pokemon.  To be used after calculation.
    Returns actual damage done**/
    public int damage(int amount, Pokemon source){
        if(hp < 1) return 0;
        if(amount <1 && amount > 0) return 1;
        amount = (int) Math.floor(amount);
        hp -= amount;
        if(hp <= 0){
            amount += hp;
        }
        
        return amount;
    }
    
    //After health calculation
    public int heal(int amount){
        if(hp < 1) return 0;
        if(amount < 1 && amount > 0) return 1;
        hp+=amount;
        if(hp >= maxHp){
            hp = maxHp;
        }
        return hp;
        
    }
    
    /** Not yet implemented **/
    public boolean immune(String type){
        return true;
    }

}
