/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Shan
 */
public class Node  {

    public boolean isTraversable;
    public int position_X;
    public int position_Y;
    public double g_cost;
    public double h_cost;
    public Node parent;

    public Node(boolean traversable, int position_X, int position_Y) {
        this.isTraversable = traversable;
        this.position_X = position_X;
        this.position_Y = position_Y;
    }

    public double f_cost() {
        return g_cost + h_cost;
    }


}
