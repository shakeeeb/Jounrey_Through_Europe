package JTE.ui;

import JTE.components.Player;
import JTE.game.JTEFileLoader;
import JTE.game.JTEGameStateManager;
import application.Main;
import application.Main.JTEPropertyType;


import java.io.File;
import java.io.IOException;

import java.net.URI;
import java.net.URL;
import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import properties_manager.PropertiesManager;




/**
 *
 * @author Shakeeb Saleh
 */
public class JTEUI {

    public enum JTEUIState{
        SPLASH_SCREEN_STATE, ABOUT_SCREEN_STATE, PLAY_GAME_STATE, VIEW_HISTORY_STATE, PLAYER_SELECT_SCREEN
    }
    
    //main stage
    private Stage primaryStage;
    //main pane
    private BorderPane mainPane;
    
    //splash screen
    private ImageView splashScreenImage;
    private Label splashScreenImageLabel;
    private StackPane splashScreenPane;
    private VBox splashScreenOptions;
    
    //player select screen
    private SelectPlayerScreenController SPScontroller;
    private Scene selectScene;

    // each player selection thing must have an image of a flag
    // a checkbox group for if player is human or computer
    // a label name, and a textbox with a player name already in it
    private PlayGameScreenController PGScontroller;
    private Scene playGameScene;
    
    private AboutScreenController AScontroller;
    private Scene aboutScene;
    
    //about screen
    private FlowPane aboutPane;
    private JEditorPane aboutScreen;
    
    //game play screen
    //components:
    //lefthand side image wahtever that shows the cards, label on top that shows which player's turn it is
    // middle is the map, a canvas with an anchorpane on top
    // this anchorpane will contain buttons which signify cities
    // these city-buttons will have tooltips which show the city names, and will be placed over the
    // coordinates of the actual city.
    // because shit's gonna be resized, iv'e got to scale the coordinates
    // righthand side has about button, an image of the dice, maybe a label for bonus points
    // a buttongroup for the quadrants, a button for flight plan, a button for game history, 
    // a button for about jte, and a button for saving the game state
    // find a class that can display the cards in a hand

    
    //the stats pane
    private FlowPane historyPane;
    private JEditorPane historyScreen;
    
    //padding
    private Insets marginlessInsets;
    
    //music
    //include music data here
    
    private String imgPath = "file:images/";
    private int paneWidth;
    private int paneHeight;
    
    //include the event handler as well as the GSM
    // also include components
    private JTEEventHandler eventHandler;
    private JTEErrorHandler errorHandler;
    //private JTEFileLoader fileLoader;
    
    private JTEGameStateManager gsm;
    private FXMLLoader loader;
    
    public JTEUI(){
        gsm = new JTEGameStateManager(this);
        eventHandler = new JTEEventHandler(this);
        errorHandler = new JTEErrorHandler();
        //fileLoader = new JTEFileLoader(this);
        initMainPane();
        initSplashScreen();
    }
//    public JTEFileLoader makeFileLoader(){
//        //fileLoader = new JTEFileLoader(this);
//        return this.fileLoader;
//    }
    
    public JTEGameStateManager getGSM(){
        return this.gsm;
    }
    
    public Scene getPGSScene(){
        return playGameScene;
    }
    
    public BorderPane getMainPane(){
        return mainPane;
    }
    
    public Stage getStage(){
        return primaryStage;
    }
    
    public void setStage(Stage stage) {
        primaryStage = stage;
    }
    public JTEEventHandler getEventHandler(){
        return eventHandler;
    }
    
