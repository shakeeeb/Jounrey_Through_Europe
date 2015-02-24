/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.ui;

import JTE.components.City;
import JTE.components.GenQueue;
import JTE.components.Graph;
import JTE.components.Player;
import JTE.game.JTEGameStateManager;
import application.Main;
import application.Main.JTEPropertyType;
import java.lang.Math;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import javax.swing.ImageIcon;
import properties_manager.PropertiesManager;

/**
 * FXML Controller class
 *
 * @author saleh
 */
public class PlayGameScreenController implements Initializable {
    @FXML
    private ImageView Q1;
    @FXML
    private ImageView Q2;
    @FXML
    private ImageView Q3;
    @FXML
    private ImageView Q4;
    @FXML
    private Button gameHistoryButton;
    @FXML
    private Button aboutButton;
    @FXML
    private Button SaveButton;
    @FXML
    private Button quitGame;
    @FXML
    private StackPane hand;
    
    private JTEUI ui;
    private JTEGameStateManager gsm;
    private JTEEventHandler eventHandler;
    
    @FXML
    private BorderPane mainPane;
    @FXML
    private ImageView Map;
    int currentQuadrant=1;
    Graph myGraph;
    @FXML
    private Label CityName;
    @FXML
    private Label theX; // first label
    @FXML
    private Label theY; // second label
    private ArrayList<City> activeCities;
    int width = 400;
    int height =  500;
    @FXML
    private ImageView diceImage;
    private HashMap<Player, ImageView> playerPieces = new HashMap(); // this way, the active player and
    // teh reference to his piece are consistent
    private ImageView activePiece;
    private HashMap<City,ImageView> Q1Flags = new HashMap(); // flags on q1 etc
    private HashMap<City,ImageView> Q2Flags = new HashMap();
    private HashMap<City,ImageView> Q3Flags = new HashMap();
    private HashMap<City,ImageView> Q4Flags = new HashMap();
    private ArrayList<City> keys = new ArrayList(); // all of teh cites that have flags o them
    private StackPane picturePane = new StackPane(); 
    private boolean diceRolled = false;
    private Player activePlayer;
    //private Pane redline = new Pane();
    private ArrayList<Player> players = new ArrayList(); // iterate through this, find the quadrant
    // has current postion. current postion ahs x and y coordinates, as well as a quadrant
    @FXML
    private Label playerName;
    
    ArrayList<City> activeHand = new ArrayList();
    ArrayList<ImageView> handPictures = new ArrayList();
    @FXML
    private Button FlightMap;

    // needs aceess to the graph
    // needs access to several images
    
    // formal
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {//FUCKER DONT STARRRT SHIT
        mainPane.setMaxHeight(height);
        mainPane.setMaxWidth(width);
        picturePane.setPrefWidth(width);
        picturePane.setMaxWidth(width);
        picturePane.setPrefHeight(height);
        picturePane.setMaxHeight(height);
        picturePane.setStyle("-fx-border-color: black;");
        mainPane.getChildren().add(picturePane);// put flags on
    }
    void initData(JTEUI initUI){
        this.ui = initUI;
        this.gsm = ui.getGSM();
        this.myGraph = gsm.getGraph();
        this.eventHandler = ui.getEventHandler();
        activeCities = myGraph.loadArrayList(1);
        Image dimage = new Image("file:images/die_1.jpg");
        diceImage.setImage(dimage);
    }
    
    public void addPlayer(Player player){ // function helps set up the players 
        // gsm needs players to show them all on the map at a time
        // the active player, however, changes based on what the gsm tells the pgs
        players.add(player);
        ImageView newPlayer = player.getPiece();
        playerPieces.put(player,newPlayer); // the actve piece will always change
        // put the piece on the map as well, f ts in the rght quadrant
        ImageView flag = player.getFlag();
        // have to know whch quadrant to put my flags
        //  also have to know where these flags are going
        keys.add(player.getHome());
        switch(player.getHome().getQuadrant()){
            case 1:
                Q1Flags.put(player.getHome(), flag);//have to get it usng a city. top left an chors
                break;
            case 2:
                Q2Flags.put(player.getHome(), flag);
                break;
            case 3:
                Q3Flags.put(player.getHome(), flag);
                break;
            case 4:
                Q4Flags.put(player.getHome(), flag);
                break;
            default:
                System.out.println("wtf you noob");
                break;
        }
        //itll be lke anchorpane, set whatever, to the home's coordinates
    }
    
