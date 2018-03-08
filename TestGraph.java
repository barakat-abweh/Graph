/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Graph;

/**
 *
 * @author theblackdevil
 */
public class TestGraph {
    public static void main(String []args){
        Graph graph=new Graph();
        graph.calculateDistances();
        System.out.println(graph.toString());
    }
}