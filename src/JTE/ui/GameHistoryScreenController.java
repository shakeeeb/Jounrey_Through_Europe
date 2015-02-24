/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.ui;

import JTE.components.Move;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author saleh
 */
public class GameHistoryScreenController implements Initializable {
    @FXML
    private Button backButton;
    @FXML
    private Label info;
    ArrayList<Move> gameHistory;
    private PlayGameScreenController pgsc;
    private Scene PGSScene;
    private JTEUI ui;
    @FXML
    private ScrollPane swagPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public void initData(JTEUI initUI){
        ui = initUI;
        pgsc = initUI.getPGSController();
        PGSScene = ui.getPGSScene();
        this.gameHistory = ui.getGSM().getGameHistory();
        String labelString = ""; 
        for(Move m: gameHistory){
            labelString += m.toString();
        }
        info.setText(labelString);
    }

    @FXML
    private void returnToGame(MouseEvent event) {
        ui.getStage().setScene(PGSScene);
    }
    
}
