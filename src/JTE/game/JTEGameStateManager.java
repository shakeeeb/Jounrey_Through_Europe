


package JTE.game;

import JTE.components.City;
import JTE.components.GenQueue;
import JTE.components.Graph;
import JTE.components.Move;
import JTE.components.Player;
import JTE.ui.JTEUI;
import JTE.ui.PlayGameScreenController;
import application.Main.JTEPropertyType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import sun.reflect.generics.tree.Tree;

/**
 *
 * @author saleh
 */
public class JTEGameStateManager {
    /**
     * 
     */
    private GenQueue players;
    private Player activePlayer;
    //private enum CurrentScreen;
    //private enum gameState;
    private boolean isWon;
    private ArrayList<Move> gameHistory;
    private ArrayList<City> yellowDeck;
    private ArrayList<City> greenDeck;
    private ArrayList<City> redDeck;
    private Graph majorGraph = new Graph();
    private int numberOfPlayers;
    private boolean goAgain;
    private PlayGameScreenController PGSController;
    
    private JTEUI ui;
    private JTEFileLoader loader;
    
    private City prev; // cant go directly back to the previous city
    
    public JTEGameStateManager(JTEUI initUI){
        ui = initUI;
        loader = new JTEFileLoader(initUI, this);
        players = new GenQueue();
        gameHistory = new ArrayList();
        yellowDeck = new ArrayList();
        greenDeck = new ArrayList();
        redDeck = new ArrayList();
        
    }
    
    public void setNumberOfPlayers(int initNumberOfPlayers){
        numberOfPlayers = initNumberOfPlayers;
    }
    public Graph getGraph(){
        return majorGraph;
    }
    
    public void addPlayer(Player noob){ // called from setplayers in
        players.enqueue(noob); // i have gto access the players from  the pgs somehow
        numberOfPlayers = players.size();
    }
    /**
     * start game-
     * start game will start a new game
     * it will load all of the data from the Properties Manager using fileloader
     * the data will compiled into cities
     * those cities will be placed into the several arraylists within graph
     */
    public void startGame(){
        
    }
    
    public void changePlayer(){
    
    }
    
    public ArrayList<Move> getGameHistory(){
        return gameHistory;
    }
    
    public void deal(int x){ //deals out x cards to each person
        for(int i=0;i<x*numberOfPlayers;i++){ // n people, x number of cards
            Player pointer = (Player)players.dequeue();
            switch(((i%3)+(i/numberOfPlayers)) % 3 ){// this math works and i developed t :D
                case 0: 
                    if(i < numberOfPlayers){ // inithome
                        pointer.setHome(greenDeck.remove(1));
                        PGSController.addPlayer(pointer);
                    } else {
                        pointer.addDestination(greenDeck.remove(1));
                    }
                    break;
                case 1: 
                    if(i < numberOfPlayers){
                        pointer.setHome(yellowDeck.remove(1));
                        PGSController.addPlayer(pointer);
                    } else {
                        pointer.addDestination(yellowDeck.remove(1));
                    }
                    break;
                case 2: 
                    if(i < numberOfPlayers){
                        pointer.setHome(redDeck.remove(1));
                        PGSController.addPlayer(pointer);
                    } else {
                        pointer.addDestination(redDeck.remove(1)); 
                    }
                    break;
            }
            players.enqueue(pointer);
        }
    }
    
