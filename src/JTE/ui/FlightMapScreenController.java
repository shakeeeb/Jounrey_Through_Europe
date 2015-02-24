/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.ui;

import JTE.components.City;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author saleh
 */
public class FlightMapScreenController implements Initializable {
    @FXML
    private Button returnButton;
    private Scene PGSScene;
    private JTEUI ui;
    private PlayGameScreenController pgsc;
    private ArrayList<City> airports;
    @FXML
    private ImageView mainImage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void initData(JTEUI ui, PlayGameScreenController pgsc){
        // pass in ui and Pgs scene from UI
        this.ui = ui;
        this.PGSScene = ui.getPGSScene();
        this.pgsc = pgsc;
        this.airports = ui.getGSM().getMajorGraph().getAirports();
    }

    @FXML
    private void checkPoint(MouseEvent event) {
        // switch to gameplay screen(change the prmaryStage scene)
        // if htey clck a valid airport city
        // and then (make a move)
        int x = (int)event.getX();
        int y = (int)event.getY();
        //System.out.println("mapx: " + x +"mapy: " + y);
        for(City c: airports){
            int cx = c.getFx();
            int cy = c.getFy();
            //System.out.println("active Player City:" + activePlayer.getCurrentPos().getName());
            if(((x < cx+10) && (x>cx-10)) && ((y<cy+10)&&(y>cy-10))){
                ui.getStage().setScene(PGSScene);
                System.out.println(c.getName());
                pgsc.move(c.getX(), c.getY());
            }
        } //System.out.println("AUUGH");
        
        // i need to make move(city)
        // i nneed to make move(x,y)
    }

    @FXML
    private void returnToGame(MouseEvent event) {
        // just set the scene again.
        ui.getStage().setScene(PGSScene);
    }
    
}