    public void setActivePlayer(Player player){
        activePlayer = player;
        activePiece = playerPieces.get(player); // ok here is where a players turn will start i guess
        activeHand = activePlayer.getDestinations();
        activePlayer.points = 0;
        int l = 0;
        // every time i look at a quadrant, i have to add on the card.
        handPictures.clear();
        hand.getChildren().clear();
        for(City c: activeHand){
            ImageView icon = new ImageView(c.toImage());
            Path p = new Path();
            p.getElements().add(new MoveTo(-400, 100+l)); // inital location
            p.getElements().add(new LineTo(100, 100+l)); // where you wanna go 100 by 100 
            PathTransition pt = new PathTransition(Duration.millis(1000), p, icon);
            pt.setCycleCount(1);
            //pt.setDelay(Duration.seconds(7));
            pt.play();
            handPictures.add(icon); // do that animation shit here
            icon.setTranslateY(l);
            l += 70;
            hand.getChildren().add(icon);
        }
        switchQuadrant(activePlayer.getQuadrant());
        diceRolled = false;
        playerName.setText(activePlayer.toString());
        drawRedLines();
        if(activePlayer.isCPU()){// have the computer move
            gsm.movePC(); // have the GSM do the movements, based on their own private paths
        }
        // might want to change to that players quadrant
        // mouse clicks on the cities
        //  have to do set on mouse dragged
        //picturePane.setOnMouseClicked(mouseEvent -> { // do stuff
        
        //});
        
    }
    