    public boolean processMove(City city, int type){
        // ok so here is where i process a 
        // i have to process the move based on what type of move is made
        //1 ifby land, 2 if by sea, 3 f by air
        if(activePlayer.isCPU()){ // do aseparate section for the PCS
            // becasue a PC needs to call pgsc.move from here
            // if it tres to move and the move is not legal, the turn will just end
            // it has to check ponts before the turn, becasue it wll end up looping over onto itself anyway
            // there's the case where it has to make a sea move, and i guess that will endturn
            if(activePlayer.points <= 0){ //basecase, end the turn
                endTurn();
            }
            if(activePlayer.peekDest() & (prev != null)){ // will it fuck it up? only if my computers freeze.
                endTurn();
            }
            // switch on type. can onyl handle sea and land
            if(type == 1){
                // process the move and then check if there are more ponts left
                Move m = new Move(activePlayer, activePlayer.getCurrentPos(), city);
                prev = activePlayer.getCurrentPos(); // sets prev
                activePlayer.move(city);
                PGSController.drawRedLines();
                PGSController.redrawDice(activePlayer.points);
                gameHistory.add(m);
                if(activePlayer.checkDest()){ // end the turn
                    if(activePlayer.hasWon()){
                        win();
                    }
                    activePlayer.PCsetPath(swegmaster);
                    endTurn();
                }
                if(activePlayer.points > 0){
                    City ciudad = swegmaster.remove(0);
                    PGSController.move(ciudad);
                } else {
                    activePlayer.PCsetPath(swegmaster);
                    endTurn();
                }
            } else { // f the searoute fails, have to add city to the whatever
                if(prev != null){
                    // sea travel will onyl work if it's the only thing you do ths turn
                    System.out.println("Sea Travel must be done on it's own roll");
                    swegmaster.add(0, city);
                    endTurn();
                    return false;
                }
                Move m = new Move(activePlayer, activePlayer.getCurrentPos(), city);
                prev = activePlayer.getCurrentPos();
                activePlayer.move(city);
                activePlayer.points = 0;
                gameHistory.add(m);
                if(activePlayer.checkDest()){
                    if(activePlayer.hasWon()){
                        win();
                    }
                    activePlayer.PCsetPath(swegmaster);
                    endTurn();
                }
                swegmaster.add(0,city);
                activePlayer.PCsetPath(swegmaster);
                endTurn();
            }
            return true;
        }
        if(activePlayer.points <= 0){ // if they're broke, aint worth it
            // end the shit
            endTurn();
            return false;
        } else {
            if(type == 1){ // its regular by land
                // check if it's not prev
                if(prev != null){
                    if(city.getName().equals(prev.getName())){// it is prev, not legal move
                        System.out.println("cant go back to rpevous city");
                        PGSController.moveFailed(city.getX(), city.getY());
                        return false;
                    }
                }
                Move m = new Move(activePlayer, activePlayer.getCurrentPos(), city);
                prev = activePlayer.getCurrentPos(); // sets prev
                activePlayer.move(city);
                PGSController.drawRedLines();
                PGSController.redrawDice(activePlayer.points);
                gameHistory.add(m);
                if(activePlayer.checkDest()){ // errytime  do cehckdest, i need to do haswon
                    if(activePlayer.hasWon()){
                        win();
                    }
                    if(activePlayer.isCPU()){ // pcs get special treatment
                        return false;
                    }
                    endTurn();
                    return true;
                }
                if(activePlayer.points == 0){
                    if(activePlayer.isCPU()){ // pcs get special treatment
                        return false;
                    }
                    endTurn();
                    return true;
                }
                return true;
                // generate a move and put t into game history
            } else if(type == 3){ // else it's by air
                int k = activePlayer.getCurrentPos().isLegalRegion(city.getRegion());
                if(activePlayer.points >= k){ // if its enough
                    if(prev != null){
                        if(city.getName().equals(prev.getName())){// it is prev, not legal move
                            System.out.println("cant go back to rpevous city");
                            PGSController.moveFailed(city.getX(), city.getY());
                            return false;
                        }
                    }
                    Move m = new Move(activePlayer, activePlayer.getCurrentPos(), city);
                    prev = activePlayer.getCurrentPos();
                    activePlayer.points -= k;
                    activePlayer.setCurrentPos(city);
                    PGSController.redrawDice(activePlayer.points);
                    PGSController.drawRedLines();
                    gameHistory.add(m);
                    System.out.println("BLEHH");
                } else {
                    System.out.println("not enough points. move isnt legal");
                    PGSController.moveFailed(city.getX(), city.getY());
                    return false;
                }
                if(activePlayer.checkDest()){
                    if(activePlayer.hasWon()){
                        win();
                    }
                    if(activePlayer.isCPU()){ // pcs get special treatment
                        return false;
                    }
                    endTurn();
                    return true;
                }
                if(activePlayer.points == 0){
                    if(activePlayer.isCPU()){ // pcs get special treatment
                        return false;
                    }
                    endTurn();
                    return true;
                }
                return true;
               // activePlayer.getCurrentPos().isLegalRegion(city.getQuadrant().getRegion());
            } else { // else, sea. remove all points if ts greater than 1. if not, pfft
                if(prev != null){
                    // sea travel will onyl work if it's the only thing you do ths turn
                    System.out.println("Sea Travel must be done on it's own roll");
                    PGSController.moveFailed(city.getX(), city.getY());
                    return false;
                }
                Move m = new Move(activePlayer, activePlayer.getCurrentPos(), city);
                prev = activePlayer.getCurrentPos();
                activePlayer.move(city);
                activePlayer.points = 0;
                gameHistory.add(m);
                if(activePlayer.checkDest()){
                    if(activePlayer.hasWon()){
                        win();
                    }
                    if(activePlayer.isCPU()){ // pcs get special treatment
                        return false;
                    }
                    endTurn();
                }
                endTurn();
                return true;
            }
        } 
    }
    
