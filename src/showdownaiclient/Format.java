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
public class Format {
    final char CONSTANT_SEARCHONLY = 'b';
    final char CONSTANT_CHALLONLY = 'c';
    final char CONSTANT_GENERAL = 'e';
    final char CONSTANT_NOTEAM = 'f';
    final char CONSTANT_CATEGORY = 'a';
    private String fname;
    private boolean search;
    private boolean challenge;
    private boolean needsteam;
    private boolean category;
    public Format(String name, char classification){
        fname = name;
        search = false;
        challenge = false;
        needsteam = true;
        category = false;
        if(classification == CONSTANT_SEARCHONLY){
            search = true;
        }
        else if(classification == CONSTANT_CHALLONLY){
            challenge =true;
        }
        else if(classification == CONSTANT_GENERAL){
            search = true;
            challenge = true;
        }
        else if(classification == CONSTANT_NOTEAM){
            search = true;
            challenge = true;
            needsteam = false;
        }
        else if(classification == CONSTANT_CATEGORY){
            category = true;
        }
    }
    public String getName(){
       return fname; 
    }
    public boolean isSearch(){
        return search;
    }
    public boolean teamRequired(){
        return needsteam;
    }
    public boolean isChallenge(){
        return challenge;
    }
    public boolean isCategory(){
        return category;
    }
}