    public void switchQuadrant(int q){
        switch(q){
            case 1:
                Q1switch();
                break;
            case 2:
                Q2switch();
                break;
            case 3:
                Q3switch();
               break;
            case 4:
                Q4switch();
                break;
        }
    }
    /**
     * where the action happens for goToQ1
     */
    public void Q1switch(){
    //whenever a quadrant is switched to, change the current active flags
        //change the current active player pieces based on which quadrant the player is in
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String path = ("file:images/"+ props.getProperty(JTEPropertyType.Q1_IMAGE));
        Image img = new Image(path, height, width, true, false);
        System.out.println("Q1");
        picturePane.getChildren().clear();
        for(City c: keys){
            if(Q1Flags.containsKey(c)){
                ImageView view = Q1Flags.get(c);
                picturePane.getChildren().add(view);
                //view.relocate(c.getX(), c.getY());
                view.setTranslateX(c.getX()/* - view.getLayoutBounds().getMinX()*/);
                view.setTranslateY(c.getY() -15/* - view.getLayoutBounds().getMinY()*/);
                System.out.println("Flag at Q1!: "+c.getName()+" X-" +view.getTranslateX() +" Y-" + view.getTranslateY());
            }
        } 
        // set the pieces in this quadrant
        for(Player p: players){
            if(p.getQuadrant() == 1){ // if that player is in quadrant 1
                // adn he's NOT the activeplayer, i have to draw him
                // if he's n this quadrant and he IS the activeplayer
                //I'll draw him later
                if(!p.equals(activePlayer)){
                    // draw his piece
                    ImageView pie = playerPieces.get(p);
                    picturePane.getChildren().add(pie);
                    pie.setTranslateX(p.getCurrentPos().getX());
                    pie.setTranslateY(p.getCurrentPos().getY());
                }
            
            }
        }
        if(activePlayer.getQuadrant()== 1){// actve player actve piece
            picturePane.getChildren().add(activePiece);
            activePiece.setTranslateX(activePlayer.getCurrentPos().getX());
            activePiece.setTranslateY(activePlayer.getCurrentPos().getY());
        }
        activeCities = myGraph.loadArrayList(1);
        currentQuadrant = 1;
        Map.setImage(img);
    }
    /**
     * goToQ1
     * ths s the eventhandler for if you clck on the imageview
     * @param event 
     */
    @FXML
    private void goToQ1(MouseEvent event) {
        Q1switch();
        drawRedLines();
    }
    /**
     * where the action happens for goToQ2
     */
    public void Q2switch(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String path = ("file:images/" + props.getProperty(JTEPropertyType.Q2_IMAGE));
        Image img = new Image(path, height, width, true, false);
        System.out.println("Q2");
        picturePane.getChildren().clear();
        for(City c: keys){
            if(Q2Flags.containsKey(c)){
                ImageView view = Q2Flags.get(c);
                picturePane.getChildren().add(view);
                //view.relocate(c.getX(), c.getY());
                view.setTranslateX(c.getX() /*- view.getLayoutBounds().getMinX()*/);
                view.setTranslateY(c.getY()-15 /*- view.getLayoutBounds().getMinY()*/);
                System.out.println("Flag at Q2!: "+c.getName()+" X-" +view.getTranslateX() +" Y-" + view.getTranslateY());
            }
        }
        // set the pieces in this quadrant
        for(Player p: players){
            if(p.getQuadrant() == 2){ // if that player is in quadrant 1
                // adn he's NOT the activeplayer, i have to draw him
                // if he's n this quadrant and he IS the activeplayer
                //I'll draw him later
                if(!p.equals(activePlayer)){
                    // draw his piece
                    ImageView pie = playerPieces.get(p);
                    picturePane.getChildren().add(pie);
                    pie.setTranslateX(p.getCurrentPos().getX());
                    pie.setTranslateY(p.getCurrentPos().getY());
                }
            
            }
        }
        if(activePlayer.getQuadrant() == 2){// actve player actve piece
            picturePane.getChildren().add(activePiece);
            activePiece.setTranslateX(activePlayer.getCurrentPos().getX());
            activePiece.setTranslateY(activePlayer.getCurrentPos().getY());
        }
        activeCities = myGraph.loadArrayList(2);
        currentQuadrant = 2;
        Map.setImage(img);
    }
    /**
     * gotoQ2
     * this is the event handler for the clck on imageview
     * @param event 
     */
    @FXML
    private void goToQ2(MouseEvent event) {
        Q2switch();
        drawRedLines();
    }
    /**
     * this is wher the action happens for gotoQ3
     */
    public void Q3switch(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String path = ("file:images/" + props.getProperty(JTEPropertyType.Q3_IMAGE));
        Image img = new Image(path, height, width, true, false);
        System.out.println("Q3");
        picturePane.getChildren().clear();
        for(City c: keys){
            if(Q3Flags.containsKey(c)){
                ImageView view = Q3Flags.get(c);
                picturePane.getChildren().add(view);//it crashed here sometme
                //view.relocate(c.getX(), c.getY());
                view.setTranslateX(c.getX() /*- view.getLayoutBounds().getMinX()*/);
                view.setTranslateY(c.getY()-15 /*- view.getLayoutBounds().getMinY()*/);
                System.out.println("Flag at Q3!: "+c.getName()+" X-" +view.getTranslateX() +" Y-" + view.getTranslateY());
            }
        }
        // set the pieces in this quadrant
        for(Player p: players){
            if(p.getQuadrant() == 3){ // if that player is in quadrant 1
                // adn he's NOT the activeplayer, i have to draw him
                // if he's n this quadrant and he IS the activeplayer
                //I'll draw him later
                if(!p.equals(activePlayer)){
                    // draw his piece
                    ImageView pie = playerPieces.get(p);
                    picturePane.getChildren().add(pie);
                    pie.setTranslateX(p.getCurrentPos().getX());
                    pie.setTranslateY(p.getCurrentPos().getY());
                }
            
            }
        }
        if(activePlayer.getQuadrant()== 3){// actve player actve piece
            picturePane.getChildren().add(activePiece);
            activePiece.setTranslateX(activePlayer.getCurrentPos().getX());
            activePiece.setTranslateY(activePlayer.getCurrentPos().getY());
        }
        activeCities = myGraph.loadArrayList(3); // need to loadout the flags at ths quadrant using keys
        currentQuadrant = 3;
        Map.setImage(img);
    }
    @FXML
    private void goToQ3(MouseEvent event) {
        Q3switch();
        drawRedLines();
    }
    /**
     * where the action happens for gotoq4
     * @param event 
     */
    public void Q4switch(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String path =("file:images/" + props.getProperty(JTEPropertyType.Q4_IMAGE));
        Image img = new Image(path, height, width, true, false);
        System.out.println("Q4");
        picturePane.getChildren().clear();
        for(City c: keys){
            if(Q4Flags.containsKey(c)){
                ImageView view = Q4Flags.get(c);
                picturePane.getChildren().add(view);
                //view.relocate(c.getX(), c.getY());
                view.setTranslateX(c.getX() /*- view.getLayoutBounds().getMinX()*/);
                view.setTranslateY(c.getY()-15 /*- view.getLayoutBounds().getMinY()*/);
                System.out.println("Flag at Q4!: "+c.getName()+" X-" +view.getTranslateX() +" Y-" + view.getTranslateY());
            }
        }
        // set the pieces in this quadrant
        for(Player p: players){
            if(p.getQuadrant() == 4){ // if that player is in quadrant 1
                // adn he's NOT the activeplayer, i have to draw him
                // if he's n this quadrant and he IS the activeplayer
                //I'll draw him later
                if(!p.equals(activePlayer)){
                    // draw his piece
                    ImageView pie = playerPieces.get(p);
                    picturePane.getChildren().add(pie);
                    pie.setTranslateX(p.getCurrentPos().getX());
                    pie.setTranslateY(p.getCurrentPos().getY());
                }
            
            }
        }
        if(activePlayer.getQuadrant()== 4){// actve player actve piece
            picturePane.getChildren().add(activePiece);
            activePiece.setTranslateX(activePlayer.getCurrentPos().getX());
            activePiece.setTranslateY(activePlayer.getCurrentPos().getY());
        }
        activeCities = myGraph.loadArrayList(4);
        currentQuadrant = 4;
        Map.setImage(img);
    }
    @FXML
    private void goToQ4(MouseEvent event) {
        Q4switch();
        drawRedLines();
    }

