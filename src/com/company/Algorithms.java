package com.company;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.company.Frame.*;

public class Algorithms {

    //Todo
    //recursion division maze generation
    public static void recursionDivision(int x0 , int y0 , int w , int h) {
        if (h < 4 || w < 4)
            return;

        delay();

        if (w < h || (w == h && random.nextBoolean())) {
            int y = y0 + 1 + random.nextInt(h - 1);

            for (int i = y0 + 1; i <= y; i++) {
                board[x0][i].setWall(true);
                panel.repaint();
            }
            int temp = ThreadLocalRandom.current().nextInt(y0 + 1, y + 1);
            board[x0][temp].setWall(false);

            recursionDivision(x0, y0, w, y - y0);
            recursionDivision(x0, y, w, h - (y - y0));
        }
        else {
            int x = x0 + 1 + random.nextInt(w - 1);
            for (int i = x0 + 1; i <= x; i++) {
                board[i][y0].setWall(true);
                panel.repaint();
            }
            int temp = ThreadLocalRandom.current().nextInt(x0+1, x + 1);
            board[temp][y0].setWall(false);

            recursionDivision(x0, y0, x - x0, h);
            recursionDivision(x, y0, w - (x - x0), h);
        }

    }

    public static void randomMaze(){
        for(int i = 0 ; i < w ; i++){
            for(int j = 0 ; j < h ; j++){
                if(random.nextInt(10) < 3)
                    board[i][j].setWall(true);
            }
        }
    }

    //prim's algorithm for maze generation
    public static void primMazeGeneration() {

//        for (int i = 0; i < w; i++) {
//            for (int j = 0; j < h; j++) {
//                board[i][j].setWall(true);
//            }
//        }
//        panel.repaint();

        int x = random.nextInt(w);
        int y = random.nextInt(h);

        board[x][y].setWall(true);

        ArrayList<Node> list = new ArrayList<>();
        HashSet<Node> vis = new HashSet<>();
        list.add(board[x][y]);

        while (!list.isEmpty()) {

            var node = list.remove(random.nextInt(list.size()));

            vis.add(node);

            var children = getFrontierCells(node);

            children.removeIf(vis::contains);

            if (children.size() == 0) {
                continue;
            }
            int i = random.nextInt(children.size());
            var inb = getInBetween(node, children.get(i));

            inb.setWall(true);
            node.setWall(true);
            children.get(i).setWall(true);

            children.remove(i);

            panel.repaint();
            delay();
            list.addAll(children);
        }
    }


    //dijkstra's path finding algorithm
    /*
     * this algorithm visits every node until it finds end node
     * then backtracks path to start node.
     * it keeps unvisited nodes list sorted based on how far it is from start node.
     * slower than a*
     */

    public static void dijkstra() throws Exception{
        ArrayList<Node> list = new ArrayList<>();
        list.add(board[startx][starty]);

        while (!list.isEmpty()){
            var current = list.remove(list.size() - 1);

            dijkstraHelper(current,list);
        }


    }

    public static void BFS() throws Exception {
        Queue<Node> queue = new LinkedList<>();
        queue.add(board[startx][starty]);
        while (!queue.isEmpty()) {
            var current = queue.remove();
            BFSHelper(current, queue);
        }
    }

    public static void DFS() throws Exception{

        var current = board[startx][starty];
        DFSHelper(current);
    }


    //a* path finding algorithm
    /*
     * this algorithm actually knows where end node is,
     * based on that it calculates heuristic value knows as H cost (H cost is distance to end node from current node),
     * distance from start node to current node knows as G cost for neighbour nodes.
     * then it adds those to values and gets F Cost (G+H) for neighbour nodes.
     * it adds all neighbour nodes to open list and sorts them(with their F cost).
     * then moves to the node which has lowest F cost and calculates F cost for neighbour nodes until it gets to end node.
     */
    public static void astar() throws Exception {

        open = new ArrayList<>();

        close = new HashSet<>();

        open.add(board[startx][starty]);

        while (!open.isEmpty()) {
            var current = open.get(0);
            close.add(current);
            open.remove(current);
            delay();
            panel.repaint();
            if (current.isEnd())
                throw new EndNodeFoundException();

            ArrayList<Node> Children = getChildren(current);

            for (var child : Children) {

                if (close.contains(child) || child.isWall())
                    continue;

                child.setG(current.getG() + 1);
                child.setH(getDis(child));

                double tempF = child.getG() + child.getH();

                if(open.contains(child) && child.getF() > tempF) {
                    child.setF(tempF);
                    child.setParent(current);
                }
                else if(!open.contains(child)){
                    child.setF(tempF);
                    open.add(child);
                    child.setParent(current);
                }
                open.sort(Comparator.comparingDouble(Node::getF));
            }
        }
    }


