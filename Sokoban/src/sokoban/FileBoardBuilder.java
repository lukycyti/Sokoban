/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Un constructeur de plateau à partir d'un fichier
 * 
 * @author lbrouet
 */
public class FileBoardBuilder implements BoardBuilder{
    TextBoardBuilder builder;
    String file;
    static String PATH = "board\\";
    
    /**
     * Le constructeur 
     * 
     * @param file le fichier qui va être lu
     */
    public FileBoardBuilder(String file) {
        this.file = file;
        builder = new TextBoardBuilder(file);
    }
    
    /**
     * Lit le fichier et ajoute les lignes au textBoardBuilder
     * @throws BuilderException 
     */
    public void read() throws BuilderException {
        try {
        File f = new File(PATH + file);
            try (Scanner reader = new Scanner(f)) {
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    builder.addRow(data);
                }   }
        }   catch (FileNotFoundException e) {
            builder.fileNotFound();
        }
    }
    
    /**
     * Construit un plateau grace au textBoardBuilder
     * 
     * @return le plateau
     * @throws BuilderException 
     */
    @Override
    public Board build() throws BuilderException{
        read();
        return builder.build();
    }
}
