

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;

import java.sql.*;
import java.util.ArrayList;

/**
 * La base de donnée
 * 
 * @author lukyc
 */
public class DataBase {
    Connection c;
    
    /**
     * Constructeur
     */
    public DataBase(){
        connect();
    }
    
    /**
     * Connexion à la base de donnée
     */
    private void connect() {
        String chemin = "data\\board.sqlite3";
        String URL = "jdbc:sqlite:" + chemin;
        chargerPiloteSQLite();
        
        try {
            Connection connexion = DriverManager.getConnection(URL);
            c = connexion;
        } catch (SQLException ex) {
            System.err.println("* Base " + URL + " introuvable.");
        }
    }
    
    /**
     * Chargement du Pilote SQLite
     */
    private static void chargerPiloteSQLite() {
        String sqlite_driver = "org.sqlite.JDBC";
        try {
            Class.forName(sqlite_driver);
        } catch (ClassNotFoundException ex) {
            System.err.println("* Driver " + sqlite_driver + " introuvable.");
            // System.exit(1);
        }
    }
    
    /**
     * Initialise la base de donnée en créant les tables
     */
    public void initialize(){
        try {
            Statement s = c.createStatement();
            s.execute("create table BOARDS (BOARD_ID TEXT NOT NULL, NAME TEXT NOT NULL, NB_ROWS INT NOT NULL,"
                    + "NB_COLS INT NOT NULL);");
            s.execute("create table ROWS(BOARD_ID, ROW_NUM INT NOT NULL, DESCRIPTION TEXT NOT NULL);");
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.err.println("Erreur lors de la création des tables");
        }
    }
    
    /**
     * Renvoit tous les nom des plateaux contenus dans la base
     * 
     * @return les noms de plateaux
     */
    public ArrayList<String> getBoards(){
        ArrayList<String> boards = new ArrayList<>();
        try{
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select * from BOARDS;");
            while(rs.next()){
                boards.add(rs.getString("NAME"));
            }
            return boards;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Renvoit l'ID du plateau en fonction de son nom
     * 
     * @param name le nom
     * @return l'ID
     */
    public String getID(String name){
        try{
            Statement s = c.createStatement();
            ResultSet rs2 = s.executeQuery("select BOARD_ID from BOARDS where NAME = '" + name + "';");
            return rs2.getString("BOARD_ID");
        } catch( SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Retourne un plateau en fonction de son ID
     * 
     * @param id l'ID
     * @return le plateau
     */
    public Board get(String id){
       
        try{
            TextBoardBuilder builder = new TextBoardBuilder(id);
            Statement s = c.createStatement();
            
            ResultSet rs2 = s.executeQuery("select * from ROWS where BOARD_ID = '" + id + "';");
            while(rs2.next()){
                String row = rs2.getString("DESCRIPTION");
                builder.addRow(row);
            }
            ResultSet rs = s.executeQuery("select NAME from BOARDS where BOARD_ID = '" + id + "';");
            String name = rs.getString("NAME");
            Board b = builder.build();
            b.addName(name);
            return b;
        } catch (SQLException | BuilderException e){
            System.out.println(e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Ajoute un plateau à la base
     * 
     * @param id l'ID du plateau
     * @param b le plateau
     */
    public void add(String id, Board b){
        try{
            Statement s = c.createStatement();
            s.execute("insert into BOARDS values ('" + id + "','" + b.name + "','" + b.nbCol + "','" + b.nbRow + "')");
            for (int rowNum = 0; rowNum < b.nbCol; rowNum++) {
                String description = b.getRow(rowNum);
                s.execute("insert into ROWS values ('" + id + "'," + rowNum + ",'" + description + "')");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            System.err.println("Erreur lors de l'ajout du plateau dans la base");
        }
    }
    
    /**
     * Retire un plateau de la base
     * 
     * @param id l'ID du plateau
     */
    public void remove(String id){
        try{
          Statement s = c.createStatement();  
          s.addBatch("delete from ROWS where BOARD_ID = '" + id + "';)");
          s.addBatch("delete from BOARDS where BOARD_ID = '" + id + "';)");
          s.executeBatch();
          s.clearBatch();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            System.err.println("Erreur lors de la suppression du plateau dans la base");
        }
    }
}
