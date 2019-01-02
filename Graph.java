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
    private long runningTime;
    private int connections[][];
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
        this.calculateDistances();
        this.outputTofile();
        this.drawGraph();
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
    private String printAdjMatrix(){
        String adjMat="";
        for (int[] adjMatrix1 : adjMatrix) {
            adjMat+=Arrays.toString(adjMatrix1)+"\n";
        }
        return adjMat;
    }
    
    final private void initializeNodes() {
        rf=new ReadFile("path to file","input file name");
        this.nodes=new Node[rf.getNodesNames().length];
        for(int i=0;i<nodes.length;i++){
            this.nodes[i]=new Node(rf.getNodeName(i));
        }
        Arrays.sort(this.nodes);
    }
    final private void linkNodesToNeighbours() {
        ArrayList<String> connections=new ArrayList<>(this.rf.getConnections());
        this.initializeConnectionsMatrix(connections.size());
        int index=0;
        while(!connections.isEmpty()){
            String connection=connections.remove(0);
            connection = connection.replaceAll("[^-?0-9]+", " ");
            String first=connection.split(" ")[1];
            String second=connection.split(" ")[2];
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
        return bfs()== this.nodes.length;
    }
    
    private void clearVisitedNodes() {
        for (Node node : this.nodes) {
            node.visited = false;
        }
    }
    private int bfs() {
        int numOfVisitedNodes=1;
        Queue queue=new LinkedList();
        queue.add(this.nodes[0]);
        this.nodes[0].visited=true;
        while(!queue.isEmpty()) {
            Node node = (Node)queue.remove();
            Node neighbour=null;
            while((neighbour=node.getUnvisitedNeighbour())!=null){
                neighbour.visited=true;
                numOfVisitedNodes++;
                queue.add(neighbour);
            }
        }
        this.clearVisitedNodes();
        return  numOfVisitedNodes;
    }
    void calculateDistances() {
        long startTime=System.nanoTime();
        if(this.isConnected()){
            TreeSet<Double> distances=this.findDistances();
            double minEcc=getMinEcc(distances);
            double maxEcc=getMaxEcc(distances);
            this.setRadius(minEcc);
            this.setDiameter(maxEcc);
        }
        this.findCycles();
        TreeSet<String> cycles=gcf.getCycles();
        if(cycles.size()<=0){
            this.bridges=new TreeSet<>();
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
        long finishTime=System.nanoTime();
        this.runningTime=finishTime-startTime;
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
        gcf.start();
    }
    @Override
    public String toString(){
        return "The Graph has the following attributes:\n"
                + "1. Number of nodes = "+this.nodes.length
                +"\n2. Adjacency Matrix = \n"+this.printAdjMatrix()
                +"3. Connected? = "+(this.isConnected()?"Connected":"Disconnected")
                +"\n4. Raidus = "+this.getRadius()
                +"\n5. Diameter = "+this.getDiameter()
                +"\n6. Girth = "+this.getGirth()
                +"\n7. Circumference = "+this.getCircumference()
                +"\n8. Bridges = "+this.getBridges()
                +"\n9. running Time = "+this.getrunningTime()/1000000000.0+" seconds";
    }
    
    private void findBridges(TreeSet<String> cycles) {
        ArrayList<String> cycleEdges=new ArrayList<>();
        ArrayList<String> originalEdges=new ArrayList<>(rf.getConnections());
        for(String cycle:cycles){
            String tempCycles[]=cycle.split(",");
           // cycleEdges.add("("+tempCycles[0]+","+tempCycles[tempCycles.length-1]+")");
            for(int i=0;i<tempCycles.length;i++){
                cycleEdges.add("("+tempCycles[i%tempCycles.length]+","+tempCycles[(i+1)%tempCycles.length]+")");
            }
        }
        while(!cycleEdges.isEmpty()){
        String tempCon=cycleEdges.remove(0);
        for(int i=0;i<originalEdges.size();i++){
            if(tempCon.equalsIgnoreCase(originalEdges.get(i))||(tempCon.substring(1,2).equalsIgnoreCase(originalEdges.get(i).substring(3,4))&&tempCon.substring(3,4).equalsIgnoreCase(originalEdges.get(i).substring(1,2)))){
                originalEdges.remove(i);
            }
        }
        }
         this.bridges=new TreeSet<>();
        for(String edge:originalEdges){
                this.bridges.add(edge);
        }
    }
    
    private String getBridges() {
        return this.bridges.size()<=0?"There is no bridges":this.bridges.toString();
    }
    
    private void outputTofile() {
        new OutputFile("path to file","output file name",this.toString());
    }
    
    private long getrunningTime() {
        return this.runningTime;
    }
    
    private void drawGraph() {
        GraphDraw gd=new GraphDraw("Special Topic Assignment Graph");
        gd.setSize(800,600);
        gd.setVisible(true);
        for(Node x:this.nodes){
            gd.addNode(x.toString(),(int)(100 + Math.random() * (((gd.height*gd.width))/2)),(int)(100 + Math.random() * (((gd.height*gd.width))/2)));
        }
       for(Node x:this.nodes){
            for(Node y:x.getNeighbours()){
            gd.addEdge(gd.getIndexOfNode(x.toString()),gd.getIndexOfNode(y.toString()));
            }
    }
       
}
}
