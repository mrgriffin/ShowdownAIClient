/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import static com.sun.glass.ui.Application.run;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Admin
 */

interface MoveEvent{
    void onUseMove(WorldState w, Pokemon p1, Pokemon p2, Move m);
}
interface WorldEffect{
    void onTurn(WorldState w);
}
interface SwitchEvent{
    void onSwitch(WorldState w, Pokemon p1, Pokemon p2);
}
interface PokemonUpdate{
    void onUpdatePokemon(WorldState w, Pokemon p1, Pokemon p2);
}
interface ItemUpdate{
    void onItemUse(Pokemon eater, Item i);
}
interface BoostEvent{
    void onBoost(Pokemon target, Pokemon source, String stat, int amount);
}


public class Ability {
    //Calls to damage are as if the owner is being targetted
    String name;
    MoveEvent onAfterDamage;
    MoveEvent onBeforeDamage;
    //
    WorldEffect onTurnStart;
    //called before priority calculation
    MoveEvent onBeforeMove;
    //Calls to onupdatepokemon are typically made before decision and after every event
    PokemonUpdate onUpdatePokemon;
    PokemonUpdate onResidual;
    
    //No abilities care abotu opponent switching, so yeah
    SwitchEvent onSwitchIn;
    ItemUpdate onItemUse;
    
    BoostEvent onBoost;
    
    boolean single;
    
    public Ability(Ability a){
        name = a.name;
        single = a.single;
        onAfterDamage = a.onAfterDamage;
        onBeforeDamage = a.onBeforeDamage;
        onTurnStart = a.onTurnStart;
        onBeforeMove = a.onBeforeMove;
        onUpdatePokemon = a.onUpdatePokemon;
        onResidual = a.onResidual;
        onSwitchIn = a.onSwitchIn;
        onItemUse = a.onItemUse;
        onBoost = a.onBoost;
    }
    