    ArrayList<City> swegmaster;
    public void movePC(){ // proceses the pc's move
        activePlayer.points = (int)Math.ceil(Math.random()*6);
        PGSController.fakeRollDice();
        PGSController.rollDice();
        // actually t should be able to do the first move every time, regardless
        swegmaster = activePlayer.PCgetPath();
        City ciudad = swegmaster.remove(0);
        System.out.println("City drawn:" + ciudad.toString());
        if(activePlayer.getCurrentPos().equals(ciudad)){
            ciudad = swegmaster.remove(0);
            System.out.println("city drawn:"+ciudad.toString());
        }
        PGSController.move(ciudad);
    }
    /**
     * draws a single card of a specifc color
     * @param color 
     */
    public void drawColor(String color){
    
    }
    
    public void setCPUpaths(){
        for(int i = 0;i < numberOfPlayers ;i++){
            Player p = (Player)players.dequeue();
            if(p.isCPU()){ // if its a CPU buld a path
                p.buildPaths();
            }
            players.enqueue(p);
        }
    }
    /** assists n starting a game. helps with all of the prepareation. 
     * only used at the begnnng of games
     */
    public void prepareGame(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String filename = props.getProperty(JTEPropertyType.DATA_PATH);
        filename += props.getProperty(JTEPropertyType.CITIES_LIST_FILE_NAME);
        majorGraph.setAllCities(loader.readCities(filename)); // this laods out all the cities into
        // the major graph
        loader.setNeighbors(majorGraph);
         // has access to the controller through the ui
        String flightFilename = props.getProperty(JTEPropertyType.DATA_PATH);
        flightFilename += "flightInfo.txt";
        loader.setFlightDetails(majorGraph, flightFilename);
        majorGraph.setAirports();
        PGSController = this.ui.getPGSController();
        // set the decks
        yellowDeck = majorGraph.getCitiesByColor("yellow");
        Collections.shuffle(yellowDeck);
        redDeck = majorGraph.getCitiesByColor("red");
        Collections.shuffle(redDeck);
        greenDeck = majorGraph.getCitiesByColor("green");
        Collections.shuffle(greenDeck);
        //initHomes();// i also add the player to the controller here
        deal(3);
        for(City c : majorGraph.getAllCities()){
            c.setAllNeighbors(); // set's field all neighbors, combinng sea and land neighbors
        }
        //activePlayer.buildPaths(); // setCPUPlayers
        setCPUpaths(); // sets the cpu playesr
        activePlayer = (Player)players.dequeue();
        PGSController.setActivePlayer(activePlayer);
        // i've dealt the cards
        // alot of the communcation s going to be done btwn the pgs and the gsm
        // i have to initialize the current city to the home.
        // i have to place (not just the active player, but all of them) all the game pieces
        // on their starting city
    }
    /**
     * saveGame
     * saves teh players of the game to a text file
     * activePlayer, separately from the rest of them
     */
    public void saveGame(){
        Player ptr;
        try{
            System.out.println("attempting to save game");
            FileOutputStream f = new FileOutputStream("savedata.txt");
            OutputStreamWriter opw = new OutputStreamWriter(f);
            PrintWriter writer = new PrintWriter(opw);
            //do the actve player first
            writer.println("n *"+activePlayer.toString()); // n means name
            writer.println("C *"+activePlayer.getColor()); // capital C means color
            writer.println("s *"+activePlayer.isCPU()); // s means state, for isComputer
            writer.println("p *"+activePlayer.points); // p is for points
            writer.println("P *"+activePlayer.getCurrentPos()); // capital P is for position
            writer.println("+");
            for(City c: activePlayer.getDestinations()){
                writer.println("d *"+c.getName()); // lowercase c is for city
            }
            ArrayList<Player> arr = players.toArrayList();
            for(Player p : arr){// now for the rest of the players
                // transferring them to an arraylist makes usre i dont fuck up my queue
                writer.println("n *"+p.toString()); // n means name
                writer.println("C *"+p.getColor()); // capital C means color
                writer.println("s *"+p.isCPU()); // s means state, for isComputer
                writer.println("P *"+p.getCurrentPos()); // capital P is for position
                writer.println("-");
                for(City c: p.getDestinations()){
                    writer.println("c *"+c.getName()); // lowercase c is for city
                }
            } // do the history, becasue all the players have been saved
            writer.println("&"); // splits the player section from the whatever section
            for(Move m : gameHistory){
                writer.println("u *"+m.getPlayer().toString());
                writer.println("c *"+m.getFrom().toString());
                writer.println("c *"+m.getTo().toString());
                writer.println("=");
                System.out.println("game saved.");
            }
        writer.close();
        } catch (FileNotFoundException f){System.out.println("file not found");}//f.printStackTrace();]}
    }
    
