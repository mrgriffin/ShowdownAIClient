/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

import static com.sun.glass.ui.Application.run;

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
    void onSwitch(Pokemon p1, Pokemon p2);
}
interface PokemonUpdate{
    void onUpdatePokemon(Pokemon p1, Pokemon p2);
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
    //calls to move are as if the owner is acting
    MoveEvent onBeforeMove;
    //Calls to onupdatepokemon are typically made before decision and after every event
    PokemonUpdate onUpdatePokemon;
    PokemonUpdate onResidual;
    
    //No abilities care abotu opponent switching, so yeah
    SwitchEvent onSwitchIn;
    ItemUpdate onItemUse;
    
    BoostEvent onBoost;
    
    public Ability(String s){
        name = s;
        switch(s){
            case "adaptability":
                onBeforeMove = (WorldState w, Pokemon p1, Pokemon p2, Move m) -> 
                    {m.stab = 2;};
            case "aftermath":
                onAfterDamage = (WorldState w, Pokemon me, Pokemon them, Move m)->
                    {
                        if(me != them && m.flags.containsKey("contact") && me.hp < 1){
                            them.damage(them.maxHp / 4, me);
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
                onBeforeMove = (WorldState w, Pokemon me, Pokemon them, Move m)->{
                    if(them.moved){
                        m.tMultiplier *= 1.3;
                    }
                };
            case "angerpoint":
                onAfterDamage = (WorldState w, Pokemon me, Pokemon them, Move m)->
                {
                    if(me.hp < 1) return;
                    else if(m.crit){
                        me.boost.put("atk", 6);
                    }
                };
            //I'm not implementing this because anticipation is worthless to our AI
            case "anticipation":
            case "arenatrap":
                onUpdatePokemon = (Pokemon me, Pokemon them)->{
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
            case "aurabreal":
                onUpdatePokemon=(Pokemon me, Pokemon them)->{
                    them.addVolatile("aurabreak", 1);
                };
            case "baddreams":
                onResidual=(Pokemon me, Pokemon them)->{
                    if(them.status.equals("sleep")){
                        them.damage(them.maxHp/8, me);
                    }
                };
            case "battlearmor":
                onBeforeDamage=(WorldState w, Pokemon me, Pokemon them, Move m)->{
                    m.crit = false;
                };
            case "bigpecks":
                onSwitchIn=(Pokemon me, Pokemon them)->{
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
        }
    }
}