    public Ability(String s){
        name = s;
        single = false;
        switch(s){
            case "adaptability":
                onBeforeMove = (WorldState w, Pokemon p1, Pokemon p2, Move m) -> 
                    {m.stab = 2;};
            case "aftermath":
                onAfterDamage = (WorldState w, Pokemon me, Pokemon them, Move m)->
                    {
                        if(me != them && m.flags.containsKey("contact") && me.hp < 1
                                && them.abi.name.equals("damp") == false){
                            them.indirectDamage(them.maxHp / 4);
                        }
                    };
            case "aerilate":
                onBeforeMove = (WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.type.equals("Normal") && m.name.equals("naturalgift") == false){
                        m.type = "Flying";
                        m.tMultiplier *= 1.3;
                    }
                };
            case "airlock":
                onTurnStart = (WorldState w)->
                {
                    w.ignoreWeather();
                };
            case "analytic":
                onBeforeDamage = (WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(me.moved){
                        m.tMultiplier *= 1.3;
                    }
                };
            case "angerpoint":
                onAfterDamage = (WorldState w, Pokemon me, Pokemon them, Move m)->
                {
                    if(me.hp < 1) return;
                    else if(m.crit){
                        me.boostStat("attack", 12, me);
                    }
                };
            //I'm not implementing this because anticipation is worthless to our AI
            //case "anticipation":
            case "arenatrap":
                onUpdatePokemon = (WorldState w, Pokemon me, Pokemon them)->{
                    if(them.immune("Ground") == false){
                        them.addVolatile("vtrapped", 1);
                    }
                };
            case "aromaveil":
                onBeforeDamage = (WorldState w, Pokemon me, Pokemon them, Move m)->
                {
                    if(m.name.equals("attract") || m.name.equals("disable") ||
                            m.name.equals("encore") || m.name.equals("healblock")
                            || m.name.equals("taunt") || m.name.equals("torment")){
                        m.nullify();
                    }
                };
            case "aurabreak":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    them.addVolatile("aurabreak", 1);
                };
            case "baddreams":
                onResidual=(WorldState w, Pokemon me, Pokemon them)->{
                    if(them.status.equals("sleep")){
                        them.indirectDamage(them.maxHp/8);
                    }
                };
            case "battlearmor":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    m.crit = false;
                };
            case "bigpecks":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.addEffectResist("defdown", 0);
                };
            case "blaze":
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.type.equals("Fire") && me.hp <= me.hp/3){
                        m.tMultiplier *= 1.5;
                    }
                };
            case "bulletproof":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.flags.containsKey("bullet")){
                        m.nullify();
                    }
                };
            case "cheekpouch":
                onItemUse= (Pokemon user, Item i)->{
                   if(i.category.equals("berry")){
                       user.heal(user.maxHp/3);
                   } 
                };
            case "chlorophyll":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    if(w.getWeather().equals("sunny")){
                        me.tempModify("speed", 2);
                    }
                };
            case "clearbody":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them) -> {
                    me.addEffectResist("atkdown", 0);
                    me.addEffectResist("defdown", 0);
                    me.addEffectResist("satkdown", 0);
                    me.addEffectResist("sdefdown", 0);
                    me.addEffectResist("accdown", 0);
                    me.addEffectResist("evadown", 0);
                    me.addEffectResist("spedown", 0);
                };
            case "cloudnine":
                onTurnStart = (WorldState w)->{
                    w.ignoreWeather();
                };
            case "colorchange":
                onAfterDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.category.equals("support") == false){
                        me.type2 = "";
                        me.type1 = m.type;
                    }
                };
            case "competitive":
                onBoost=(Pokemon me, Pokemon them, String stat, int value)->{
                    if(me != them && value < 0){
                        me.boostStat("sattack", value, me);
                    }
                };
            case "compoundeyes":
                onBeforeMove=(WorldState w,Pokemon me, Pokemon them, Move m)->{
                    m.aMultiplier *= 1.3;
                };
            case "contrary":
                onBoost=(Pokemon me, Pokemon them, String stat, int value)->{
                    me.boostStatBypass(name, value * -2);
                };
            case "cursedbody":
                onAfterDamage=(WorldState w, Pokemon me, Pokemon  them, Move m)->{
                    for(Move mov : me.moves){
                        if(mov.disabled){
                            return;
                        }
                    }
                    m.disabled = true;
                    m.disint = 3;
                };
            case "cutecharm":
                onAfterDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.flags.containsKey("contact") && me.gender != 0
                            && them.gender != 0 && me.gender != them.gender){
                        if(Math.random() <= 0.3){
                            them.addVolatile("attract", 5);
                        }
                    }
                };
            case "damp":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.name.equals("explosion") && m.name.equals("selfdestruct")){
                        m.nullify();
                    }
                };
            case "darkaura":
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.type.equals("Dark")){                    
                        if(me.volatiles.containsKey("aurabreak")){
                            m.tMultiplier *= 3/4;
                        }
                        else{
                            m.tMultiplier *= 4/3;
                        }
                    }
                };
            case "defeatist":
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(me.hp <= me.maxHp/2){
                        m.tMultiplier *= 0.5;
                    }
                };
            case "defiant":
                onBoost=(Pokemon me, Pokemon them, String stat, int value)->{
                    if(me != them && value < 0){
                        me.boostStatBypass("attack", 2);
                    }
                };
                
            //Sticking to OU and random play, so this will almost never come up
            //case "deltastream":
            //Ubers exclusive
            //case "desoateand":
            case "downlaod":
                onSwitchIn=(WorldState w, Pokemon me, Pokemon them)->{
                    if(them.Stats.get("defense") < them.Stats.get("sdefense")){
                        me.boostStatBypass("attack", 1);
                    }
                    else{
                        me.boostStatBypass("sattack", 1);
                    }
                };
            case "drizzle":
                onSwitchIn=(WorldState w, Pokemon me, Pokemon them)->{
                    w.setWeather("rainy", -1);
                };
            case "drought":
                onSwitchIn=(WorldState w, Pokemon me, Pokemon them)->{
                    w.setWeather("sunny", -1);
                };
            case "dryskin":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon other, Move m)->{
                    if(m.type == "Fire"){
                        m.tMultiplier *= 1.25;
                    }
                    else if(m.type == "Water"){
                        m.nullify();
                        me.heal(me.maxHp / 4);
                    }
                };
                onResidual=(WorldState w, Pokemon me, Pokemon other)->{
                    if(w.getWeather().equals("sunny")){
                        me.damage(me.maxHp / 8, me);
                    }
                    else if(w.getWeather().equals("rainy")){
                        me.heal(me.maxHp/4);
                    }
                };
            case "earlybird":
                onResidual=(WorldState w, Pokemon me, Pokemon other)->{
                    if(me.status.equals("sleep")){
                        me.statusRemaining--;
                    }
                };
            case "effectspore":
                onAfterDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.flags.containsKey("contact") && them.status.equals("")
                            && them.immune("powder") == false){
                        double r = Math.random();
                        if(r < 0.1){
                            them.tryStatus("sleep");
                        }
                        else if(r < 0.2){
                            them.tryStatus("poison");
                        }
                        else if(r < 0.3){
                            them.tryStatus("paralyze");
                        }
                    }
                };
            case "fairyaura":
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.type.equals("Fairy")){                    
                        if(me.volatiles.containsKey("aurabreak")){
                            m.tMultiplier *= 3/4;
                        }
                        else{
                            m.tMultiplier *= 4/3;
                        }
                    }
                };
            case "filter":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(me.getResist(m.type) > 1){
                        m.tMultiplier *= 0.75;
                    }
                };
            case "flamebody":
                onAfterDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.flags.containsKey("contact")){
                        if(Math.random() < 0.3){
                            them.tryStatus("burn");
                        }
                    }
                };
            case "flareboost":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(me.status.equals("burn") && m.category.equals("special")){
                        m.tMultiplier *= 1.5;
                    }
                };
            case "flashfire":
                onAfterDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.type.equals("Fire")){
                        m.nullify();
                        single = true;
                    }
                };
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(single && m.type.equals("Fire")){
                        m.tMultiplier *= 1.5;
                    }
                };
            case "flowergift":
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(w.getWeather().equals("sunny")){
                        me.tempModify("attack", 1.5);
                        me.tempModify("sdefense", 1.5);
                    }
                };
            case "forecast":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    if(me.species.equals("Castform") == false){
                        return;
                    }
                    else{
                        if(w.getWeather().equals("sunny")){
                            me.species = "castformsunny";
                        }
                        else if(w.getWeather().equals("rainy")){
                            me.species = "castformrainy";
                        }
                        else if(w.getWeather().equals("hail")){
                            me.species = "castformsnowy";
                        }
                    }
                };
            //case "forewarn":  pointless
            //case "friendguard" pointless
            //case "frisk"  pointless
            case "furcoat":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.tempModify("defense", 2);
                };
            case "galewings":
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.type == "Flying"){
                        m.pmod++;
                    }
                };
            case "gluttony":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon then)->{
                    me.addVolatile("gluttony", 1);
                };
            case "gooey":
                onAfterDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.flags.containsKey("contact")){
                        them.boostStat("speed", -1, me);
                    }
                };
            //Not bothering with this one
            //case "grasspelt":
            case "guts":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    if(me.status.equals("") == false){
                        me.tempModify("attack", 1.5);
                        me.addVolatile("guts", 1);
                    }
                };
            case "harvest":
                onResidual=(WorldState w, Pokemon me, Pokemon them)->{
                    if(me.iconsumed && me.hitem.category.equals("berry")){
                        if(Math.random() > 0.5 || w.getWeather().equals("sunny")){
                            me.iconsumed = false;
                        }
                    }
                };
                //double battle exclusive
                //case "healer":
            case "heatproof":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.type.equals("Fire")){
                        m.tMultiplier *= 0.5;
                    }
                    
                };
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.addEffectResist("burn", 0.5);
                };
            case "heavymetal":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.tempModify("weight", 2);
                };
            //no competitive use
            //case "honeygather:
            case "hugepower":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.tempModify("attack", 2);
                };
            case "hustle":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.tempModify("attack", 1.5);
                };
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.category.equals("physical")){
                        m.aMultiplier *= 0.8;
                    }
                };
            case "hydration":
                onResidual=(WorldState w, Pokemon me, Pokemon them)->{
                    if(w.getWeather().equals("rainy")){
                        me.cure();
                    }
                };
            case "hypercutter":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.addEffectResist("atkdown", 0);
                };
            //imperfect information means that illusion is worthless to us
            //case "illusion":
            case "icebody":
                onResidual=(WorldState w, Pokemon me, Pokemon them)->{
                    if(w.getWeather().equals("hail")){
                        me.heal(me.maxHp/8);
                    }
                };
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.addEffectResist("hail", 0);
                };
            case "immunity":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    if(me.status.equals("poison")){
                        me.cure();
                    }
                    me.addEffectResist("poison", 0);
                };
            case "imposter":
                onSwitchIn=(WorldState w, Pokemon me, Pokemon them)->{
                    me.Stats = new HashMap<String, Integer>(them.Stats);
                    me.moves = new ArrayList<Move>(them.moves);
                    me.type1 = them.type1;
                    me.type2 = them.type2;
                    me.weight = them.weight;
                    me.height = them.height;
                };
            case "infiltrator":
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    m.infiltrate = true;
                };
            case "innerfocus":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.addEffectResist("flinch", 0);
                };
            case "insomnia":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    if(me.status.equals("sleep")){
                        me.cure();
                    }
                    me.addEffectResist("sleep", 0);
                };
            case "intimidate":
                onSwitchIn=(WorldState w, Pokemon me, Pokemon them)->{
                    them.boostStat("attack", -1, me);
                };
            case "ironbarbs":
                onAfterDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.flags.containsKey("contact")){
                        them.indirectDamage(them.maxHp/8);
                    }
                };
            case "ironfist":
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.flags.containsKey("punch")){
                        m.tMultiplier *= 1.2;
                    }
                };
            case "justified":
                onAfterDamage = (WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.type.equals("Dark")){
                        me.boostStat("attack", 1, me);
                    }
                };
            case "keeneye":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.addEffectResist("accdown", 0);
                };
                onBeforeMove=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    m.ignoreeva= true;
                };
            case "klutz":
                onUpdatePokemon = (WorldState w, Pokemon me, Pokemon them)->{
                    me.itemlocked = true;
                };
            case "leafguard":
                onUpdatePokemon = (WorldState w, Pokemon me, Pokemon them) ->{
                    if(w.getWeather().equals("sunny")){
                        me.addEffectResist("poison", 0);
                        me.addEffectResist("burn", 0);
                        me.addEffectResist("freeze", 0);
                        me.addEffectResist("paralyze", 0);
                        me.addEffectResist("sleep", 0);
                    }
                };
            case "levitate":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(me.volatiles.containsKey("grounded") == false || m.type.equals("Ground")){
                        m.nullify();
                    }
                };
            case "lightmetal":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.tempModify("weight", 0.5);
                };
            case "lightningrod":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.type.equals("Electric")){
                        m.nullify();
                        me.boostStat("sattack", 1, me);
                    }
                };
            case "limber":
                onUpdatePokemon=(WorldState w, Pokemon me, Pokemon them)->{
                    me.addEffectResist("paralyze", 0);
                };
            case "liquidooze":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.flags.containsKey("drain")){
                        m.oozed = true;
                    }
                };
            case "magicbounce":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(m.flags.containsKey("reflectable")){
                        if(m.bounced == false){
                            m.bounced = true;
                            w.useMove(me, them, m);
                        }
                        m.nullify();
                    }
                };
            case "magicguard":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    me.indimmune = true;
                };
            
        }
    }
}
