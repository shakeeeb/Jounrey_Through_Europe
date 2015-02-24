/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.components;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author saleh
 */
public class City {
    private String name; // name of the city
    
    private boolean airport; //does it have an airport?
    private int airportRegion = 0; // which region is this airport in?
    
    private boolean Seaport; // is it a harbor?
    private ArrayList<City> seaNeighbors = new ArrayList(); // who are its neighbors by sea?
    
    // map information
    private int quadrant; //which quadrant is it in?
    private int x; // x coordinate
    private int y; // y coordinate
    
    // coordinates in relation to the flight map
    private int fx;
    private int fy;
    
    private ArrayList<City> neighbors = new ArrayList();
    private int numNeighbors = 0;
    
    private ArrayList<City> allNeighbors = new ArrayList();
    
    // also for the card version of the city which will be held in each players hand
    private String color;
    
    //this is for the card verision of the city, to see if the effect has ativated yet
    private boolean activated;
    private boolean hasCondition;
    private int[] conditions; // conditions will be encoded
    
    public City(String name){
        this.name = name;
    }
    
    public City(String name, String color, int quadrant, int x, int y){
        this.name = name;
        this.color = color;
        this.quadrant = quadrant;
        this.x = x;
        this.y = y;
    }
    
    public void setAllNeighbors(){
        allNeighbors = new ArrayList();
        allNeighbors.addAll(neighbors);
        allNeighbors.addAll(seaNeighbors);
    }
    
    public ArrayList<City> getAllNeighbors(){
        return allNeighbors;
    }
    
    public boolean isSeaNeighbor(City to){
        if(seaNeighbors.contains(to))
            return true;
        else
            return false;
    }
    
    public boolean isNeighbor(City to){
        if(neighbors.contains(to))
            return true;
        else
            return false;
    }
    
    public boolean isLegalMove(City to){
        boolean one = isNeighbor(to);
        if(one)
            System.out.println(to.getName() + " is a land neighbor to this city, "+ this.getName());
        boolean two = isSeaNeighbor(to);
        if(two)
            System.out.println(to.getName() + " is a sea neighbor to this city, "+ this.getName());
        boolean three = isLegalRegion(to.getRegion())  > 0;
        //boolean three = isLegalRegion(to.getRegion()) > 0;
        return(one || two || three);
    }
    public int whichTypeOfNeighbor(City c){ // gets which type of eghbor this s
        // 1 if by land, 2 if by sea, 3 if by air
        // assuming it is a neighbor
        if(this.isNeighbor(c)){ // land above all
            return 1;
        } else if(this.isSeaNeighbor(c)){ // sea 
            return 2;
        } else { // lastly airport
            return 3;
        }
    
    }
    
    public void setFCoordinates(int fx, int fy){
        this.fx = fx;
        this.fy = fy;
    }
    
    public int getFx(){
        return fx;
    }
    
    public int getFy(){
        return fy;
    }
    
    public void setRegion(int r){
        airport = true;
        this.airportRegion = r;
    }
    
    public int getQuadrant(){
        return quadrant;
    }
    
    public boolean isAirport(){
        return airport;
    }
    
    public boolean isHarbor(){
        return Seaport;
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public String getName(){
        return name;
    }
    public int getRegion(){
        return airportRegion;
    }
    
    public void addNeighbor(City friend){ // add a neghbor the the lst of neighbors, 
        // increment total list of neghbors
        //System.out.println("adding neighbor:" + friend.getName());
        neighbors.add(friend);
        numNeighbors = neighbors.size();
    
    }
    
    public void addSeaNeighbor(City waterhorse){ // add a neighbor to the lst of sea neighbors
        //System.out.println("adding neighbor:" + waterhorse.getName());
        seaNeighbors.add(waterhorse);
        numNeighbors++;
    }
    
    public int isLegalRegion(int i){
    switch(airportRegion){
        case 1: if((i == 2) || (i == 4)){
                    return 4;
                } else if (i == 1){
                    return 2;
                } else {
                    return -1;
                }
        case 2:if((i == 1) || (i == 3)){
                    return 4;
                } else if (i == 2){
                    return 2;
                } else {
                    return -1;
                }
        case 3: if((i == 2) || (i == 4) || (i == 6)){
                    return 4;
                } else if (i == 3){
                    return 2;
                } else {
                    return -1;
                }
        case 4: if((i == 3) || (i == 1) || (i == 5)){
                    return 4;
                } else if (i == 4){
                    return 2;
                } else {
                    return -1;
                }
        case 5: if((i == 6) || (i == 4)){
                    return 4;
                } else if (i == 5){
                    return 2;
                } else {
                    return -1;
                }
        case 6: if((i == 5) || (i == 3)){
                    return 4;
                } else if (i == 6){
                    return 2;
                } else {
                    return -1;
                } 
        default:
            return -1;
    }
    }
    
    public String getColor(){
        return this.color;
    }
    
    public void printNeighbors(){ // prints this cities landneghbors
        System.out.println(name);
        for(City c: neighbors){
            System.out.println("neighbor:"+ c.getName());
        }
        for(City c: seaNeighbors){
            System.out.println("sea neighbor:" + c.getName());
        }
    }
    
    public ArrayList<City> getNeighbors(){
        return neighbors;
    }
    
    public ArrayList<City> getSeaNeighbors(){
        return seaNeighbors;
    }
    
    public Image toImage(){
        // get name of the cty and turn it into a card
        //color.toString();
        
        String path = "file:images/"+color+"/"+name+".jpg";
        //System.out.println(path);
        Image card = new Image(path, 500, 300, true, false);
        return card;
    
    }
    
    @Override
    public boolean equals(Object other){
        if(other instanceof City){
            City c = (City)other;
            return name.equals(c.getName());
        } else { return false;}
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    
}