/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Un joueur du Jeu
 * 
 * @author lbrouet
 */
public class Player {
    
    static Scanner sc = new Scanner(System.in);
    static List<String> availableBoard = new ArrayList<>();
    static DataBase db;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        db = new DataBase();
        try{
            availableBoard.addAll(db.getBoards());
        } catch (NullPointerException e){
            db.initialize();
        }
        
        if(availableBoard.isEmpty()){
            System.out.println("Aucun plateau disponible actuellement");
            System.exit(0);
        }
        menu();
    }
    
    /**
     * Menu du jeu (choix du plateau)
     */
    private static void menu(){
        System.out.println("SOKOBAN -- MENU -- Choix du plateau");
        listBoard(db);
        String choice = sc.next();
        while(!isNumber(choice)){
            System.out.println("Veuillez rentrer un nombre");
            choice = sc.next();
        }
        int choiceInt = Integer.parseInt(choice);
        
        if(choiceInt <= availableBoard.size() && choiceInt >= 1){
            String choiceString = availableBoard.get(choiceInt - 1);
            showBoard(db, choiceString);
            System.out.print("Jouer avec ce plateau (oui / non) ? ");
            String confirmation = sc.next();
            if (confirmation.equals("oui")) {
                String id = db.getID(choiceString);
                Board b = db.get(id);
                System.out.println("Début de la partie !");
                play(b);
                return;
            }
        } else {
            System.out.println("Le plateau est inexistant");
        }
        menu();
    }
    
    /**
     * Verifie so la chaine de caractère correspond à un nombre
     * @param s la chaine de caractère
     * @return vrai si s est un nombre
     */
    public static boolean isNumber(String s){
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c < 48 || c > 57){
                return false;
            }
        }
        return true;
    }
    
    public static void play(Board b){
        b.display();
        boolean quit = false;
        while(!quit){
            InGameMenu();
            String action = userInput();
            action = action.toUpperCase();
            for(char c : action.toCharArray()) quit = playAction(b, c);
            b.display();
            if(quit){
                System.out.println("Abandon par le joueur");
            } else {
                quit = b.won();
                if(quit){
                    System.out.println("Vous avez gagné la partie");
                    break;
                }
            }
        }
    }
    
    /**
     * Le menu en jeu
     */
    private static void InGameMenu() {
        System.out.println("Que souhaitez vous jouer ? U, D, L, R, S (restart), Q (quit)");
    }

    /**
     * Demande au joueur une action
     * 
     * @return la chaine de caratère correspondante à l'action
     */
    private static String userInput() {
        Scanner s = new Scanner(System.in);
        return s.next().trim();
    }
    
    /**
     * Joue l'action sur un plateau donnée
     * 
     * @param b le plateau sur lequel la partie se joue
     * @param action le charatère correspondant à l'action
     * @return si la partie s'arrête
     */
    public static boolean playAction(Board b, char action){
        boolean stop = false;
        switch(action){
            case 'Q':
                stop = true;
                break;
            case 'S':
                play(reset(b));
                break;
            case 'U':
            case 'L':
            case 'R':
            case 'D':
                b.displacePlayer(action);
                break;
            default:
                System.out.println("Commande inconnue : " + action);
                break;
        }
        return stop;
    }
    
    /**
     * Liste tous les plateaux disponibles
     * 
     * @param db la base de donnée contenant les plateaux
     */
    public static void listBoard(DataBase db){
        if(availableBoard.isEmpty()){
            System.out.println("(Aucun plateau disponible)");
            return;
        }
        for (int i = 0; i < availableBoard.size(); i++) {
            System.out.println(i + 1 + ". " + availableBoard.get(i));
        }
    }
    
    /**
     * Affiche un plateau
     * 
     * @param db la base de donnée contenant les plateaux
     * @param choice le nom du plateau que  l'on veut afficher
     */
    public static void showBoard(DataBase db, String choice){
        if(!availableBoard.contains(choice)){
            System.out.println("Plateau inexistant");
            return;
        }
        String id = db.getID(choice);
        Board b = db.get(id);
        b.display();
    }
    
    /**
     * Reinitialise un plateau
     * 
     * @param b le plateau
     * @return le plateau reinitialisé
     */
    private static Board reset(Board b){
       Board reset = db.get(b.id);
       return reset;
    }
    
}
