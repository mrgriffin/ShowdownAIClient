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
    String type1;
    String type2;
    HashMap<String, Integer> Stats;
    Ability abi;
    double height;
    double weight;
    ArrayList<Move> moves;
    int maxHp;
    
    //0 genderless, 1 male, 2 female
    int gender;
    
    //Battle values
    int hp;
    HashMap<String, Integer> boost;
    // "", "sleep", "poison", "burn", "paralysis", etc.
    String status;
    int statusRemaining;
    Item hitem;
    boolean iconsumed;
    
    //Volatile values (decremented each turn)
    //vtrapped: Volatile trapping (arena trap)
    //aurabreak: affected by aura break
    HashMap<String, Integer> volatiles;
    //Very volatile, reset each turn
    boolean moved;
    //full stat names, lowercase, special abbreviated to s, weight, accuracy, and evasion included
    HashMap<String, Double> tMultipliers;
    //0 for immunity.  types, statdown ("statdown"), etc
    HashMap<String, Double> addTypeResistances;
    HashMap<String, Double> addEffectResistances;
    boolean itemlocked;
    boolean indimmune;
    //Generic pokemon (opponent's)
    
    //deep copy constructor
    public Pokemon(Pokemon p){
        species = p.species;
        type1 = p.type1;
        type2 = p.type2;
        Stats = (HashMap<String,Integer>)p.Stats.clone();
        abi = new Ability(p.abi);
        height = p.height;
        weight = p.weight;
        moves = new ArrayList<Move>();
        for(Move m:p.moves){
            moves.add(new Move(m));
        }
        maxHp = p.maxHp;
        gender = p.gender;
        hp = p.hp;
        boost = (HashMap<String, Integer>)p.boost.clone();
        status = p.status;
        statusRemaining = p.statusRemaining;
        hitem = p.hitem;
        iconsumed = p.iconsumed;
        volatiles = (HashMap<String, Integer>)p.volatiles.clone();
        
        moved = false;
        tMultipliers = new HashMap<String, Double>();
        addTypeResistances = new HashMap<String, Double>();
        addEffectResistances = new HashMap<String, Double>();
        addEffectResistances = new HashMap<String, Double>();
        itemlocked = false;
        indimmune = false;
        
    }
    
    public Pokemon(JSONObject speciesData, int level){
        
    }
    //Specific Pokemon (ours)
    public Pokemon(JSONObject speciesData, int level, int[] ivs, int[] evs, Ability abi){
        
    }
    
    public void tempModify(String name, double value){
        if(tMultipliers.containsKey(name)){
            tMultipliers.put(name, tMultipliers.get(name) * value);
        }
        else{
            tMultipliers.put(name, value);
        }
    }
    
    public void boostStat(String name, int val, Pokemon Source){
        
        if(boost.containsKey(name)){
            boost.put(name, boost.get(name) + val);
            if(boost.get(name).compareTo(6) >= 0){
                boost.put(name, 6);
            }
            else if(boost.get(name).compareTo(6) <= 0){
                boost.put(name, -6);
            }
        }
        else{
            boost.put(name, val);
        }
        //call onBoost here
    }
    
    /**Does the same thing as boostStat, but bypasses onBoos**/
    public void boostStatBypass(String name, int val){
        if(boost.containsKey(name)){
            boost.put(name, boost.get(name) + val);
            if(boost.get(name).compareTo(6) >= 0){
                boost.put(name, 6);
            }
            else if(boost.get(name).compareTo(6) <= 0){
                boost.put(name, -6);
            }
        }
        else{
            boost.put(name, val);
        }
    }
    
    public void addVolatile(String name, int value){
        volatiles.put(name, value);
    }
    
    public void addEffectResist(String name, double value){
        addEffectResistances.put(name, value);
    }
    
    /**For non-attack damage**/
    public void indirectDamage(int amount){
        if(!indimmune){
            damage(amount, null);
        }
            
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
    
    /**Not yet implemented **/
    public double getResist(String type){
        return 1.0;
    }
    
    public boolean hasType(String type){
        return type1.equals(type) || type2.equals(type);
    }
    
    public boolean tryStatus(String type){
        if(!(addEffectResistances.containsKey(type) && addEffectResistances.get(type).equals(0))){
            return false;
        }
        else if(type.equals("burn") && hasType("Fire")){
            return false;
        }
        else if(type.equals("poison") && (hasType("Steel") || hasType("Poison"))){
            return false;
        }
        else if(type.equals("paralyze") && hasType("Electric")){
            return false;
        }
        else{
            status = type;
        }
        if(type.equals("sleep")){
            statusRemaining = (int)(Math.random() * 2) + 1;
        }
        return true;
    }
    
    public void cure(){
        status = "";
        statusRemaining = 0;
    }
}
