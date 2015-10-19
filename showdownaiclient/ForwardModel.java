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
    //Decide turn order
    //call onBeforeMove, onSwitch
    //check resistances
    //call onBeforeDamage
    //call damage calc
    //call onAfterDamage
    //call onResidual
    End Turn
}