    public void changeWorkspace(JTEUIState uiScreen){
        switch(uiScreen){
            case PLAY_GAME_STATE:
                primaryStage.setScene(playGameScene);
                break;
            case PLAYER_SELECT_SCREEN:
                initPlayerSelectScreen();
                break;
            case VIEW_HISTORY_STATE:
                if(historyPane == null){
                    initHistoryPane();
                }
                mainPane.setCenter(historyPane);
                break;
            case ABOUT_SCREEN_STATE: // dont need this
                System.out.println("changeworkspace called for about screen");
                eventHandler.switchToAboutScreenRequest(uiScreen);
                break;
            case SPLASH_SCREEN_STATE: // idk whats going on
                //initSplashScreen();
                mainPane.setCenter(splashScreenPane);
                Scene temp = new Scene(mainPane, 800, 600);
                primaryStage.setScene(temp);
                break;
            default:
                System.out.println("WHOOPS");
                break;
        }
    }
    
    /**
     * initializes the main pane, which is the big pane where everything goes.
     * mainpane is eventually placed in primaryStage because of how main executes
     * 
     */
    public void initMainPane(){
        marginlessInsets = new Insets(5,5,5,5);
        mainPane = new BorderPane();
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        paneWidth = Integer.parseInt(props
            .getProperty(JTEPropertyType.WINDOW_WIDTH));
        paneHeight = Integer.parseInt(props
            .getProperty(JTEPropertyType.WINDOW_HEIGHT));
        mainPane.resize(paneWidth, paneHeight);
        mainPane.setPadding(marginlessInsets);
    }
    
    
    
    public void initAboutPane() { // initalize the about pane usng fxml
    try{
        loader = new FXMLLoader(getClass().getResource("aboutScreen.fxml"));
        aboutScene = new Scene(loader.load());
        AScontroller = loader.<AboutScreenController>getController();
    } catch (IOException e){e.printStackTrace(); }
    }
    
    public void setAboutScreen(JTEUIState uiScreen){ // passes the location of where about screen is called
        if(aboutScene == null){
                    initAboutPane();
        }
        AScontroller.initData(this, uiScreen);
        primaryStage.setScene(aboutScene);
    }

    public void initHistoryPane() { // initialize the hstory pane usng fxml
        
    }
    
    /**
     * initializes the splash screen, the first place the user interacts with
     */
    public void initSplashScreen(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String splashScreenImagePath = props
                .getProperty(JTEPropertyType.SPLASH_SCREEN_IMAGE_NAME);
        // if you wanna play some welcome music, this si the place to do so
        
        //grab the splash screen image ad place it onto the splash screen
        splashScreenPane = new StackPane();
        Image splashScreenPic = loadImage(splashScreenImagePath, 800, 600);
        splashScreenImage = new ImageView(splashScreenPic);
        splashScreenImageLabel = new Label();
        splashScreenImageLabel.setGraphic(splashScreenImage);
        splashScreenImageLabel.setAlignment(Pos.CENTER);
        splashScreenPane.getChildren().add(splashScreenImageLabel);
        
        // now make the group of buttons
        splashScreenOptions = new VBox();
        splashScreenOptions.setAlignment(Pos.BOTTOM_CENTER);
        //the splash screen has 4 buttons: Start, Load, About, and Quit
        Button startButton = initButton(JTEPropertyType.START_IMG_NAME);
        splashScreenOptions.getChildren().add(startButton);
        startButton.setOnAction((ActionEvent event) -> {eventHandler.
                respondToSwitchScreenRequest(JTEUIState.PLAYER_SELECT_SCREEN);});
        
        Button loadButton = initButton(JTEPropertyType.LOAD_IMG_NAME);
        splashScreenOptions.getChildren().add(loadButton);
        loadButton.setOnAction((ActionEvent event) -> {eventHandler.
                respondToLoadGameRequest();});
        
        Button aboutButton = initButton(JTEPropertyType.ABOUT_IMG_NAME);
        splashScreenOptions.getChildren().add(aboutButton);
        aboutButton.setOnAction((ActionEvent event) -> {eventHandler.
                switchToAboutScreenRequest(JTEUIState.SPLASH_SCREEN_STATE);});
        
        Button exitButton = initButton(JTEPropertyType.EXIT_IMG_NAME);
        splashScreenOptions.getChildren().add(exitButton);
        exitButton.setOnAction((ActionEvent event) -> {eventHandler.
                respondToExitRequest();});
        
        //four buttons on the splash screen are done
        
        splashScreenPane.getChildren().add(splashScreenOptions);
        splashScreenOptions.toFront();
        mainPane.setCenter(splashScreenPane);
    }
    
