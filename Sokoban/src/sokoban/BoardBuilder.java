/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;

/**
 * Un constructeur de plateau
 * 
 * @author lbrouet
 */
interface BoardBuilder{
    
    /**
     * Construit un plateau
     * 
     * @return le plateau construit
     * @throws BuilderException 
     */
    Board build() throws BuilderException ;
}