    public void resumeGame(){ // loads the data out
        try{
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String filename = props.getProperty(JTEPropertyType.DATA_PATH);
        filename += props.getProperty(JTEPropertyType.CITIES_LIST_FILE_NAME);
        majorGraph.setAllCities(loader.readCities(filename)); // this laods out all the cities into
        // the major graph
        loader.setNeighbors(majorGraph);
         // has access to the controller through the ui
        String flightFilename = props.getProperty(JTEPropertyType.DATA_PATH);
        flightFilename += "flightInfo.txt";
        loader.setFlightDetails(majorGraph, flightFilename);
        majorGraph.setAirports();
        
        System.out.println("reading out files");
        FileReader inputFile = new FileReader("./savedata.txt");
        BufferedReader b = new BufferedReader(inputFile);
        String line; char first;
        Player shell = null;
        String name = null;
        String color = null;
        boolean cpu = false;
        int points = 0;
        City pos = null;
        City city = null;
        City to = null;
        City from = null;
        boolean shift = false; // changes where the cities go, into players or moves
         while ((line = b.readLine()) != null)   {
             // do things with line
             first = line.charAt(0);
             switch(first){
                 case 'd': // d means a city that belongs to actveplaer
                     city = majorGraph.search(line.substring(line.indexOf("*")+1));
                     if(activePlayer.getDestinations().isEmpty()){
                        activePlayer.setHome(city);
                     } else {
                        activePlayer.addDestination(city);
                     }
                     break;
                 case 'n': // n is player name
                     name = line.substring(line.indexOf("*")+1);
                     break;
                 case 'C': // color
                     color = line.substring(line.indexOf("*")+1);
                     break;
                 case 's': // iscomputer
                     cpu = Boolean.parseBoolean(line.substring(line.indexOf("*")+1));
                     break;
                 case 'p': //points
                     points = Integer.parseInt(line.substring(line.indexOf("*")+1));
                     break; 
                 case 'P': // position
                     pos = majorGraph.search(line.substring(line.indexOf("*")+1));
                     break;
                 case 'c': // cityname
                     if(shift){ // its in a move, leave it be
                         if(to==null)
                             to = majorGraph.search(line.substring(line.indexOf("*")+1));
                         else
                             from = majorGraph.search(line.substring(line.indexOf("*")+1));
                     }else{ //its in a player, place it
                         city = majorGraph.search(line.substring(line.indexOf("*")+1));
                         if(shell.getDestinations().isEmpty()){
                             shell.setHome(city);
                         } else {
                         shell.addDestination(city);
                         }
                     }
                     break;
                 case '-': // process a player
                     shell = new Player(cpu, name, color);
                     shell.setCurrentPos(pos);
                     players.enqueue(shell);
                     break;
                 case 'u': // process a player as part of a move
                     shell = playerSearch(line.substring(line.indexOf("*")+1));
                     break;
                 case '&': // shift from players to moves
                     shift = true;
                     break;
                 case '=': // process a move
                     Move newm = new Move(shell, from, to);
                     gameHistory.add(newm);
                     shell = null;
                     from = null;
                     to = null;
                     break;
                 case '+': // process the activeplayer
                     activePlayer = new Player(cpu,name,color);
                     activePlayer.setCurrentPos(pos);
                     activePlayer.points = points;
                     players.enqueue(activePlayer);
                     cpu = false;
                     name = null;
                     color = null;
                     pos = null;
                     break;
             
             }} b.close();
             System.out.println("inittng jteui");

        
        
        // set the decks
        yellowDeck = majorGraph.getCitiesByColor("yellow");
        Collections.shuffle(yellowDeck);
        redDeck = majorGraph.getCitiesByColor("red");
        Collections.shuffle(redDeck);
        greenDeck = majorGraph.getCitiesByColor("green");
        Collections.shuffle(greenDeck);
        //initHomes();// i also add the player to the controller here
        ui.initJTEUI();
        PGSController = this.ui.getPGSController();
        numberOfPlayers = players.size();
        for(int i =0; i<numberOfPlayers;i++){
            Player ptr = (Player)players.dequeue();
            PGSController.addPlayer(ptr);
            players.enqueue(ptr);
        }
        //deal(3);
        for(City c : majorGraph.getAllCities()){
            c.setAllNeighbors(); // set's field all neighbors, combinng sea and land neighbors
        }
        //activePlayer.buildPaths(); // setCPUPlayers
        setCPUpaths(); // sets the cpu playesr
        activePlayer = (Player)players.dequeue();
        PGSController.setActivePlayer(activePlayer);
          
        } catch (IOException i){i.printStackTrace();}
    }
    
