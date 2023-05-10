/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;

import java.util.HashSet;
import java.util.Set;


/**
 * Un plateau
 * 
 * @author lbrouet
 */
public class Board {
    String name;
    String id;
    int nbCol;
    int nbRow;
    Position player;
    Set<Position> targets = new HashSet<>();
    
    Position[][] board;
    
    /**
     * Constructeur d'un plateau
     * 
     * @param id l'ID du plateau
     * @param nbCol le nombre de colonne
     * @param nbRow le nombre de ligne
     */
    public Board(String id, int nbCol, int nbRow){
        this.id = id;
        this.nbCol = nbCol;
        this.nbRow = nbRow;
        this.board = new Position[nbCol][nbRow];
        initializeBoard();
    }
    
    /**
     * Ajoute un nom au plateau (ou remplace celui déjà existant)
     * 
     * @param name le nom
     */
    public void addName(String name){
        this.name = name;
    }
    
    /**
     * Retourn la description de la ligne à partir de son numéro
     * 
     * @param i le numero de ligne
     * @return  la description de la ligne
     */
    public String getRow(int i){
        String s = "";
        for (int j = 0; j < nbRow; j++) {
            String character = Character.toString(board[i][j].display());
            s = s.concat(character);
        }
        return s;
    }
    
    /**
     * Initialise le plateau
     */
    private void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Position p = new Position(i, j);
                board[i][j] = p;
            }
        }
    }
    
    /**
     * Ajoute un mur horizontal
     * 
     * @param col la colonne concernée
     * @param row la ligne concernée
     * @param size la taille du mur
     */
    void addWall(int col, int row) {
            board[col][row].setElement('#');
    }

    /**
     * Ajoute un mur vertical
     * 
     * @param col la colonne concernée
     * @param row la ligne concernée
     * @param size la taille du mur
     */
    void addVerticalWall(int col, int row, int size) {
        while(!outOfBoard(col, row) && size != 0){
            board[col][row].setElement('#');
            col++;
            size--;
        }
    }

    /**
     * Ajoute une boite
     * 
     * @param col la colonne concernée
     * @param row la ligne concernée
     */
    void addBox(int col, int row) {
        board[col][row].setElement('C');
    }

    /**
     * Ajoute un cible
     * 
     * @param col la colonne concernée
     * @param row la ligne concernée
     */
    void addTarget(int col, int row) {
        board[col][row].setTarget();
        targets.add(board[col][row]);
    }

    /**
     * Definie la position du joueur
     * @param col la colonne concernée
     * @param row la ligne concernée
     */
    void setPosition(int col, int row) {
        board[col][row].setElement('P');
        player = board[col][row];
    }
    
    /**
     * Supprime tout élément sur une position
     * 
     * @param col la colonne concernée
     * @param row la ligne concernée
     */
    void setEmpty(int col, int row){
        board[col][row].setElement('.');

    }

    /**
     * Definie si la position est hors du plateau
     * 
     * @param col la colonne concernée
     * @param row la ligne concernée
     * @return 
     */
    private boolean outOfBoard(int col, int row) {
        return col >= nbCol || col < 0 || row >= nbRow || row < 0;
    }
    
    /**
     * Affiche le plateau
     */
    public void display(){
        for (Position[] board1 : board) {
            for (Position board11 : board1) {
                System.out.print(board11.display() + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * Déplace le joueur
     * 
     * @param dir la direction
     */
    public void displacePlayer(char dir) {
        Position nextPos = nextPos(player, dir);
        if(availablePos(nextPos, dir)){
           if(nextPos.getElement() == 'C'){
               Position nextPosBox = nextPos(nextPos, dir);
               while(nextPosBox.getElement() == 'C'){
                   nextPosBox = nextPos(nextPosBox, dir);
               }
               board[nextPosBox.col][nextPosBox.row].setElement('C');
           }
           player.setElement('.');
           setPosition(nextPos.col, nextPos.row);
        }
    }

    /**
     * Verifie la victoire
     * 
     * @return vrai s'il y a victoire (les caisses sont sur les cibles)
     */
    boolean won() {
        return targets.stream().noneMatch((p) -> (p.getElement() != 'C'));
    }

    /**
     * Retourne la prochaine position en fonction de la direction
     * 
     * @param p la position
     * @param dir la direction
     * @return la prochaine position
     */
    private Position nextPos(Position p, char dir) {
        switch(dir){
            case 'U':
                return getPos(p.col - 1, p.row);
            case 'D':
                return getPos(p.col + 1, p.row);
            case 'L':
                return getPos(p.col, p.row - 1);
            case 'R':
                return getPos(p.col, p.row + 1);
            default:
        }
        return null;
    }
    
    /**
     * Retourne la position à partir du numero de ligne et de colonne
     * 
     * @param col le numero de colonne
     * @param row le numero de ligne
     * @return la position
     */
    private Position getPos(int col, int row){
        for (Position[] board1 : board) {
            for (Position board11 : board1) {
                if(board11.col == col && board11.row == row){
                    return board11;
                }
            }
        }
        return null;
    }

    /**
     * Indique s'il est possible de se déplacer vers une certaine position
     * 
     * @param nextPos la futur postition
     * @param dir la direction
     * @return vrai si c'est possible
     */
    private boolean availablePos(Position nextPos, char dir) {
        if(nextPos == null){ return false; }
        switch(nextPos.getElement()){
            case '.':
                return true;
            case 'C':
//                 Une seule caisse a la fois :
//                 return nextPos(nextPos, dir).getElement() == '.';
//                 plusieurs caisses a la fois :
                return availablePos(nextPos(nextPos, dir), dir);
            default:
                return false;
        }
    }
}
