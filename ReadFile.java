/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package specialtopics.assingment1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author theblackdevil
 */
public class ReadFile {
    private String path;
    private String filename;
    private File file;
    private Scanner scanner;
    private String nodesNames[];
    private ArrayList<String> connections;
    public ReadFile(String path,String filename){
        this.path=path;
        this.filename=filename;
        this.openFile();
        this.readData();
        this.closeFile();
    }
    private void openFile(){
        this.file=new File(this.path+"/"+this.filename);
    }
    private void readData(){
        try {
            this.scanner=new Scanner(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.nodesNames=scanner.nextLine().split(",");
        this.connections=new ArrayList<>();
        while(scanner.hasNextLine()){
            connections.add(scanner.nextLine());
        }
    }
    private void closeFile() {
       this.scanner.close();
    }
     public String[] getNodesNames(){
    return this.nodesNames;
    }
    public ArrayList<String> getConnections(){
    return this.connections;
    }
}
