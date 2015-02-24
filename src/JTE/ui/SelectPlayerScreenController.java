/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author saleh
 */
public class SelectPlayerScreenController implements Initializable {
    @FXML
    private AnchorPane Player1;
    @FXML
    private AnchorPane Player2;
    @FXML
    private AnchorPane Player4;
    @FXML
    private AnchorPane Player5;
    @FXML
    private AnchorPane Player6;
    @FXML
    private MenuButton dropdown;
    @FXML
    private AnchorPane Player3;
    int players;
    private ArrayList<PlayerSelectNode> playerNodes = new ArrayList();
    
    private JTEUI ui;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerNodes = new ArrayList();
    }
    
    void initData(JTEUI initUi){
        this.ui = initUi;
        dropdown.setText("6");
       clearAllChildren();
       playerNodes.clear();
       PlayerSelectNode p1 = new PlayerSelectNode(1);
       Player1.getChildren().add(p1);
       playerNodes.add(p1);
       PlayerSelectNode p2 = new PlayerSelectNode(2);
       Player2.getChildren().add(p2);
       playerNodes.add(p2);
       PlayerSelectNode p3 = new PlayerSelectNode(3);
       Player3.getChildren().add(p3);
       playerNodes.add(p3);
       PlayerSelectNode p4 = new PlayerSelectNode(4);
       Player4.getChildren().add(p4);
       playerNodes.add(p4);
       PlayerSelectNode p5 = new PlayerSelectNode(5);
       Player5.getChildren().add(p5);
       playerNodes.add(p5);
       PlayerSelectNode p6 = new PlayerSelectNode(6);
       Player6.getChildren().add(p6);
       playerNodes.add(p6);
       players = 6;
    }

    @FXML
    private void setNumberOfPlayers(ActionEvent event) { 
        players = Integer.parseInt(dropdown.getText());
    }

    @FXML
    private void setPlayers(ActionEvent event) {
        
        ui.setPlayers(playerNodes, players);
    }

    @FXML
    private void TwoPlayers(ActionEvent event) {
        dropdown.setText("2"); 
        clearAllChildren();
        playerNodes.clear();
        PlayerSelectNode p1 = new PlayerSelectNode(1);
        Player1.getChildren().add(p1);
        playerNodes.add(p1);
        System.out.println("Black");
        PlayerSelectNode p2 = new PlayerSelectNode(2);
        Player2.getChildren().add(p2);
        playerNodes.add(p2);
        System.out.println("Blue");
        players = 2;
    }

    @FXML
    private void ThreePlayers(ActionEvent event) {
       dropdown.setText("3");
       clearAllChildren();
       playerNodes.clear();
       PlayerSelectNode p1 = new PlayerSelectNode(1);
       Player1.getChildren().add(p1);
       playerNodes.add(p1);
       PlayerSelectNode p2 = new PlayerSelectNode(2);
       Player2.getChildren().add(p2);
       playerNodes.add(p2);
       PlayerSelectNode p3 = new PlayerSelectNode(3);
       Player3.getChildren().add(p3);
       playerNodes.add(p3);
       players = 3;
    }

    @FXML
    private void FourPlayers(ActionEvent event) {
       dropdown.setText("4");
       clearAllChildren();
       playerNodes.clear();
       PlayerSelectNode p1 = new PlayerSelectNode(1);
       Player1.getChildren().add(p1);
       playerNodes.add(p1);
       PlayerSelectNode p2 = new PlayerSelectNode(2);
       Player2.getChildren().add(p2);
       playerNodes.add(p2);
       PlayerSelectNode p3 = new PlayerSelectNode(3);
       Player3.getChildren().add(p3);
       playerNodes.add(p3);
       PlayerSelectNode p4 = new PlayerSelectNode(4);
       Player4.getChildren().add(p4);
       playerNodes.add(p4);
       players = 4;
    }

    @FXML
    private void FivePlayers(ActionEvent event) {
       dropdown.setText("5");
       clearAllChildren();
       playerNodes.clear();
       PlayerSelectNode p1 = new PlayerSelectNode(1);
       Player1.getChildren().add(p1);
       playerNodes.add(p1);
       PlayerSelectNode p2 = new PlayerSelectNode(2);
       Player2.getChildren().add(p2);
       playerNodes.add(p2);
       PlayerSelectNode p3 = new PlayerSelectNode(3);
       Player3.getChildren().add(p3);
       playerNodes.add(p3);
       PlayerSelectNode p4 = new PlayerSelectNode(4);
       Player4.getChildren().add(p4);
       playerNodes.add(p4);
       PlayerSelectNode p5 = new PlayerSelectNode(5);
       Player5.getChildren().add(p5);
       playerNodes.add(p5);
       players = 5;
    }

    @FXML
    private void SixPlayers(ActionEvent event) {
       dropdown.setText("6");
       clearAllChildren();
       playerNodes.clear();
       PlayerSelectNode p1 = new PlayerSelectNode(1);
       Player1.getChildren().add(p1);
       playerNodes.add(p1);
       PlayerSelectNode p2 = new PlayerSelectNode(2);
       Player2.getChildren().add(p2);
       playerNodes.add(p2);
       PlayerSelectNode p3 = new PlayerSelectNode(3);
       Player3.getChildren().add(p3);
       playerNodes.add(p3);
       PlayerSelectNode p4 = new PlayerSelectNode(4);
       Player4.getChildren().add(p4);
       playerNodes.add(p4);
       PlayerSelectNode p5 = new PlayerSelectNode(5);
       Player5.getChildren().add(p5);
       playerNodes.add(p5);
       PlayerSelectNode p6 = new PlayerSelectNode(6);
       Player6.getChildren().add(p6);
       playerNodes.add(p6);
       players = 6;
    }
    
    private void clearAllChildren(){
        Player1.getChildren().clear();
        Player2.getChildren().clear();
        Player3.getChildren().clear();
        Player4.getChildren().clear();
        Player5.getChildren().clear();
        Player6.getChildren().clear();
    }
    
}
