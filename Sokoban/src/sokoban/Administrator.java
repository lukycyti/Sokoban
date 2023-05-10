package sokoban;


import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static sokoban.Player.availableBoard;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Un administrateur du jeu
 * 
 * @author lukyc
 */
public class Administrator {
    
    static Scanner sc = new Scanner(System.in);
    static DataBase db;
    static boolean menu = true;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        db = new DataBase();
        // Si base de donnée inexistante
        try{
            availableBoard.addAll(db.getBoards());
        } catch (NullPointerException e){
            db.initialize();
        }
        try {
            while(menu){
                menu();
                System.out.println("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Menu de l'administrateur
     * 
     * @throws SQLException 
     */
    private static void menu() throws SQLException{
        
        System.out.println(" ADMINISTRATION INTERFACE - USE WITH CAUTION\n" +
        "1. Reinitialiser la base\n" +
        "2. Lister les plateaux\n" +
        "3. Afficher un plateau\n" +
        "4. Ajouter un plateau à partir d'un fichier\n" +
        "5. Supprimer un plateau de la base [Dangereux]\n" +
        "6. Quitter.");
        
        String choice = sc.next();
        
        switch(choice){
            case "1":
                createNewDatabase();
                break;
            case "2":
                Player.listBoard(db);
                break;
            case "3":
                showBoard();
                break;
            case "4":
                addBoardFromFile();
                break;
            case "5":
                removeBoardFromDatabase();
                break;
            case "6":
                menu = false;
                break;
            default:
                System.out.println("commande inconnue : " + choice);
                break;
        }
    }
    
    /**
     * Reinitialise la base de donnée
     */
    private static void createNewDatabase(){
        String m = "Voulez vous vraiment reinitialiser la base ?";
        if(!confirmation(m)){return;}
        availableBoard.forEach((String s) -> {
            String id = db.getID(s);
            db.remove(id);
        });
        availableBoard.clear();
        System.out.println("Base de donnée réinitialisée");
    }
    
    /**
     * Affiche un plateau choisi
     * 
     * @throws SQLException 
     */
    public static void showBoard() throws SQLException{
        System.out.println("Choix du plateau");
        Player.listBoard(db);
        if(availableBoard.isEmpty()) return;
        String choice = sc.next();
        while(!Player.isNumber(choice)){
            System.out.println("Veuillez rentrer un nombre");
            choice = sc.next();
        }
        int choiceInt = Integer.parseInt(choice);
        if(choiceInt <= availableBoard.size() && choiceInt >= 1){
            String choiceString = availableBoard.get(choiceInt - 1);
            Player.showBoard(db, choiceString);
        }
        else{
            System.out.println("Plateau inexistant");
        }
    }

    /**
     * Ajoute un plateau à partir d'un fichier
     * 
     * @throws SQLException 
     */
    private static void addBoardFromFile() throws SQLException{
        String ask = "Donnez le nom du fichier à ajouter";
        System.out.println(ask);
        String choice = sc.next().trim();
        if(boardAlreadyAdded(choice)) { return; }
        var builder = new FileBoardBuilder(choice + ".txt") ;
        try{
            Board b = builder.build();
            System.out.println("Donnez lui un nom");
            String name = sc.next().trim();
            while(availableBoard.contains(name)){
                System.out.println("Le nom de plateau existe déjà (veuillez choisir à nouveau)");
                name = sc.next().trim();
            }
            b.addName(name);
            db.add(choice, b);
            availableBoard.add(name);
            System.out.println("Plateau '" + b.name + "' ajouté avec succés");
        } catch (BuilderException ex) {
            System.out.print("Opération annulée \n");
        }
        // Afin de positionner le texte au bon endroit
        try{
            Thread.sleep(20);
        } catch(InterruptedException ie){
            System.out.println(ie.getMessage() + "Interruption");
        }
    }
    
    /**
     * Indique si le plateau à déjà été ajouté dans la base
     * 
     * @param choice le nom du plateau
     * @return vrai si le plateau est déjà présent
     */
    private static boolean boardAlreadyAdded(String choice){
        for(String b : availableBoard){
            if(choice.equals(db.getID(b))) {
                System.out.println("Ce plateau a déjà été ajouté");
                return true;
            }
        }
        return false;
    }

    /**
     * Supprime un plateau de la base
     * 
     * @throws SQLException 
     */
    private static void removeBoardFromDatabase() throws SQLException {
        System.out.println("Donnez le nom du plateau à supprimer (un nom inexistant annulera l'action)");
        Player.listBoard(db);
        String choice = sc.next();
        while(!Player.isNumber(choice)){
            System.out.println("Veuillez rentrer un nombre");
            choice = sc.next();
        }
        int choiceInt = Integer.parseInt(choice);
        if(choiceInt <= availableBoard.size() && choiceInt >= 1){
            String choiceString = availableBoard.get(choiceInt - 1);
        
            String s = "Etes vous sur de vouloir supprimer le plateau '" + choiceString + "' ?";
            if(confirmation(s)){
                String id = db.getID(choiceString);
                db.remove(id);
                availableBoard.remove(choiceString);
                System.out.println("Plateau '" + choiceString + "' retiré avec succés");
                return;
            }
        }
        System.out.println("Operation annulée");
    }
    
    /**
     * Message de confirmatio
     * 
     * @param message le message demandé
     * @return oui si l'utilisateur à confirmé
     */
    private static boolean confirmation(String message){
        System.out.println(message + " tapez CONFIRMATION");
        return sc.next().trim().equals("CONFIRMATION");
    }
}
