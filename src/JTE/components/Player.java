/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sun.reflect.generics.tree.Tree;

/**
 *
 * @author saleh
 */
public class Player {
    private boolean isComputer;
    private String name;
    private City Home;
    private City currentPos; // this is a city, a cty has a quadrant
    private ArrayList<City> history = new ArrayList();
    private ArrayList<City> destinations = new ArrayList();
    public int points = 0;
    private int futurePoints = 0;
    private String color;
    private ImageView piece;
    private ImageView flag;
    
    // for CPU Stuff
    // to fnd the shortest path, run a depth first search on the 'graph'; this will return a tree
    // then, run a depth first search on the tree.
    // do this indivdually for each location. then append everything into a queue
    // after this is done, use the queue for CPU movement
    private ArrayList<City> CPUPath = new ArrayList();
    private ArrayList<City> singlePath = new ArrayList();
    //private ArrayList<City> visited;
    private TreeNode head;
    
    public Player(boolean isCPU, String nombre, String color){ // now player has a color
        // he needs a piece and a flag
        isComputer = isCPU;
        name = nombre;
        this.color = color;
        // set piece and set flag
        String flagPath = "file:images/flag_"+color+".png";
        Image flagImage = new Image(flagPath);
        flag = new ImageView(flagImage);
        flag.setScaleX(.2);
        flag.setScaleY(.2);
        
        String piecePath = "file:images/piece_"+color+".png";
        Image pieceImage = new Image(piecePath);
        piece = new ImageView(pieceImage);
        piece.setScaleX(.15);
        piece.setScaleY(.15);
        if(isCPU){
            //CPUPath = new ArrayList();
            //visited = new ArrayList();
            
        }
    }
    public ImageView getPiece(){ // gets the piece
        return this.piece;
    }
    /**
     * this is for setup purposes
     * @param newPos 
     */
    public void setCurrentPos(City newPos){
        currentPos = newPos;
    }
    
    public void makeTree(City from, City to){ // makes a tree of treenodes using BFS 
        GenQueue<City> cities = new GenQueue();
        GenQueue<TreeNode> nodes = new GenQueue(); // these are parallel queues
        ArrayList<City> visited = new ArrayList();
        
        TreeNode cabeza = new TreeNode(from);
        head = cabeza;
        
        visited.add(from); // set up everything
        cities.enqueue(from);
        nodes.enqueue(cabeza);
        while(!cities.isEmpty()){//most important loop. look through every city using the queue
            City t = cities.dequeue();
            TreeNode h = nodes.dequeue(); // swthces head to shldren go to the right place
            if(t.equals(to))
                break;
            for(City l : t.getAllNeighbors()){// for each neighbor of t
                if(!visited.contains(l)){ // if the node is new, hasn't been visted yet
                    visited.add(l);
                    cities.enqueue(l); // place the node in the queue
                    TreeNode leaf = new TreeNode(l);
                    nodes.enqueue(leaf); // place the node of the leaf in a parallel queue
                    h.add(leaf); // add it to the children of the current head
                }
            } 
        }
    }
    
    public void buildPaths(){
        // after the traversal is finished, print out the path
        for(int i = 0; i<destinations.size()-1;i++){
            makeTree(destinations.get(i), destinations.get(i+1)); // make the frst tree
            singlePath = new ArrayList();// refresh the arraylist
            treeTraversal(head, destinations.get(i+1)); // traverse the tree for the arraylist
            singlePath.add(head.getCity());
            // now i need to reverse the arraylist
            Collections.reverse(singlePath);
            // i need to stitch them all together and remove duplicates
            if(!(CPUPath.isEmpty()||singlePath.isEmpty())){
                singlePath.remove(0);
            }
            CPUPath.addAll(singlePath);
        } // after you've gone to your last city you must return home
        singlePath = new ArrayList();
        makeTree(destinations.get(destinations.size()-1), Home); // make the tree
        treeTraversal(head, Home);
        Collections.reverse(singlePath);
        CPUPath.addAll(singlePath);
        // doesnt work just yet for sea or air. will never work for air.
        System.out.println(CPUPath.toString());
    }
    
    public boolean treeTraversal(TreeNode node , City to){ // adds things to an arraylist recursively
        // the arraylst is outsde the scope so t'll just add to it.
        if(node.getCity().equals(to)){
            //singlePath.add(node.getCity());
            return true;
        }
        for(TreeNode n : node.getLeaves()){
            if(treeTraversal(n, to)){
                singlePath.add(n.getCity());
                return true;
            }
        } 
        return false;
    }
    
    public ArrayList<City> PCgetPath(){
        return CPUPath; //  have to replace it after doing shit
    }
    public void PCsetPath(ArrayList<City> newPath){
        CPUPath = newPath;
    }
    
    public City getCurrentPos(){
        return currentPos;
    }
    public int getQuadrant(){
        return currentPos.getQuadrant();
    }
    /**
     * this is for actually moving the city location during the actual game
     * @param newPos 
     */
    public void move(City newPos){
        if(points > 0){
            currentPos = newPos;
            points--;
            System.out.println("new postion: "+ newPos.getName());
        } else {System.out.println("not enough points");}
    }
    public ImageView getFlag(){ // gets the flag
        return this.flag;
    }
    public void setHome(City home){
        this.Home = home;
        destinations.add(home);
        currentPos = home;
    }
    public void addDestination(City c){
        destinations.add(c);
    }
    public City getHome(){
        return Home;
    }
    public ArrayList<City> getDestinations(){
        return destinations;
    }
    @Override
    public String toString(){
        return name;
    }
    public String getColor(){
        return this.color;
    }
    public boolean peekDest(){ // check dest, no strings attached
        for(City c: destinations){
            if(currentPos.equals(c)){
                return true;
            }
        }
        return false;
    }
    public boolean checkDest(){ // compares current city to destnation
        // if  reach a dsetination, i stop there.
        for(City c: destinations){
            if(currentPos.getName().equals(c.getName())){
                if(!currentPos.getName().equals(Home.getName())){// if its not home
                    removeDest(c);
                    return true;
                } else {// home needs be removed last
                    if(destinations.size() == 1){
                        removeDest(c);
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }
    public void removeDest(City c){ // removes destnation. what if a card has specal effects?
        destinations.remove(c);
    }
    public boolean hasWon(){
        if(destinations.isEmpty()){
            if(currentPos.getName().equals(Home.getName())){
            return true;
            }
        }
        return false;
    }
    public Boolean isCPU(){
        return isComputer;
    }
    
}
