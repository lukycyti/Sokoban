/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;


/**
 * Une position dans un plateau
 * 
 * @author lukyc
 */
public class Position {
    int col;
    int row;
    private char elements;
    private boolean isTarget;
    
    /**
     * Constructeur d'un position
     * 
     * @param col le numero de colonne
     * @param row le numero de ligne
     */
    public Position(int col, int row){
        this.col = col;
        this.row = row;
        this.elements = '.';
    }
    
    /**
     * Retourne le caratère affichée de la position
     * 
     * @return le caractère
     */
    public char display(){
        if(elements == '.' && isTarget){
            return 'x';
        }
        return elements;
    }
    
    /**
     * Definie l'élément sur cette position
     * 
     * @param e l'élément en caractère
     */
    public void setElement(char e){
        this.elements = e;
    }
    
    /**
     * definie cette position comme étant une cible
     */
    public void setTarget(){
        isTarget = true;
    }
    
    /**
     * Retourne l'élement de cette position
     * 
     * @return l'élément en caractère
     */
    public char getElement(){
        return this.elements;
    }
}
