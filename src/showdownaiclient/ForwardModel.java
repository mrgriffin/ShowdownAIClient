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
public class ForwardModel {
    //How the forward model works:
    //In any turn:
    //Call all onTurnStart lambda functions
    //Call all onUpdatePokemon labda functions
    
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
}