    @FXML
    private void getGameHistory(MouseEvent event) {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameHistoryScreen.fxml"));
        Scene GHScene = new Scene(loader.load());
        ui.getStage().setScene(GHScene);
        GameHistoryScreenController ghsc = loader.<GameHistoryScreenController>getController();
        ghsc.initData(ui);
        } catch (Exception e){e.printStackTrace();}
    }

    @FXML
    private void goToAboutScreen(MouseEvent event) { // have to call the event handler and changeworkspace
        eventHandler.switchToAboutScreenRequest(JTEUI.JTEUIState.PLAY_GAME_STATE);
    }

    @FXML
    private void saveGame(MouseEvent event) {
        gsm.saveGame();
    }

    @FXML
    private void quitRequest(MouseEvent event) {
        System.exit(0);
    }
    /**
     * drawRedLines 
     * this draws the red lines
     * needs to be called from setactveplayer
     * as well as checkpoint, after the currentcty has been set, or even process move in the gsm itself
     */
    public void drawRedLines(){// find all the neighbors of the player.getCurrentLocation
        //land, and sea neghbors
        // make a line for every one of them that points from the current location
        // setstroke to the color red, setstroke to lke 2 or summat
        // to their location
        // place these in the picturepane
        //redline.getChildren().clear();
        ArrayList<Line> lines = new ArrayList();
        if(currentQuadrant == activePlayer.getQuadrant()){
        int sx = (int)activePlayer.getCurrentPos().getX();
        int sy = (int)activePlayer.getCurrentPos().getY();
        int ex, ey;
        for(City c: activePlayer.getCurrentPos().getNeighbors()){
            //if(activePlayer.getQuadrant() == c.getQuadrant()){
            ex = (int)c.getX();
            ey = (int)c.getY();
            Line l = new Line(sx, sy, ex, ey);
            l.setStroke(Color.RED);
            l.setStrokeWidth(2);
            lines.add(l);
            // add everythng later in it own separate for loop
            //} else { }
        }
        for(City c: activePlayer.getCurrentPos().getSeaNeighbors()){
            //if(activePlayer.getQuadrant() == c.getQuadrant()){
            ex = (int)c.getX();
            ey = (int)c.getY();
            Line l = new Line(sx, sy, ex, ey);
            l.setStroke(Color.AQUAMARINE);
            l.setStrokeWidth(2);
            lines.add(l);
            //} else { }
        }
        switchQuadrant(activePlayer.getQuadrant());
        for(Line l : lines){
            picturePane.getChildren().add(l);
            l.setTranslateX((l.getEndX()+sx)/2);
            l.setTranslateY((l.getEndY()+sy)/2);
        }
        }
        
    
    }
    
    public void moveFailed(int cx, int cy){
        Path path = new Path();
        path.getElements().add(new MoveTo(cx+100, cy+100));
        path.getElements().add(new LineTo(activePlayer.getCurrentPos().getX()+100, activePlayer.getCurrentPos().getY()+100));
                    
        PathTransition p = new PathTransition(Duration.millis(1000), path, activePiece);
        p.setCycleCount(1);
        p.play();
       activePiece.setTranslateX(activePlayer.getCurrentPos().getX());
       activePiece.setTranslateY(activePlayer.getCurrentPos().getY());
    }