    // the first time init sokoban UI is called, it is called from the FileLoader,
    // at the very end of the file loading process. 
    // I'm honestly not sure when initJTEUI should be called
    // it makes sense to see if it exists, and then call it from start,
    // so probably there, perhas even initworkspace
    
    //if a game is started from the top, it has to go to the player select screen first, 
    //before actually entering into the game itself
    //whenever the user presses start, they will go into the palyer selcet screen
    // fromt eh player selcet screen wil the user go into the actual play game state
    public void initJTEUI(){
        // this is where the play game pane gets built for the very first time.
        // i'll use some fxml magic t do this shit
        try {
        loader = new FXMLLoader(getClass().getResource("playGameScreen.fxml"));
        playGameScene = new Scene(loader.load());
        PGScontroller= loader.<PlayGameScreenController>getController();
        PGScontroller.initData(this);
        //PGScontroller.giveMap();
        primaryStage.setScene(playGameScene);
        } catch (Exception e) { e.printStackTrace();}
    }
    /**
     * initPlayerSelectSCreen--
     * whenever the user presses start, he will always be taken to the player select screen
     * b/c the player select will always launch when staritng a new game
     */
    public void initPlayerSelectScreen(){
        try {
        loader = new FXMLLoader(getClass().getResource("selectPlayerScreen.fxml"));
        selectScene = new Scene(loader.load());
        SPScontroller= loader.<SelectPlayerScreenController>getController();
        SPScontroller.initData(this);
        primaryStage.setScene(selectScene);
        } catch (Exception e) { e.printStackTrace();}

    }
    
    public PlayGameScreenController getPGSController(){
        return PGScontroller;
    }
    
    public void setPlayers(ArrayList<PlayerSelectNode> optionsBox, int n){
        PlayerSelectNode node;
        for(int i = 0; i<n; i++){ 
            node = optionsBox.get(i);
            Player person = new Player(node.isComputer(), node.getName(), node.getColor());
            System.out.println(person.toString()+"\n");
            gsm.addPlayer(person);
        }
        gsm.setNumberOfPlayers(n);
        //ready to start a new game
        eventHandler.startNewGameRequest();
    }
    /**
     * initButton--
     * a helper button which makes buttons and puts images onto those buttons, and returns the button
     * @param prop
     * @return 
     */
    private Button initButton(JTEPropertyType prop){
        //load out the image name from the property type
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imageName = props.getProperty(prop);
        
        Image image = loadImage(imageName);
        ImageView imageIcon = new ImageView(image);
        Button button = new Button();
        
        button.setGraphic(imageIcon);
        button.setPadding(marginlessInsets);
        
        return button;
    }
    /**
     * loadImage -
     * loads an image from specified file, if image is in image folder.
     * loads the image true to size
     * @param imageName
     * @return 
     */
    public Image loadImage(String imageName) {
        Image img = new Image(imgPath + imageName);
        return img;
    }
    /**
     * loadImage -
     * an overloaded method that loads out an image and resizes it to fit certain dimensions
     * dimensions are sent into the method too.
     * @param imageName
     * @param height
     * @param width
     * @return 
     */
    public Image loadImage(String imageName, int height, int width){
        String imgInfo = imgPath + imageName;
        Image img = new Image(imgInfo, height, width, true, false);
        return img;
    }
    /**
     * loadButtonImage--
     * Specifically resizes an image so that it can fit on a button thumbnail
     * turns it into a 100 by 100 image
     * @param imageName
     * @return 
     */
    public Image loadButtonImage(String imageName){
        String imgInfo = imgPath + imageName;
        Image img = new Image(imgInfo, 100, 100, true, false);
        return img;
    }
    
    
}
