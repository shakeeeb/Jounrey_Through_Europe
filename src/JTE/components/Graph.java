 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.components;

import java.util.ArrayList;

/**
 *
 * @author saleh
 */
public class Graph {
    ArrayList<City> allCities;
    ArrayList<City> seaports = new ArrayList();
    ArrayList<City> airports = new ArrayList();
    ArrayList<City> Q1Cities = new ArrayList();
    ArrayList<City> Q2Cities = new ArrayList();
    ArrayList<City> Q3Cities = new ArrayList();
    ArrayList<City> Q4Cities = new ArrayList();
    
    public void Graph(){
    
    }
    
    public ArrayList<City> getAllCities(){
        return allCities;
    }
    public void setAirports(){
        for(City c: allCities){
            if(c.isAirport()){
                airports.add(c);
            }
        }
    }
    public ArrayList<City> getAirports(){
        return airports;
    }
    public ArrayList<City> getCitiesByColor(String color){
        ArrayList<City> returner = new ArrayList();
        for(City c : allCities){
            if(c.getColor().equals(color)){ // if the colors are the same, ADD T TO RETURNER
                returner.add(c);
            }
        }
        return returner;
    }
    
    public void setAllCities(ArrayList<City> initCities){
        this.allCities = initCities;
        setCitiesByQuadrant();
        setCitiesByTransportation();
    }
    
    public void setCitiesByQuadrant(){
        City pointer;
        for(int i=0;i < allCities.size(); i++){
            pointer = allCities.get(i);
            switch(pointer.getQuadrant()){
                case 1:
                    Q1Cities.add(pointer);
                    break;
                case 2:
                    Q2Cities.add(pointer);
                    break;
                case 3:
                    Q3Cities.add(pointer);
                    break;
                case 4:
                    Q4Cities.add(pointer);
                    break;
                default:
                    System.out.println("D'OH!");
                    break;
            }

        }
    }
    
    public void setCitiesByTransportation(){
        City pointer;
        for (int i=0; i<allCities.size();i++){
            pointer = allCities.get(i);
            if(pointer.isHarbor())
                seaports.add(pointer);
            if(pointer.isAirport())
                airports.add(pointer);
        }
                
    }
    public ArrayList<City> loadArrayList(int i){
        switch(i){
            case 1:
                return Q1Cities;
            case 2:
                return Q2Cities;
            case 3:
                return Q3Cities;
            case 4:
                return Q4Cities;
            default:
                System.out.println("GODDAMMNN IT");
                break;
        }
        return null;
    }
    
    public City search(String name){
        
        for(City c: allCities){
            if(c.getName().equals(name)){
                return c;
            }
        }
        System.out.println(name +" not found. returning null");
        return null;
        // this is a search
        
    }
    
    
}
