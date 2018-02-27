/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Graph;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author theblackdevil
 */
class Graph {
    
    /**
     * @param args the command line arguments
     */
    int adjMatrix[][];
    Node[] nodes;
    ReadFile rf;
    public Graph() {
        initializeNodes();
        initializeAdjMatrix(nodes.length);
        linkNodesToNeighbours();
    }
    void initializeAdjMatrix(int row){
        adjMatrix=new int[row][row];
    }
    void buildAdjMatrix(int row,int col){
        adjMatrix[row-1][col-1]=1;
        adjMatrix[col-1][row-1]=1;
    }
    void printAdjMatrix(){
        for(int i=0;i<adjMatrix.length;i++){System.out.println(Arrays.toString(adjMatrix[i]));}
    }
    
    void initializeNodes() {
        rf=new ReadFile("/home/theblackdevil/Desktop","graph.in");
        nodes=new Node[rf.getNodesNames().length];
        for(int i=0;i<nodes.length;i++){
            nodes[i]=new Node(rf.getNodeName(i));
        }
    }
    
    void linkNodesToNeighbours() {
        ArrayList<String> connections=rf.getConnections();
        while(!connections.isEmpty()){
            String first=connections.get(0).substring(1,2);
            String second=connections.get(0).substring(3,4);
            connections.remove(0);
            buildAdjMatrix(Integer.parseInt(first), Integer.parseInt(second));
            Node firstNode=getNode(first);
            Node secondNode=getNode(second);
            firstNode.addNeighbour(secondNode);
            secondNode.addNeighbour(firstNode);
        }
    }
    
    Node getNode(String nodename) {
        Node node=null;
        for(int i=0;i<nodes.length;i++){
            if(nodes[i].toString().equalsIgnoreCase(nodename)){node=nodes[i];break;}
        }
        return node;
    }
    
    
}
