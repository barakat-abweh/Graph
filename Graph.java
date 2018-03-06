/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Graph;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeSet;

/**
 *
 * @author theblackdevil
 */
class Graph {
    /**
     * @param args the command line arguments
     */
    int adjMatrix[][];
    private Node[] nodes;
    private  ReadFile rf;
    private double radius=Double.POSITIVE_INFINITY;
    private double diameter=Double.POSITIVE_INFINITY;
    private double girth=0.0;
    private double circumference=Double.POSITIVE_INFINITY;
    public Graph() {
        initializeNodes();
        initializeAdjMatrix(nodes.length);
        linkNodesToNeighbours();
    }
    public double getRadius(){
        return this.radius;
    }
    public void setRadius(double radius){
        this.radius=radius;
    }
    public double getDiameter(){
        return this.diameter;
    }
    public void setDiameter(double diameter){
        this.diameter=diameter;
    }
    public double getGirth(){
        return this.girth;
    }
    public void setGirth(double girth){
        this.girth=girth;
    }
    public double getCircumference(){
        return this.circumference;
    }
    public void setCircumference(double circumference){
        this.circumference=circumference;
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
        for(int i=0;i<this.nodes.length;i++)
        {
            this.nodes[i].sortNeighbours();
        }
    }
    
    Node getNode(String nodename) {
        Node node=null;
        for(int i=0;i<nodes.length;i++){
            if(nodes[i].toString().equalsIgnoreCase(nodename)){node=nodes[i];break;}
        }
        return node;
    }
    
    boolean isConnected(){
        int visitedNodes=bfs().size();
        if(visitedNodes!=this.nodes.length){
            return false;
        }
        return true;
    }
    
    private void clearVisitedNodes() {
        for(int i=0;i<this.nodes.length;i++){
            this.nodes[i].visited=false;
        }
    }
    
    ArrayList<Node> bfs() {
        ArrayList<Node> temp=new ArrayList<Node>();
        Queue queue=new LinkedList();
        queue.add(this.nodes[0]);
        temp.add(this.nodes[0]);
        this.nodes[0].visited=true;
        while(!queue.isEmpty()) {
            Node node = (Node)queue.remove();
            Node neighbour=null;
            while((neighbour=node.getUnvisitedNeighbour())!=null){
                neighbour.visited=true;
                temp.add(neighbour);
                queue.add(neighbour);
            }
        }
        this.clearVisitedNodes();
        return temp;
    }
    ArrayList<Node> dfs() {
        ArrayList<Node> temp=new ArrayList<Node>();
        Stack stack=new Stack();
        stack.push(this.nodes[0]);
        temp.add(this.nodes[0]);
        this.nodes[0].visited=true;
        while(!stack.isEmpty()){
            Node node = (Node)stack.peek();
            Node neighbour=node.getUnvisitedNeighbour();
            if(neighbour!=null){
                neighbour.visited = true;
                temp.add(neighbour);
                stack.push(neighbour);
            }else{
                stack.pop();
            }
        }
        this.clearVisitedNodes();
        return temp;
    }
    
    void calculateDistances() {
        if(this.isConnected()){
            TreeSet<Double> distances=this.findDistances();
            double minEcc=getMinEcc(distances);
            double maxEcc=getMaxEcc(distances);
            this.setRadius(minEcc);
            this.setDiameter(maxEcc);
        }
        TreeSet<Double> cycles=this.findCycles();
    }
    
    TreeSet<Double> findDistances() {
        TreeSet<Double> distances=new TreeSet<>();
        for(int i=0;i<this.nodes.length;i++)
        {
            double tempDistances[]=findDistancesPerNode(this.nodes[i]);
            Arrays.sort(tempDistances);
            distances.add(tempDistances[tempDistances.length-1]);
        }
        return distances;
    }
    
    double[] findDistancesPerNode(Node source) {
        Queue<Node> queue=new LinkedList<>();
        queue.add(source);
        double distances[]=new double[this.nodes.length];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[Integer.parseInt(source.toString())-1]=0;
        while(!queue.isEmpty())
        {
            int node=Integer.parseInt(queue.poll().toString())-1;
            for(Node neighbour:this.nodes[node].getNeighbours())
            {
                if(distances[Integer.parseInt(neighbour.toString())-1]==Double.POSITIVE_INFINITY){
                    distances[Integer.parseInt(neighbour.toString())-1]=distances[node]+1;
                    queue.add(neighbour);
                }
            }
        }
        return distances;
    }
    
    private double getMinEcc(TreeSet<Double> distances) {
        return distances.first();
    }
    
    private double getMaxEcc(TreeSet<Double> distances) {
        return distances.last();
    }
    private double getMinCycle(TreeSet<Double> cycles) {
        return cycles.first();
    }
    
    private double getMaxCycle(TreeSet<Double> cycles) {
        return cycles.last();
    }
    
    TreeSet<Double> findCycles() {
        TreeSet<Double> cycles=new TreeSet<>();
        return cycles;
    }
    
}