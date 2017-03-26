
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Shan
 */
public class PathFinder {

    /**
     * Retraces the path from startNode to the endNode
     * @param startNode begin
     * @param endNode destination
     * @param gridSize size of the node grid
     */
    public void backtracePath(Node startNode, Node endNode, int gridSize) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;

        // retrace till the start node is found
        while (currentNode != startNode) {
            path.add(currentNode);
            currentNode = currentNode.parent;
        }
        
        // visualize the traced path on the grid
        StdDraw.setPenColor(StdDraw.BLUE);
        for (int i = 1; i < path.size(); i++) {
            Node temp = path.get(i);
            StdDraw.filledSquare(temp.position_Y, gridSize - temp.position_X - 1, 0.5);;
        }

        for (Node step : path) {
            StdOut.println("i: " + step.position_X + "j: " + step.position_Y);
        }
    }

    /**
     * Calculates Manhattan distance between two nodes
     *
     * @param node_1 first node
     * @param node_2 second node
     * @return Manhattan distance between the two nodes
     */
    public int getManhattanDistance(Node node_1, Node node_2) {
        int distance_X = Math.abs(node_1.position_X - node_2.position_X);
        int distance_Y = Math.abs(node_1.position_Y - node_2.position_Y);

        if (distance_X > distance_Y) {
            return 2 * distance_Y + (distance_X - distance_Y);
        }
        return 2 * distance_X + (distance_Y - distance_X);
    }

    /**
     * Gets the set of neighbors around a given node
     *
     * @param node the center node
     * @param grid grid of nodes
     * @return neighbors of the given node
     */
    public List<Node> getNeighbours(Node node, Node[][] grid) {
        int gridSize = grid.length;

        // why List =  usually there'll be 8, but could be fewer as well
        List<Node> neighbours = new ArrayList<Node>();

        // searching neighbours around the 3 * 3 hypotheical block around the given node
        for (int x = -1; x <= 1; x++) { // looping columns
            for (int y = -1; y <= 1; y++) { // looping rows
                if (x == 0 && y == 0) { // skip the position of the given node
                    continue;
                }

                // to check whether the actual position is in range of the grid by adding the hypothetical positin it
                int check_X = node.position_X + x;
                int check_Y = node.position_Y + y;

                // check if the node lies within the range of the grid
                if (check_X >= 0 && check_X < gridSize && check_Y >= 0 && check_Y < gridSize) {

                    // find the position of the neigbour from the grid
                    for (Node[] column : grid) { // looping columns
                        for (Node row : column) { // looping grids
                            if (row.position_X == check_X && row.position_Y == check_Y) { // position found
                                Node tempNode = row;
                                neighbours.add(tempNode);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return neighbours;
    }

    /**
     * Finds the shortest path between two grid points (nodes) of a given grid
     *
     * @param startNode where to start
     * @param endNode where to go
     * @param grid the grid of nodes
     */
    public void findPath(Node startNode, Node endNode, Node[][] grid) {
        int gridSize = grid.length;

        List<Node> openSet = new ArrayList<Node>(); // nodes to be evaluated
        HashSet<Node> closedSet = new HashSet<Node>(); // nodes that are already evaluated

        openSet.add(startNode); // initially the open set will only have the startNode

        while (!openSet.isEmpty()) { // evaluating each node in open set
            Node currentNode = openSet.get(0); // the node with lowest f_cost, initially the first element

            // iteration starts from the 2nd node 
            // since the first element is currently the currentNode
            for (int i = 1; i < openSet.size(); i++) {
                if (openSet.get(i).f_cost() < currentNode.f_cost() // select the one with lowest f_cost

                        // select the one with lowest h_cost if the f_cost is equal
                        || openSet.get(i).f_cost() == currentNode.f_cost()
                        && openSet.get(i).h_cost < currentNode.h_cost) {
                    currentNode = openSet.get(i);
                }
            }

            openSet.remove(currentNode);
            closedSet.add(currentNode);

            if (currentNode == endNode) { // destination node is found
                backtracePath(startNode, endNode, gridSize); // trace the path
                return; // exit the while loop
            }

            for (Node neighbour : getNeighbours(currentNode, grid)) {

                // skip thw iteration if the neigbor is a blocked cell or already traversed one
                if (!neighbour.isTraversable || closedSet.contains(neighbour)) {
                    continue;
                }

                int newMovementCostToNeighbour = currentNode.g_cost + getManhattanDistance(currentNode, neighbour);
                if (newMovementCostToNeighbour < neighbour.g_cost || !openSet.contains(neighbour)) {
                    neighbour.g_cost = newMovementCostToNeighbour;
                    neighbour.h_cost = getManhattanDistance(neighbour, endNode);
                    neighbour.parent = currentNode;

                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour);
                    }
                }
            }
        }
    }

    // draw the N-by-N node matrix to standard draw, including the points A (x1, y1) and B (x2,y2) to be marked by a circle
    public static void showGrid(Node[][] a, boolean which, int x1, int y1, int x2, int y2) {
        int N = a.length;
        StdDraw.setXscale(-1, N);;
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (a[i][j].isTraversable == which) {
                    if ((i == x1 && j == y1) || (i == x2 && j == y2)) {
                        StdDraw.circle(j, N - i - 1, .5);
                    } else {
                        StdDraw.square(j, N - i - 1, .5);
                    }
                } else {
                    StdDraw.filledSquare(j, N - i - 1, .5);
                }
            }
        }
    }

    // draw the N-by-N Node matrix to standard draw
    public static void showGrid(Node[][] a, boolean which) {
        int N = a.length;
        StdDraw.setXscale(-1, N);;
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (a[i][j].isTraversable == which) {
                    StdDraw.square(j, N - i - 1, .5);
                } else {
                    StdDraw.filledSquare(j, N - i - 1, .5);
                }
            }
        }
    }

    /**
     * Print the M-by-N array of Nodes to standard output.
     */
    public static void printGrid(Node[][] a) {
        int M = a.length;
        int N = a[0].length;
        StdOut.println(M + " " + N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (a[i][j].isTraversable) {
                    StdOut.print("1 ");
                } else {
                    StdOut.print("0 ");
                }
            }
            StdOut.println();
        }
    }

    // return a random N-by-N boolean matrix, where each entry is
    // true with probability p
    public static Node[][] random(int N, double p) {
        Node[][] a = new Node[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                a[i][j] = new Node(StdRandom.bernoulli(p), i, j);
            }
        }
        return a;
    }

    public static void main(String[] args) {
        Node[][] randommGrid = random(20, 0.9);
        printGrid(randommGrid);
        showGrid(randommGrid, true);

        Scanner in = new Scanner(System.in);
        System.out.println("Enter i for A > ");
        int Ai = in.nextInt();

        System.out.println("Enter j for A > ");
        int Aj = in.nextInt();

        System.out.println("Enter i for B > ");
        int Bi = in.nextInt();

        System.out.println("Enter j for B > ");
        int Bj = in.nextInt();

        showGrid(randommGrid, true, Ai, Aj, Bi, Bj);
        PathFinder p = new PathFinder();

        Node startNode = null;
        Node endNode = null;

        // finding the refrences of the startNode and the endNode from the grid
        for (Node[] column : randommGrid) { // looping through each column
            for (Node row : column) { // looping through each row
                if (row.position_X == Ai && row.position_Y == Aj) { // startNode is found
                    startNode = row;
                } else if (row.position_X == Bi && row.position_Y == Bj) { // endNode is found
                    endNode = row;
                }

                // exit the loop when both nodes are found
                if (startNode != null && endNode != null) {
                    break;
                }
            }
        }
        StdOut.println(startNode.position_X + " " + startNode.position_Y);
        StdOut.println(endNode.position_X + " " + endNode.position_Y);
        StdOut.println();
        StdOut.println();
        StdOut.println();

        Stopwatch timerFlow = new Stopwatch();

        p.findPath(startNode, endNode, randommGrid);

        StdOut.println("\nElapsed time = " + timerFlow.elapsedTime() + "s");
    }
}
