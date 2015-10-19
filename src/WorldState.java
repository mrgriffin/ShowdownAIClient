/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdownaiclient;

/**
 *
 * @author Admin
 */
public class WorldState {
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
    private boolean ignoreWeather;
    
    /** Force the engine to ignore weather effects. Resets at the end of turn.**/
    public void ignoreWeather(){
        ignoreWeather = true;
    }
}
