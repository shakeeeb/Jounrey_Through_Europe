/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.game;

import JTE.components.City;
import JTE.components.Graph;
import JTE.ui.JTEUI;
import big.data.DataSource;
import big.data.DataSourceIterator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//import properties_manager.PropertiesManager;
/**
 *
 * @author saleh
 */

// formula X*actualsize / apparentsize
public class JTEFileLoader {
    private JTEUI ui;
    private JTEGameStateManager gsm;
    private Stage primaryStage;
    
    public JTEFileLoader(JTEUI initUI, JTEGameStateManager gsm){
        this.ui = initUI;
        this.gsm = gsm;
        this.primaryStage = ui.getStage();
    }
    
    public void setNeighbors(Graph graph){
        try {
            City pointer = null; // pointer to main city
            City nPointer = null; // pointer to neighbors
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("cities.xml"));
            Node root = doc.getElementsByTagName("routes").item(0);
            NodeList cardlist = root.getChildNodes();
            for (int i = 0; i < cardlist.getLength(); i++) {
                Node cardNode = cardlist.item(i);
                if (cardNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList cardAttrs = cardNode.getChildNodes();
                    // one card
                    for (int j = 0; j < cardAttrs.getLength(); j++) {
                        if (cardAttrs.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Node theNode = cardAttrs.item(j);
                            switch (theNode.getNodeName()) {
                                case "name": // here i have to wrte code that says, find a city wth this name in the graph
                                    String cityName = theNode.getTextContent();
                                    pointer = graph.search(cityName);
//                                    System.out.println("City name: "
//                                            + theNode.getTextContent());
                                    break;
                                case "land": // these will be it's land neighbors, so  will add each one of these to the arraylist
                                    NodeList landList = theNode.getChildNodes();
                                    for (int k = 0; k < landList.getLength(); k++) {
                                        if (landList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                            // place into neighbors here
                                            // search for this city by name
                                            // then add it to neighbors
                                            nPointer = graph.search(landList.item(k).getTextContent());
                                            pointer.addNeighbor(nPointer);
//                                            System.out.println("Land neighbour: "
//                                                    + landList.item(k)
//                                                    .getTextContent());
                                        }
                                    }
                                    break;
                                case "sea":
                                    NodeList seaList = theNode.getChildNodes();
                                    for (int k = 0; k < seaList.getLength(); k++) {
                                        if (seaList.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                            // place into seaneighbors here
                                            // search for this city by name
                                            // and then set neighbor
                                            nPointer = graph.search(seaList.item(k).getTextContent());
                                            pointer.addSeaNeighbor(nPointer);
//                                            System.out.println("Sea neighbour: "
//                                                    + seaList.item(k)
//                                                    .getTextContent());
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<City> readCities(String fileName){
         //read cities will raed in all the cities from a csv file using bigdata.jar
        DataSource ds = DataSource.connectCSV(fileName);
        ds = ds.load();
        DataSourceIterator iter = ds.iterator();
        String cityName = "";
        String cityColor;
        int q; // quadrant
        int x, y; // x, y coordinates
        int counter = 0; // counts # of cities
        ArrayList<City> returner = new ArrayList();
        while(iter.hasData()){
            // order is city, color, quarter, x, y
            cityName = iter.fetchString("City"); // i use bigdata.jar to get these values
            cityColor = iter.fetchString("Color"); // and it gets data based on column header
            q = iter.fetchInt("quarter");
            x = iter.fetchInt("x");
            y = iter.fetchInt("y");
            switch(q){
                case 1: 
                    x = (int)((x *400)/ 2010); //x is width y is height
                    y = (int)((y *500)/ 2569); // i do a proportion 
                    break; // my x/my image size = recieved x / recieved image size
                case 2: 
                    x = (int)((x *400)/ 1903);
                    y = (int)((y *500)/ 2585);
                    break;
                case 3:
                    x = (int)((x *400)/ 1985);
                    y = (int)((y *500)/ 2583);
                    break;
                case 4:
                    x = (int)((x *400)/ 1927);
                    y = (int)((y *500)/ 2561);
                    break;
                default:
                    System.out.println("D'OH");
            }
            City european = new City(cityName, cityColor, q, x, y);
            //System.out.println(" " + cityName + "\t" +cityColor+ "\t" +q + "\t" +x+"\t" + y + "\t");
            returner.add(european);
            iter.loadNext();
            counter++;
        }
        System.out.println(counter);
        return returner;
    }
    
    public void setFlightDetails(Graph majorGraph, String filepath){
        // uses the .txt file tose the details for flight,
        //fx, fy, and flghtregion
        // from the file specified in the properties manager
        DataSource ds = DataSource.connectCSV(filepath);
        ds = ds.load();
        DataSourceIterator iter = ds.iterator();
        String cityname = "";
        int x, y, flightRegion;
        City ptr;
        while(iter.hasData()){
            cityname = iter.fetchString("CITY");
            x = iter.fetchInt("X");
            x = (int)((x*443)/1120);; // 
            y = iter.fetchInt("Y");
            y = (int)((y*478)/1232);; //
            flightRegion = iter.fetchInt("Section");
            ptr = majorGraph.search(cityname);
            ptr.setFCoordinates(x, y);
            ptr.setRegion(flightRegion);
            //System.out.println("city:"+ cityname +" X:"+ x +"Y:"+ y);
            iter.loadNext();
        }
    
    }
    
}
