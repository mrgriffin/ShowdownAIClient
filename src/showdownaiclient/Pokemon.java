/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class Pokemon {
    //Intrinsic values
    String species;
    int level;
    String type1;
    String type2;
    //atk, def, spa, spd, spe
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
    // "", "slp", "psn", "brn", "par", etc.
    String status;
    int statusRemaining;
    Item hitem;
    boolean iconsumed;
    
    //Volatile values (decremented each turn)
    //vtrapped: Volatile trapping (arena trap)
    //aurabreak: affected by aura break
    HashMap<String, Integer> volatiles;
    //Very volatile, reevaluate each turn
    boolean moved;
    //full stat names, lowercase, special abbreviated to s, weight, accuracy, and evasion included
    HashMap<String, Double> tMultipliers;
    //0 for immunity.  types, statdown ("statdown"), etc
    HashMap<String, Double> addTypeResistances;
    HashMap<String, Double> addEffectResistances;
    boolean itemlocked;
    boolean indimmune;
    //Generic pokemon (opponent's)
    
    public void printinfo(){
        System.out.println("Species: " + species);
        System.out.println("Level " + level);
        System.out.println("Typing: " + type1 + "-" + type2);
        for(Entry<String, Integer> e:Stats.entrySet()){
            System.out.println(e.getKey() + ": " + e.getValue());
        }
        System.out.println("Ability: " + abi.name);
        System.out.println("Current Status: " + hp + "/" + maxHp);
        System.out.println("Item: " + hitem.name);
        System.out.println("Moves:");
        for(Move m:moves){
            m.printinfo();
        }
        System.out.println();
    }
    
    //deep copy constructor
    public Pokemon(Pokemon p){
        try{
        level = p.level;
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
        catch(Exception e){
            e.printStackTrace(System.out);
        }
    }
    
    public Pokemon(JSONObject currentdata){
        try{
            //Testing
            System.out.println("JSONConstructor!");
            String details = currentdata.getString("details");
            species = details.substring(0, details.indexOf(",")).toLowerCase();
            details = details.substring(details.indexOf(",") + 3);
            System.out.println(details);
            if(details.contains(",")){
                level = Integer.parseInt(details.substring(0, details.indexOf(",")));
            }
            else{
                level = Integer.parseInt(details.trim());
            }
            details = details.substring(details.length() - 1);
            
            if(details.equals("M")){
                gender = 1;
            }
            else if(details.equals("F")){
                gender = 2;
            }
            else{
                gender = 0;
            }
            JSONObject jstats = currentdata.getJSONObject("stats");
            //testing
            System.out.println(jstats);
            String statnames[] = JSONObject.getNames(jstats);
            Stats = new HashMap<String, Integer>();
            for(String s: statnames){
                Stats.put(s, jstats.getInt(s));
            }
            abi = new Ability(currentdata.getString("baseAbility"));
            hitem = new Item(currentdata.getString("item"));
            details = currentdata.getString("condition");
            hp = Integer.parseInt(details.substring(0, details.indexOf("/")));
            int mhealthend = details.length();
            if(details.contains(" ")){
                mhealthend = details.indexOf(" ");
            }
            maxHp = Integer.parseInt(details.substring(details.indexOf("/")+1, mhealthend));
            if(details.contains(" ")){
                status = details.substring(mhealthend + 1, details.length());
            }
            iconsumed = false;
            statusRemaining = -1;
            boost = new HashMap<String, Integer>();
            JSONArray mov = currentdata.getJSONArray("moves");
            moves = new ArrayList<Move>();
            for(int i = 0; i < mov.length(); i++){
                moves.add(new Move(mov.getString(i)));
            }
            species = species.toLowerCase().replaceAll("-", "").replaceAll(" ", "").replaceAll("\\.", "").replaceAll("'", "");
            JSONObject speciesdata = Databases.pokedex.get(species);
            JSONArray stypes = speciesdata.getJSONArray("types");
            type1 = stypes.getString(0);
            type2 = "";
            if(stypes.length() == 2){
                type2 = stypes.getString(1);
            }
            height = speciesdata.getDouble("heightm");
            weight = speciesdata.getDouble("weightkg");
            volatiles = new HashMap<String, Integer>();
            moved = false;
            tMultipliers = new HashMap<String, Double>();
            addTypeResistances = new HashMap<String, Double>();
            addEffectResistances = new HashMap<String, Double>();
            itemlocked = false;
            indimmune = false;
            
        }
        catch(Exception e){
            e.printStackTrace(System.out);
        }
    }
    //Generic pokemon, theirs
    //assumes pokemon has 31 IVs across the board, as well as 100 EVs
    public Pokemon(String pspecies, int level){
        try{
            species = pspecies;
            species = species.toLowerCase().replaceAll("-", "").replaceAll(" ", "").replaceAll("\\.", "");
            JSONObject speciesdata = Databases.pokedex.get(species);
            JSONArray stypes = speciesdata.getJSONArray("types");
            type1 = stypes.getString(0);
            type2 = "";
            if(stypes.length() == 2){
                type2 = stypes.getString(1);
            }
            height = speciesdata.getDouble("heightm");
            weight = speciesdata.getDouble("weightkg");
            volatiles = new HashMap<String, Integer>();
            moved = false;
            tMultipliers = new HashMap<String, Double>();
            addTypeResistances = new HashMap<String, Double>();
            addEffectResistances = new HashMap<String, Double>();
            itemlocked = false;
            indimmune = false;
            iconsumed = false;
            statusRemaining = -1;
            boost = new HashMap<String, Integer>();
            gender = 0;
            status = "";
            JSONObject jstats = speciesdata.getJSONObject("baseStats");
            //testing
            System.out.println(jstats);
            String statnames[] = JSONObject.getNames(jstats);
            Stats = new HashMap<String, Integer>();
            for(String s: statnames){
                if(s.equals("hp") == false){
                    Stats.put(s, (((jstats.getInt(s) * 2) + 56) * level/100)+5);
                }
                else{
                    //The fundamental assumption is that a pokemon will be at max health at the beginning of a battle
                    maxHp =  (((jstats.getInt(s) * 2) + 56) * level/100)+10 + level;
                    hp = maxHp;
                }
            }
            JSONObject abichoices = speciesdata.getJSONObject("abilities");
            String[] choicekeys = JSONObject.getNames(abichoices);
            abi = new Ability(abichoices.getString(choicekeys[(int)Math.random() * choicekeys.length]));
            moves = new ArrayList<Move>();
            moves.add(new Move(type1, true));
            moves.add(new Move(type1, false));
            if(type2.equals("") == false){
                moves.add(new Move(type2, true));
                moves.add(new Move(type2, false));
            }
        }
        catch(Exception e){
            System.out.println("Error parsing species data");
        }
    }
    
    public void learnMove(String name){
        boolean found = false;
        for(Move m:moves){
            if(m.name.equals(name)){
                found = true;
            }
        }
        if(found == false){
            moves.add(new Move(name));
        }
    }
    
    //will parse JSON
    public void updateByJSON(JSONObject jsono){
        
    }
    
    //will parse the string
    public void updateByOpcode(String input){
        
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
    
    /**Does the same thing as boostStat, but bypasses onBoost**/
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
