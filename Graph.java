/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Graph;


import java.util.*;
/**
 *
 * @author theblackdevil
 */
class Graph {
    /**
     * @param args the command line arguments
     */
    private int adjMatrix[][];
    int connections[][];
    private int numberOfConnections=0;
    private Node[] nodes;
    private  ReadFile rf;
    private double radius=Double.POSITIVE_INFINITY;
    private double diameter=Double.POSITIVE_INFINITY;
    private double girth=0.0;
    private double circumference=Double.POSITIVE_INFINITY;
    private TreeSet<String> bridges;
    private GraphCycleFinder gcf=new GraphCycleFinder();
    public Graph() {
        this.initializeNodes();
        this.initializeAdjMatrix(nodes.length);
        this.linkNodesToNeighbours();
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
    final private void initializeConnectionsMatrix(int size) {
        this.connections=new int[size][2];
    }
    final void initializeAdjMatrix(int row){
        this.adjMatrix=new int[row][row];
    }
    private void buildAdjMatrix(int row,int col){
        this.adjMatrix[row-1][col-1]=1;
        this.adjMatrix[col-1][row-1]=1;
    }
    final private void buildConnectionsMatrix(int row,int first,int second) {
        this.connections[row][0]=first;
        this.connections[row][1]=second;
    }
    public void printAdjMatrix(){
        for (int[] adjMatrix1 : adjMatrix) {
            System.out.println(Arrays.toString(adjMatrix1));
        }
    }
    
    final private void initializeNodes() {
        rf=new ReadFile("/home/theblackdevil/Desktop","graph.in");
        this.nodes=new Node[rf.getNodesNames().length];
        for(int i=0;i<nodes.length;i++){
            this.nodes[i]=new Node(rf.getNodeName(i));
        }
        Arrays.sort(this.nodes);
    }
    final private void linkNodesToNeighbours() {
        ArrayList<String> connections=rf.getConnections();
        this.initializeConnectionsMatrix(connections.size());
        int index=0;
        while(!connections.isEmpty()){
            String first=connections.get(0).substring(1,2);
            String second=connections.get(0).substring(3,4);
            connections.remove(0);
            buildAdjMatrix(Integer.parseInt(first), Integer.parseInt(second));
            buildConnectionsMatrix(index++,Integer.parseInt(first), Integer.parseInt(second));
            Node firstNode=getNode(first);
            Node secondNode=getNode(second);
            firstNode.addNeighbour(secondNode);
            secondNode.addNeighbour(firstNode);
        }
        for (Node node : this.nodes) {
            node.sortNeighbours();
        }
    }
    
    public Node getNode(String nodename) {
        Node node=null;
        for (Node node1 : this.nodes) {
            if (node1.toString().equalsIgnoreCase(nodename)) {
                node = node1;
                break;
            }
        }
        return node;
    }
    
    private boolean isConnected(){
        return bfs().size()== this.nodes.length;
    }
    
    private void clearVisitedNodes() {
        for (Node node : this.nodes) {
            node.visited = false;
        }
    }
    private ArrayList<Node> bfs() {
        ArrayList<Node> temp=new ArrayList<>();
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
    void calculateDistances() {
        this.findCycles();
        if(this.isConnected()){
            TreeSet<Double> distances=this.findDistances();
            double minEcc=getMinEcc(distances);
            double maxEcc=getMaxEcc(distances);
            this.setRadius(minEcc);
            this.setDiameter(maxEcc);
        }
        TreeSet<String> cycles=gcf.getCycles();
        if(cycles.size()<=0){
            this.bridges.add("Each edge is a bridge");
        }
        else{
        this.setGirth(this.getMinCycle(cycles));
        this.setCircumference(this.getMaxCycle(cycles));
        }
        if(isConnected()){
            this.findBridges(cycles);
        }
        else{
            this.bridges=new TreeSet<>();
            this.bridges.add("The Graph is disconnected, it contains no bridges and there is no way to find any bridge");
        }
    }
    
    TreeSet<Double> findDistances() {
        TreeSet<Double> distances=new TreeSet<>();
        for (Node node : this.nodes) {
            double[] tempDistances = findDistancesPerNode(node);
            Arrays.sort(tempDistances);
            distances.add(tempDistances[tempDistances.length-1]);
        }
        return distances;
    }
    
    private double[] findDistancesPerNode(Node source) {
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
    private double getMinCycle(TreeSet<String> cycles) {
        double length=Double.POSITIVE_INFINITY;
        for(String cycle:cycles){
            int cycleLength=cycle.split(",").length;
            length=cycleLength<length?cycleLength:length;
        }
        return length;
    }
    
    private double getMaxCycle(TreeSet<String> cycles) {
        double length=Double.NEGATIVE_INFINITY;
        for(String cycle:cycles){
             int cycleLength=cycle.split(",").length;
            length=cycleLength>length?cycleLength:length;
        }
        return length;
    }
    void findCycles() {
        gcf.setGraph(this.connections);
        gcf.run();
    }
    @Override
    public String toString(){
        return "The Graph has the following attributes:\n"
                + "1. Number of nodes = "+this.nodes.length
                +"\n2. Connected? = "+(this.isConnected()?"Connected":"Disconnected")
                +"\n3. Raidus = "+this.getRadius()
                +"\n4. Diameter = "+this.getDiameter()
                +"\n5. Girth = "+this.getGirth()
                +"\n6. Circumference = "+this.getCircumference()
                +"\n7. Bridges = "+this.getBridges();
    }
    
    private void findBridges(TreeSet<String> cycles) {
        ArrayList<Node> originalNodes=new ArrayList<>(Arrays.asList(this.nodes));
        TreeSet<String> nodesNames=new TreeSet<>();
        for(String cycle:cycles){
            nodesNames.addAll(Arrays.asList(cycle.split(",")));
        }
        ArrayList<Node>cyclesNodes=new ArrayList<>();
        for(String nodeName:nodesNames){
            cyclesNodes.add(this.getNode(nodeName));
        }
        originalNodes.removeAll(cyclesNodes);
        this.bridges=new TreeSet<>();
        for(Node node:originalNodes){
            for(Node neighbour:node.getNeighbours()){
                this.bridges.add(node+"-----"+neighbour);
            }
        }
    }

    private TreeSet getBridges() {
       return this.bridges;
    }
}