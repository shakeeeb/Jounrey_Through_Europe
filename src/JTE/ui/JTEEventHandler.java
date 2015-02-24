/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.ui;

import JTE.game.JTEGameStateManager;

/**
 *
 * @author saleh
 */
public class JTEEventHandler {
    JTEUI ui;
    JTEGameStateManager gsm;
    
    JTEEventHandler(JTEUI ui){
        this.ui = ui;
        this.gsm = ui.getGSM();
    
    }
    
    public void respondToSwitchScreenRequest(JTEUI.JTEUIState uiState){
        System.out.println("respond to switch screen request");
        ui.changeWorkspace(uiState);
    }
    
    public void respondToLoadGameRequest(){// load a previously saved game
        gsm.resumeGame();
    }
    
    public void respondToExitRequest(){
        System.exit(0);
    }
    
    public void switchToAboutScreenRequest(JTEUI.JTEUIState uiState){ // gets the location that the aboutscreen is being called from
        ui.setAboutScreen(uiState);
    }
    
    public void startNewGameRequest(){
        // this is what starts up a fresh game
        // EXTREMELY IMPORTANT!! this will call on the GSM to start all the necessary processes
        // that take place in a game
        // but right now, all i want to do is to set up the Play Game screen
        ui.initJTEUI();// this will start up the map pane and stuff.
        // tel the GSM to start the game too
        gsm.prepareGame();
    }
}
