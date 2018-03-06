/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Graph;

import java.util.ArrayList;
import java.util.Collections;
/**
 *
 * @author theblackdevil
 */
public class Node implements Comparable<Object>{
    
    private String name;
    boolean visited = false;
    private ArrayList<Node> neighbours;
    
    public Node(String name) {
        this.name = name;
        this.neighbours = new ArrayList<>();
    }
    
    public ArrayList<Node> getNeighbours() {
        return this.neighbours;
    }
    
    public void addNeighbour(Node neighbour) {
        this.neighbours.add(neighbour);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    Node getUnvisitedNeighbour() {
        for (int i = 0; i < this.neighbours.size(); i++) {
            if (!this.neighbours.get(i).visited) {
                return this.neighbours.get(i);
            }
        }
        
        return null;
    }
    
    @Override
    public int compareTo(Object node) {
        if(Integer.parseInt(this.toString()) == Integer.parseInt(((Node)node).toString())) return 0;
        if(Integer.parseInt(this.toString()) > Integer.parseInt(((Node)node).toString())) return 1;
        return -1;
    }
    
    void sortNeighbours() {
        Collections.sort(this.neighbours);
    }
    
    
    
}
