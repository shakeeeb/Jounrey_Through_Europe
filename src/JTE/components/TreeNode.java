/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JTE.components;

import java.util.ArrayList;

/**
 *
 * @author saleh
 */
public class TreeNode {
    
    private ArrayList<TreeNode> leaves = new ArrayList();
    private City city;
    
//    public TreeNode(){
//        super();
//    }
    public TreeNode(City c){
        this.city = c;
    }
    
    public void add(TreeNode leaf){
        leaves.add(leaf);
    }
    
    public boolean isEmpty(){
        return leaves.isEmpty();
    }
    
    public ArrayList<TreeNode> getLeaves(){
        return leaves;
    }
    
    public City getCity(){
        return city;
    }
    
}
