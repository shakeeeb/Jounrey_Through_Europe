/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import JTE.ui.JTEUI.JTEUIState;

/**
 * FXML Controller class
 *
 * @author saleh
 */
public class AboutScreenController implements Initializable {
    @FXML
    private WebView viewer;
    private JTEUI ui;
    @FXML
    private Button mainButton;
    private JTEEventHandler handler;
    JTEUIState from;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void initData(JTEUI initUI, JTEUIState state){
        this.ui = initUI;
        WebEngine engine = viewer.getEngine();
        handler = ui.getEventHandler();
        engine.load("http://en.wikipedia.org/wiki/Journey_Through_Europe");
        from = state;
        switch(from){
            case SPLASH_SCREEN_STATE:
                mainButton.setText("move on to the player screen");
                break;
            case PLAY_GAME_STATE:
                mainButton.setText("return to the game");
                break;
            default:
                System.out.println("D'OH!");
                break;
        }
        
    }

    @FXML
    private void returnToPrior(MouseEvent event) { // i have to get a value of where this came from somehow
        System.out.println("button pressed");
        switch(from){
            case SPLASH_SCREEN_STATE:
                this.ui.initPlayerSelectScreen();
                break;
            case PLAY_GAME_STATE:
                handler.respondToSwitchScreenRequest(from);
                break;
            default:
                System.out.println("D'OH!");
                break;
        }
    }
    
}
