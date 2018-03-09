/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Graph;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author theblackdevil
 */
class OutputFile {
    private String path;
    private String filename;
    private String output;
    private File file;
    private PrintWriter pw;
    OutputFile(String path, String filename,String output) {
        this.path=path;
        this.filename=filename;
        this.output=output;
        this.openFile();
        this.WriteData();
        this.closeFile();
    }
    
    final private void openFile() {
        this.file=new File(this.path+"/"+this.filename);
    }
    
    final private void WriteData() {
        try {
            this.pw=new PrintWriter(this.file);
            this.pw.write(this.output);
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        }
        
    }
    
    final private void closeFile() {
        this.pw.close();
    }
    
}
