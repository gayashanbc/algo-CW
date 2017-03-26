
import java.util.Comparator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Shan
 */
public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        // select the one with lowest f_cost
        if (o1.f_cost() < o2.f_cost()) {
            return -1;
        } else if (o1.f_cost() > o2.f_cost()) {
            return 1;

            // select the one with lowest h_cost if the f_cost is equal
        } else if (o1.h_cost < o2.h_cost) {
            return -1;
        } else if (o1.h_cost > o2.h_cost) {
            return 1;
        } else {
            return 0;
        }
    }

}
