
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

    public void backtracePath(Node startNode, Node endNode) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;

        while (currentNode != startNode) {
            path.add(currentNode);
            currentNode = currentNode.parent;
        }
        StdDraw.setPenColor(StdDraw.BLUE);
        for (int i = 1; i < path.size(); i++) {
            Node temp = path.get(i);
            StdDraw.filledSquare(temp.position_Y, 10 - temp.position_X - 1, 0.5);;
        }

        for (Node step : path) {
            StdOut.println("i: " + step.position_X + "j: " + step.position_Y);
        }
    }

    public int getDistance(Node node_1, Node node_2) {
        int distance_X = Math.abs(node_1.position_X - node_2.position_X);
        int distance_Y = Math.abs(node_1.position_Y - node_2.position_Y);

        if (distance_X > distance_Y) {
            return 2 * distance_Y + (distance_X - distance_Y);
        }
        return 2 * distance_X + (distance_Y - distance_X);
    }

    public List<Node> getNeighbours(Node node, int gridSize, Node[][] grid) {
        List<Node> neighbours = new ArrayList<Node>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) {
                    continue;
                }

                int check_X = node.position_X + x;
                int check_Y = node.position_Y + y;

                if (check_X >= 0 && check_X < gridSize && check_Y >= 0 && check_Y < gridSize) {
                    for (Node[] column : grid) {
                        for (Node row : column) {
                            if (row.position_X == check_X && row.position_Y == check_Y) {
                                Node tempNode = row;
                                neighbours.add(tempNode);
                            }
                        }

                    }
                }
            }
        }
        return neighbours;
    }

    public void findPath(Node startNode, Node endNode, int gridSize, Node[][] grid) {
        List<Node> openSet = new ArrayList<Node>();
        HashSet<Node> closedSet = new HashSet<Node>();

        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.get(0);

            for (int i = 1; i < openSet.size(); i++) {
                if (openSet.get(i).f_cost() < currentNode.f_cost()
                        || openSet.get(i).f_cost() == currentNode.f_cost()
                        && openSet.get(i).h_cost < currentNode.h_cost) {
                    currentNode = openSet.get(i);
                }
            }

            openSet.remove(currentNode);
            closedSet.add(currentNode);

            if (currentNode == endNode) {
                backtracePath(startNode, endNode);
                return;
            }

            for (Node neighbour : getNeighbours(currentNode, gridSize, grid)) {
                if (!neighbour.isTraversable || closedSet.contains(neighbour)) {
                    continue;
                }

                int newMovementCostToNeighbour = currentNode.g_cost + getDistance(currentNode, neighbour);
                if (newMovementCostToNeighbour < neighbour.g_cost || !openSet.contains(neighbour)) {
                    neighbour.g_cost = newMovementCostToNeighbour;
                    neighbour.h_cost = getDistance(neighbour, endNode);
                    neighbour.parent = currentNode;

                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour);
                    }
                }
            }
        }
    }

    // draw the N-by-N boolean matrix to standard draw, including the points A (x1, y1) and B (x2,y2) to be marked by a circle
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

    // draw the N-by-N boolean matrix to standard draw
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
     * Print the M-by-N array of booleans to standard output.
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
        Node[][] randommGrid = random(10, 0.7);
        printGrid(randommGrid);
        showGrid(randommGrid, true);

        Stopwatch timerFlow = new Stopwatch();

        Scanner in = new Scanner(System.in);
        System.out.println("Enter i for A > ");
        int Ai = in.nextInt();

        System.out.println("Enter j for A > ");
        int Aj = in.nextInt();

        System.out.println("Enter i for B > ");
        int Bi = in.nextInt();

        System.out.println("Enter j for B > ");
        int Bj = in.nextInt();

        StdOut.println("Elapsed time = " + timerFlow.elapsedTime());

        showGrid(randommGrid, true, Ai, Aj, Bi, Bj);
        PathFinder p = new PathFinder();

        Node startNode = null;
        Node endNode = null;

        for (Node[] column : randommGrid) {
            for (Node row : column) {
                if (row.position_X == Ai && row.position_Y == Aj) {
                    startNode = row;
                } else if (row.position_X == Bi && row.position_Y == Bj) {
                    endNode = row;
                }

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
        p.findPath(startNode, endNode, randommGrid.length, randommGrid);
    }
}