    public Graph getMajorGraph(){
        return majorGraph;
    }
    
    public Player playerSearch(String name){
        ArrayList<Player> arr = players.toArrayList();
        for(Player p : arr){
            if(p.toString().equals(name))
                return p;
        }
        return null;
    }
    
    public boolean getDiceRoll(int j){
        if(activePlayer.points>0){
            // doesnt process more rolls. it only processes one
            return false;
        } else {
            activePlayer.points = j; // sets the points for this particular player
            System.out.println("get dice roll points: "+activePlayer.points);
            return true;
        }
    }
    
    public Tree getPath(City city){
        return null;
    }
    
    public void endTurn(){// go to the next player
        // have to reset the points of the player
        //
        prev = null;
        if(goAgain){
        // have this player go again
        } else {
        players.enqueue(activePlayer);
        activePlayer = (Player)players.dequeue();
        resetPoints();
        PGSController.setActivePlayer(activePlayer);
        }
    }
    
    public void resetPoints(){
        activePlayer.points = 0;
    }
    public void extractConditions(){
    
    }
    public boolean isValidMove(){
        // TODO
        return false;
    }
    public void win(){
        // make a scene for winning and place it in the Ui.getPrimaryStage
        Stage winStage = new Stage();
        winStage.initModality(Modality.WINDOW_MODAL);
        BorderPane border = new BorderPane();
        System.out.println("YOU WON");
        Button okButton = new Button("ayyyyy");
        border.setBottom(okButton);
        Label messagelabel = new Label(activePlayer.toString() + " , you have just WON! Congrats");
        border.setCenter(messagelabel);
        Scene scene = new Scene(border, 200, 100);
        winStage.setScene(scene);
        winStage.show();
        okButton.setOnAction(e -> {
            //winStage.close();      
            System.exit(0);
            //victory.stop();
        });
    }
    

}
