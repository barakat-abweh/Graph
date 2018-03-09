/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Graph;
import java.io.*;
import java.util.*;
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
    final private void openFile(){
        this.file=new File(this.path+"/"+this.filename);
    }
    final private void readData(){
        try {
            this.scanner=new Scanner(file);
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        }
        this.nodesNames=scanner.nextLine().split(",");
        this.connections=new ArrayList<>();
        while(scanner.hasNextLine()){
            connections.add(scanner.nextLine());
        }
    }
    final private void closeFile() {
        this.scanner.close();
    }
    final public String[] getNodesNames(){
        return this.nodesNames;
    }
    final public String getNodeName(int index){
        return this.nodesNames[index];
    }
    final public ArrayList<String> getConnections(){
        return this.connections;
    }
}
