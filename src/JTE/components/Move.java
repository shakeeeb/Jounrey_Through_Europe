/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.components;

/**
 *
 * @author saleh
 */
public class Move {
    private Player player;
    private City to;
    private City from;
    
    public Move(Player p, City from, City to){
        this.player = p;
        this.to = to;
        this.from = from;
    }
    
    public Player getPlayer(){
        return this.player;
    }
    
    public City getTo(){
        return to;
    }
    
    public City getFrom(){
        return from;
    }
    @Override
    public String toString(){
        String returner = (player.toString()) + " moved from "+from.getName() +" to "+ to.getName()+"\n";
        return returner;
    }
}