    //helper Functions :


    //here it calculates H cost for Astar
    public static double getDis(Node node) {

        double xd = (endx - node.getX());

        double yd = (endy - node.getY());

        return Math.sqrt((xd * xd) + (yd * yd));

    }

    public static ArrayList<Node> getChildren(Node current) {

        ArrayList<Node> ans = new ArrayList<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(diagonals.isSelected()){
                    if ((j == 0 && i == 0))
                        continue;
                }
                else {
                    if ((j == 0 && i == 0) || (i == -1 && j == -1) || (i == -1 && j == 1) || (i == 1 && j == -1) || (i == 1 && j == 1))
                        continue;
                }
                int dx = current.getX() + i;
                int dy = current.getY() + j;

                if (dx < 0 || dy < 0 || dx > w - 1 || dy > h - 1 || board[dx][dy].isWall())
                    continue;
                ans.add(board[dx][dy]);

            }
        }

        return ans;
    }

    public static ArrayList<Node> getFrontierCells(Node current){
        ArrayList<Node> ans = new ArrayList<>();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if ((i == -2  && j == 0) || (i == 0  && j == -2) || (i == 2  && j == 0) || (i == 0  && j == 2))  {
                    int dx = current.getX() + i;
                    int dy = current.getY() + j;
                    if (dx < 0 || dy < 0 || dx > w - 1 || dy > h - 1)
                        continue;
                        ans.add(board[dx][dy]);
                }
            }
        }

        return ans;
    }

    private static Node getInBetween(Node randomNode, Node randomPassage) {
        int x1 = randomNode.getX();
        int y1 = randomNode.getY();
        int x2 = randomPassage.getX();
        int y2 = randomPassage.getY();

        if(x1 - x2 < 0){
            return board[x1+1][y1];
        }

        else if(x1 - x2 > 0){
            return board[x1-1][y1];
        }
        else if(y1 - y2 < 0) {
            return board[x1][y1 + 1];
        }
        else {
            return board[x1][y1 - 1];
        }

    }

    public static void BFSHelper(Node node, Queue<Node> queue) throws Exception {

        if (visited.contains(node) || node.isWall())
            return;

        if (node.isEnd())
            throw new EndNodeFoundException();

        delay();

        visited.add(node);
        panel.repaint();

        ArrayList<Node> children = getChildren(node);

        for (var child : children) {
            if (child.getParent() == null) {
                child.setParent(node);
            }
            queue.add(child);
        }
    }

    private static void dijkstraHelper(Node node, ArrayList<Node> list) throws Exception {
        if (visited.contains(node) || node.isWall())
            return;

        if (node.isEnd())
            throw new EndNodeFoundException();

        delay();

        visited.add(node);
        panel.repaint();

        ArrayList<Node> children = getChildren(node);

        for (var child : children) {
            if (child.getParent() == null) {
                child.setParent(node);
                child.setG(node.getG()+1);
            }
            list.add(child);
        }
        list.sort(Comparator.comparingDouble(Node::getG).reversed());
    }

    private static void DFSHelper(Node node) throws Exception {
        if(node.isWall() || visited.contains(node))
            return;

        if(node.isEnd())
            throw new EndNodeFoundException();

        visited.add(node);
        panel.repaint();
        delay();

        var children = getChildren(node);
        for(var child : children){
            if(child.getParent() == null)
                child.setParent(node);
                DFSHelper(child);
        }

    }


}
