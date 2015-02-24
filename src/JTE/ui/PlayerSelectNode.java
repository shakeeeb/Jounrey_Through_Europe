/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.ui;

import java.awt.Color;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author saleh
 */
public class PlayerSelectNode extends FlowPane{
   ToggleGroup group = new ToggleGroup();
   RadioButton imHuman = new RadioButton("Player");
   RadioButton imAComputer = new RadioButton("Computer");
   VBox buttonGroup = new VBox();
   int n;
   String flagColor = "flag_";
   private String imgPath = "file:images/";
   TextField playerName;
   private String name;
   
   PlayerSelectNode(int n){
    this.n = n;
    imHuman.setToggleGroup(group);
    imHuman.setSelected(true);
    imAComputer.setToggleGroup(group); // use is selected to see which option is chosen here
    buttonGroup.getChildren().add(imHuman);
    buttonGroup.getChildren().add(imAComputer);
    switch(n){
                case 1: flagColor += "black.png"; 
                    break;
                case 2: flagColor += "blue.png";
                    break;
                case 3: flagColor += "green.png";
                    break;
                case 4: flagColor += "red.png";
                    break;
                case 5: flagColor += "white.png";
                    break;
                case 6: flagColor += "yellow.png";
                    break;
                default:
                    break;
            }
    Image flagImg = new Image(imgPath+flagColor);
    ImageView flag = new ImageView(flagImg); //this image is ready to be placed
            
    playerName = new TextField(this.getColor());
    
    this.getChildren().add(playerName);
    this.getChildren().add(buttonGroup);
    this.getChildren().add(flag);
   }
   
   public boolean isComputer(){
       return imAComputer.isSelected(); // this will be TRUE if hes a computer
   }
   
   public String getName(){
       return playerName.getText();
   }
   public String getColor(){
       switch(n){
                case 1: return "Black"; 
                case 2: return "Blue";
                case 3: return "Green";
                case 4: return "Red";
                case 5: return "White";
                case 6: return "Yellow";
                default:
                    break;
            }
       return "fuck";
   }
    
}
