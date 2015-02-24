package application;

import properties_manager.PropertiesManager;
import JTE.ui.JTEUI;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * CSE 219
 * BENCHMARK 1
 *sbuid: 109239204
 * @author shakeeb saleh
 */
public class Main extends Application{
    static String PROPERTY_TYPES_LIST = "property_types.txt"; // DONT WORRY ISNT USED
    static String UI_PROPERTIES_FILE_NAME = "properties.xml";
    static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    static String DATA_PATH = "./data/"; // is already defined, doest need to be redefined
    
    /**
     * Start--
     * Whatever calls start will give it a primaryStage,
     * this primarstage is what we dress up and present to the user
     * UI is intialized with a primaryStage
     * @param PrimaryStage 
     */
    @Override
    public void start(Stage primaryStage){
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(JTEPropertyType.UI_PROPERTIES_FILE_NAME,
                    UI_PROPERTIES_FILE_NAME);
            props.addProperty(JTEPropertyType.PROPERTIES_SCHEMA_FILE_NAME,
                    PROPERTIES_SCHEMA_FILE_NAME);
            props.addProperty(JTEPropertyType.DATA_PATH.toString(),
                    DATA_PATH);
            props.loadProperties(UI_PROPERTIES_FILE_NAME,
                    PROPERTIES_SCHEMA_FILE_NAME);
            // get the loaded title and place it in the primary stage
            String title = props.getProperty(JTEPropertyType.SPLASH_SCREEN_TITLE_TEXT);
            primaryStage.setTitle(title);
            
            JTEUI root = new JTEUI();
            BorderPane mainPane = root.getMainPane();
            root.setStage(primaryStage);
            
            Scene scene = new Scene(mainPane, mainPane.getWidth(), mainPane.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Main-- this is where the application begins
     * launch will launch the application using Start
     * @param args 
     */
    public static void main(String[] args){
        launch(args);
    }
    /**
     * These Enums control the properties of this application using XML files and shit
     */
    public enum JTEPropertyType{
        /* setup file names*/
        UI_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME,
        /* DIRECTORIES FOR FILE LOADING */
        DATA_PATH, IMG_PATH,
        /* WINDOW DIMENSIONS */
        WINDOW_WIDTH, WINDOW_HEIGHT,
        /* GAME TEXT */
        SPLASH_SCREEN_TITLE_TEXT, GAME_TITLE_TEXT, GAME_SUBHEADER_TEXT, WIN_DISPLAY_TEXT, LOSE_DISPLAY_TEXT, GAME_RESULTS_TEXT, GUESS_LABEL, LETTER_OPTIONS, EXIT_REQUEST_TEXT, YES_TEXT, NO_TEXT, DEFAULT_YES_TEXT, DEFAULT_NO_TEXT, DEFAULT_EXIT_TEXT,
        /* IMAGE FILE NAMES */
        /*load, play, about, exit, (q1, q2, q3, q4,)thumbnail+canvas (red, blue, yellow, green, black, white,)game piece + flag,  */
        WINDOW_ICON, SPLASH_SCREEN_IMAGE_NAME, LOAD_IMG_NAME, EXIT_IMG_NAME, START_IMG_NAME, ABOUT_IMG_NAME, GAME_HISTORY_IMG_NAME,
        /* DATA FILE STUFF */
        GAME_FILE_NAME, HISTORY_FILE_NAME, CITIES_LIST_FILE_NAME, CITIES_XML_FILE_NAME, //depends on how i store the cities
        /* MAP SHIT */
        MAP_WIDTH, MAP_HEIGHT, Q1_IMAGE, Q1_WIDTH, Q1_HEIGHT, Q2_IMAGE, Q2_WIDTH, Q2_HEIGHT, Q3_IMAGE, Q3_WIDTH, Q3_HEIGHT, Q4_IMAGE, Q4_WIDTH, Q4_HEIGHT
    }
}
