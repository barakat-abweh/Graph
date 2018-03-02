/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Graph;

import java.util.ArrayList;

/**
 *
 * @author theblackdevil
 */
public class Node {
    private String name;
    boolean visited=false;
    private ArrayList<Node> neighbours;
    private ArrayList<Integer> distances;
    public Node(String name){
        this.name=name;
        this.neighbours=new ArrayList<>();
        this.distances=new ArrayList<>();
    }
    public ArrayList<Node> getNeighbours(){
        return this.neighbours;
    }
    public void addNeighbour(Node neighbour){
        this.neighbours.add(neighbour);
        this.distances.add(1);
    }
    @Override
    public String toString(){
        return this.name;
    }
    
    Node getUnvisitedNeighbour() {
        for(int i=0;i<this.neighbours.size();i++){
            if(!this.neighbours.get(i).visited){return this.neighbours.get(i);}
        }
        return null;
    }
}
