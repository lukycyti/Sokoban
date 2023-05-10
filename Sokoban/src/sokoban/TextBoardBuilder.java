/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;


/**
 * Un constructeur de plateau à partir de texte
 * @author lbrouet
 */
public class TextBoardBuilder implements BoardBuilder{
    String boardString = "";
    String name;
    int nbCol = 0;
    int nbRow = 0;
    
    /**
     * Le constructeur
     * 
     * @param name Le nom du plateau
     */
    public TextBoardBuilder(String name){
        this.name = name;
    }
    
    /**
     * Ajoute une ligne au futur plateau
     * 
     * @param s la ligne
     * @throws BuilderException 
     */
    public void addRow(String s) throws BuilderException{
        boardString = boardString.concat(s + "\n");
        if (invalidBoard(s)){
            throw new BuilderException("Nombre de colonne invalide");
        }
        nbCol = s.length();
        nbRow++;
    }
    
    /**
     * Determine si le plateau est invalide à partir de
     * la ligne ajoutée
     * 
     * @param s la ligne ajoutée
     * @return vrai si le plateau est invalide
     */
    private boolean invalidBoard(String s){
        return nbCol != 0 && s.length() != nbCol;
    }
    
    /**
     * Méthode appelée si une erreur de fichier s'est 
     * produite dans le FileBoardBuilder
     * 
     * @throws BuilderException 
     */
    public void fileNotFound() throws BuilderException{
        System.err.println("Fichier non trouvé");
        boardString = null;
        throw new BuilderException("Fichier inexistant");
    }

    /**
     * Construit un plateau à partir de la chaîne de caractère (boardString)
     * 
     * @return le plateau construit
     * @throws BuilderException 
     */
    @Override
    public Board build() throws BuilderException {
        if(boardString == null) { return null; }
        if(nbRow < 3 || nbCol < 3){
            throw new BuilderException("Le plateau doit être au moins de 3x3"); 
        }
        Board b = new Board(name, nbRow, nbCol);
        int col = 0 ; int row = 0; int nbTarget = 0; int nbBox = 0; int player = 0;
        for (int i = 0; i < boardString.length(); i++) {
            char c = boardString.charAt(i);
            switch(c){
                case '#': b.addWall(col, row); row++; break;
                case '.': row++ ; break;
                case 'C': b.addBox(col, row); row++ ; nbBox++ ; break;
                case 'P': b.setPosition(col, row); row++ ; player++ ; break;
                case ' ': break;
                case 'x': b.addTarget(col, row); row++ ; nbTarget++; break;
                case '\n': row = 0; col++; break;
                default: throw new BuilderException("Caractère(s) invalide(s)");
            }
        }
        
        // Erreurs de build 
        
        if(nbBox < nbTarget){ 
            throw new BuilderException("Le nombre de caisse doit être supérieur ou égal au nombre de cible");
        }
        if(player != 1){
            throw new BuilderException("Il doit y avoir un joueur (et un seul) sur plateau");
        }
        if(nbTarget == 0){
            throw new BuilderException("Le nombre de cible doit être supèrieur à 0");
        }
        return b;
    }
}