/**
 * CHECKPOINT
 * checkpont is a fucking huge method
 * checkpoint actually moves a piece to a neighboring city
 * in checkpont i call the method to draw redlnes
 * @param event 
 */
    @FXML
    private void checkPoint(MouseEvent event) {
        // ok so sometmes it doesnt change the current city to the new city
        // and the point values don't change the right way
        int x2 = (int)event.getX();
        int y2 = (int)event.getY();
        move(x2, y2);
        
    }
    
    int seconds = 30;
    Timeline tl = null;
    @FXML
    private void diceRoll(MouseEvent event) { // rolls the dice
        rollDice();
    }
    
    public void rollDice(){ // oe has an event handler the other dudnt
        int k = 0;
        if(activePlayer.points == 0){ // this will always be one behind
            //diceImage.setImage(dimage); // as sugar, set the diceimage to the current number of moves left
            //Timeline tl = null;
            //int seconds = 30;
            if(tl != null){
                tl.stop();
            }
            tl = new Timeline();
            seconds = 30;
            tl.setCycleCount(Timeline.INDEFINITE);
            tl.getKeyFrames().add(
                    new KeyFrame(Duration.millis(50),
                            new EventHandler(){
                            @Override
                            public void handle(Event event){
                                seconds--;
                                // have an updatedice method
                                updateDice();
                                if(seconds <= 0){
                                    gsm.getDiceRoll(updateDice());
                                    diceRolled = true;
                                    tl.stop();
                                }
                            }
                            }
                    )
            
            ); tl.playFromStart();
        }
    }
    
    public int updateDice(){
        int j = (int)Math.ceil(Math.random()*6);
        diceImage.setImage(new Image("file:images/die_"+j+".jpg"));
        //System.out.println("j is "+ j);
        return j;
    
    }

    @FXML
    private void endTurn(MouseEvent event) {
        gsm.endTurn(); // end the turn
    }
    /**
     * CHECK
     * checks mouseover for city name
     * NOTE: 
     * check s experienceing dead zones idk why
     * @param event 
     */
    @FXML
    private void check(MouseEvent event) {
        // im pretty sure this s on the imageview itself
        // if a city is moused over, ive gotta do this
        // it's not workng on certain parts of certain panes for some odd reason
        // idk why
        // add a handler for mouseover on the pane itself
        // 
        int x = (int)event.getX();
        int y = (int)event.getY();
        String i = String.valueOf(event.getX());
        String j = String.valueOf(event.getY());
        theX.setText(i);
        theY.setText(j);
        CityName.setText(" ");
        for (City c : activeCities) {
            // loop through arraylist to match and see if anything comes close
            // have leeway of ten each time
            
            int cx = c.getX();
            int cy = c.getY();
            if(((x < cx+10) && (x>cx-10)) && ((y<cy+10)&&(y>cy-10))){
                    CityName.setText(c.getName());
                    //c.printNeighbors();
            }
            
        }
    }
    

    @FXML
    private void checkDrag(MouseEvent event) {
        // ok so sometmes it doesnt change the current city to the new city
        // and the point values don't change the right way
        int x2 = (int)event.getX();
        int y2 = (int)event.getY();
        if(diceRolled){
            activePlayer.getPiece().setTranslateX(x2);
            activePlayer.getPiece().setTranslateY(y2);
        for (City c : myGraph.getAllCities()) { // i need to translate across quadrants
            // loop through arraylist to match and see if anything comes close
            // have leeway of ten each time
            // this is only for city c in the current quadrant of active cities
            // move the player and have gsm process the move
            int cx = c.getX();
            int cy = c.getY();
            //System.out.println("active Player City:" + activePlayer.getCurrentPos().getName());
            if(((x2 < cx+10) && (x2>cx-10)) && ((y2<cy+10)&&(y2>cy-10))){ // if it's within the bounds of that city c
                System.out.println(c.getName()); // goes here
                // need to pick up the piece
                
                if(activePlayer.getCurrentPos().isLegalMove(c)){ // f the city is a neghbor to the active city
                    // for some reason this doesnt recognize certain cities
                    // doesnt recognize sea neghbors
                    // do a path transition
                    if(c.getQuadrant() == activePlayer.getQuadrant()){
                        // city is within the current quadrant
                        int k = activePlayer.getCurrentPos().whichTypeOfNeighbor(c);
                        activePiece.setTranslateX(x2);
                        activePiece.setTranslateY(y2);
                        System.out.println("City: "+ c.getName() + " Movement Type:" + k);
                        gsm.processMove(c, k); // gotta hadle flght not suported yet
                    } else { 
                        // you cant drag it to the next quadrant
                    }
                    //gsm.processMove(c, 1);
                }
            }
            
        } 
        } else {
            System.out.println("Dice Have Not been rolled yet!");
        }
    }
    public void redrawDice(int i){
        if((i<0)||(i>6)){
            System.out.println("richard cranium");
            return;
        }
        if(i==0){
            diceImage.setImage(new Image("file:images/die_1.jpg"));
        } else {
            diceImage.setImage(new Image("file:images/die_"+i+".jpg"));
        }
    }

    @FXML
    private void checkDragDrop(DragEvent event) {
        
    }
    
    public boolean move(City c){
        
        return move(c.getX(), c.getY());
    }
    
    public void fakeRollDice(){
        diceRolled = true;
    }
    /**
     * basically, checkpoint will call this method. this is checkpoint
     * @param x2
     * @param y2 
     */
    boolean sweg;
    public boolean move(int x2, int y2){
    sweg = false;
    if(diceRolled){
        for (City c : myGraph.getAllCities()) { // i need to translate across quadrants
            // loop through arraylist to match and see if anything comes close
            // have leeway of ten each time
            // this is only for city c in the current quadrant of active cities
            // move the player and have gsm process the move
            int cx = c.getX();
            int cy = c.getY();
            //System.out.println("active Player City:" + activePlayer.getCurrentPos().getName());
            if(((x2 < cx+10) && (x2>cx-10)) && ((y2<cy+10)&&(y2>cy-10))){ // if it's within the bounds of that city c
                System.out.println(c.getName()); // goes here
                //activePlayer.getCurrentPos().printNeighbors(); // it works with like 3 or four clicks
                // cities are getcurrentPos and c
                if(activePlayer.getCurrentPos().isLegalMove(c)){ // f the city is a neghbor to the active city
                    // for some reason this doesnt recognize certain cities
                    // doesnt recognize sea neghbors
                    // do a path transition
                    if(c.getQuadrant() == activePlayer.getQuadrant()){ // this s only f the city is 
                        // in the current quadrant
                        Path path = new Path();
                        
                        path.getElements().add(new MoveTo(activePlayer.getCurrentPos().getX()+100, activePlayer.getCurrentPos().getY()+100));
                        path.getElements().add(new LineTo(cx+100, cy+100));
                    
                        PathTransition p = new PathTransition(Duration.millis(1000), path, activePiece);
                        p.setOnFinished((ActionEvent arg0) -> {
                            int k = activePlayer.getCurrentPos().whichTypeOfNeighbor(c);
                            activePiece.setTranslateX(x2);
                            activePiece.setTranslateY(y2);
                            System.out.println("City: "+ c.getName() + " Movement Type:" + k);
                            sweg = gsm.processMove(c, k);
                        });
                        p.setCycleCount(1);
                        p.play();
                        return sweg;

                    } else { // i have to handle the case that the cty is a neighbor not of this quadrant
                        // switchQuadrant based on which quadrant its in
                        // have to process the move, and then switch the quadrant
                        // have to physcally change which thnge the playerpiece is stored in
                        int k = activePlayer.getCurrentPos().whichTypeOfNeighbor(c);
                        System.out.println("City: "+ c.getName() + " Movement Type:" + k);
                        if(gsm.processMove(c, k)){
                            switchQuadrant(c.getQuadrant());
                            return true;
                        }  
                    }
                }
            }
            
        } 
        } else {
            System.out.println("Dice Have Not been rolled yet!");
        }
    return false;
    }
    
    public void finshMove(){
    
    }

    @FXML
    private void goToFlightMap(MouseEvent event) {
        // waa gwaaan
        // pass in a Ui reference, as well as a PGS scene reference, PrimaryStage (bc i need to wipe it too)
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("flightMapScreen.fxml"));
        Scene fmsScene = new Scene(loader.load());
        ui.getStage().setScene(fmsScene);
        FlightMapScreenController fmsc = loader.<FlightMapScreenController>getController();
        fmsc.initData(ui, ui.getPGSController());
        } catch (Exception e){e.printStackTrace();}
    }
    
}
