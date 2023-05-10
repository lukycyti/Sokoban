/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;

/**
 * Jetée lorsque un plateau voulant être construit est invalide
 * 
 * @author lbrouet
 */
class BuilderException extends Exception {
    
    public BuilderException(String message){
        System.err.println("Plateau invalide : " + message);
    }
}
