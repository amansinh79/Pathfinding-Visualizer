package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Frame {

    //Variables
    private static JPanel panel;
    private static int rectSize = 12;
    private static Node[][] board;
    private static Random random;
    private static int w, h;
    private static int pw , ph;
    private static Set<Node> visited, path, close;
    private static int startx, starty , endx, endy;
    private static ArrayList<Node> open;
    private static JCheckBox diagonals;
    private static int delay;
    private static final Font font = new Font("Bold" , Font.BOLD , 14);

    //Constructor
    Frame() {

        //Initialization
        JFrame f = new JFrame();
        f.setLayout(new BorderLayout());
        random = new Random();
        delay = 1;
        panel = new JPanel(new GridLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        f.setSize(1200, 800);
        panel.setBackground(Color.white);
        buttonPanel.setBackground(Color.DARK_GRAY);
        panel.setPreferredSize(new Dimension(f.getWidth(),f.getHeight()));
        buttonPanel.setPreferredSize(new Dimension(200,f.getHeight()));
        panel.add(new MyCanvas());
        f.add(buttonPanel,BorderLayout.WEST);
        f.add(panel,BorderLayout.CENTER);
        pw = panel.getWidth();
        ph = panel.getHeight();
        JButton b = new JButton();
        JButton clearpath = new JButton();
        JButton clearMaze = new JButton();
        JButton reset = new JButton();
        JButton GenerateMaze = new JButton();
        diagonals = new JCheckBox("   Diagonals",true);
        JSlider gridSlider = new JSlider(4, 70, rectSize);
        JSlider delaySlider = new JSlider(0,100,delay);
        String[] algos = {"A*","Dijkstra"};
        final JComboBox<String> algocb = new JComboBox<>(algos);
        JLabel label = new JLabel("        Algorithms");
        label.setForeground(Color.white);
        JLabel nodeslb = new JLabel("           Nodes");
        nodeslb.setForeground(Color.white);
        String[] nodes = {"Start" , "End" , "Wall" , "Remove Walls"};
        JComboBox<String> nodescb = new JComboBox<>(nodes);
        JLabel gridSize = new JLabel("        Grid Size");
        gridSize.setForeground(Color.white);
        JLabel delaylb = new JLabel("           Delay");
        delaylb.setForeground(Color.white);
        nodeslb.setFont(font);
        delaylb.setFont(font);
        label.setFont(font);
        gridSize.setFont(font);

        //Height and Width for buttons
        int buttonW = 130;
        int buttonH = 30;


        //Button Positions in frame
        b.setPreferredSize(new Dimension(buttonW,buttonH));
        clearpath.setPreferredSize(new Dimension(buttonW,buttonH));
        clearMaze.setPreferredSize(new Dimension(buttonW,buttonH));
        reset.setPreferredSize(new Dimension(buttonW,buttonH));
        GenerateMaze.setPreferredSize(new Dimension(buttonW,buttonH));
        gridSize.setPreferredSize(new Dimension(buttonW,buttonH));
        gridSlider.setPreferredSize(new Dimension(buttonW,buttonH));
        label.setPreferredSize(new Dimension(buttonW,buttonH));
        algocb.setPreferredSize(new Dimension(buttonW,buttonH));
        diagonals.setPreferredSize(new Dimension(buttonW,buttonH));
        nodeslb.setPreferredSize(new Dimension(buttonW,buttonH));
        nodescb.setPreferredSize(new Dimension(buttonW,buttonH));
        delaylb.setPreferredSize(new Dimension(buttonW,buttonH));
        delaySlider.setPreferredSize(new Dimension(buttonW,buttonH));
        delaySlider.setPreferredSize(new Dimension(buttonW,buttonH));


        //Button titles
        b.setText("Visualize!");
        clearpath.setText("Clear Path");
        clearMaze.setText("Clear Maze");
        reset.setText("Reset");
        GenerateMaze.setText("Generate Maze");



        //Listeners
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = e.getX() / rectSize;
                int j = e.getY() / rectSize;
                if(i < 0 || i > h || j < 0 || j > w)
                    return;
                int index = nodescb.getSelectedIndex();
                if(index == 0){
                    if(startx < h && starty < w)
                    board[startx][starty].setStart(false);
                    startx = i;
                    starty = j;
                    board[startx][starty].setStart(true);
                }
                else if(index == 1)
                {
                    if(endx < h && endy < w)
                    board[endx][endy].setEnd(false);
                    endx = i;
                    endy = j;
                    board[endx][endy].setEnd(true);
                }
                else if(index == 2){
                    if(e.getButton() == 1)
                        board[i][j].setWall(true);
                    else if(e.getButton() == 3)
                        board[i][j].setWall(false);
                }
                else if(index == 3){
                    board[i][j].setWall(false);
                }
                    panel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

                int i = e.getX()/rectSize;
                int j = e.getY()/rectSize;
                if(i < 0 || i > h || j < 0 || j > w)
                    return;
                if(board[i][j].isStart() && board[i][j].isEnd())
                    return;

                if(nodescb.getSelectedIndex() == 2) {
                        board[i][j].setWall(true);
                }

               else if(nodescb.getSelectedIndex() == 3){
                        board[i][j].setWall(false);
                }

                f.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {}
        });
        gridSlider.addChangeListener(changeEvent -> {
            JSlider s = (JSlider) changeEvent.getSource();
            rectSize = s.getValue();
            initialize();
            panel.repaint();
        });
        b.addActionListener(actionEvent -> {
            new Thread(() -> {
                     try {
                         if(algocb.getSelectedIndex() == 0)
                             Frame.this.astar();
                         else if(algocb.getSelectedIndex() == 1)
                             Frame.this.dij();
                     } catch (EndNodeFoundException e) {
                         drawPath();
                     }
                     catch (Exception e)
                     {
                        e.printStackTrace();
                     }
                 }).start();
        });
        clearpath.addActionListener(actionEvent -> clearPath());
        clearMaze.addActionListener(actionEvent -> clearMaze());
        reset.addActionListener(actionEvent -> initialize());
        GenerateMaze.addActionListener(actionEvent -> {
            new Thread(() -> {
                try {
                    Frame.this.divide(0,0,h,w);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    board[startx][starty].setWall(false);
                    board[endx][endy].setWall(false);
                }
            }).start();

        });
        delaySlider.addChangeListener(changeEvent -> {
            JSlider s = (JSlider) changeEvent.getSource();
            delay = s.getValue();
        });
        f.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.setSize(f.getWidth(),f.getHeight());
                ph = panel.getHeight();
                pw = panel.getWidth();
                initialize();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });


        //adding stuff to Button Panel
        buttonPanel.add(b);
        buttonPanel.add(clearpath);
        buttonPanel.add(clearMaze);
        buttonPanel.add(GenerateMaze);
        buttonPanel.add(label);
        buttonPanel.add(algocb);
        buttonPanel.add(delaylb);
        buttonPanel.add(delaySlider);
        buttonPanel.add(gridSize);
        buttonPanel.add(gridSlider);
        buttonPanel.add(nodeslb);
        buttonPanel.add(nodescb);
        buttonPanel.add(diagonals);
        buttonPanel.add(reset);


        f.setTitle("PathFinding Visualizer by Aman");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }


    //if frame resizes this function resets everything
    private static void initialize() {
        board = new Node[pw/rectSize][ph/rectSize];
        h = board.length;
        w = board[0].length;
        for (int i = 0; i < h ; i++) {
            for (int j = 0; j < w ; j++) {
                board[i][j] = new Node(i, j, false, false, false);
            }
        }
        //start and end node for first time
        endx = 5;
        endy = 5;
        startx = 0;
        starty = 0;

        board[endx][endy].setEnd(true);
        board[startx][starty].setStart(true);

        visited = new HashSet<>();
        path = new HashSet<>();
        close = new HashSet<>();
        open = new ArrayList<>();

        panel.repaint();
    }

    //clears path and visited nodes in grid
    private void clearPath(){
        visited = new HashSet<>();
        path = new HashSet<>();
        close = new HashSet<>();
        open = new ArrayList<>();
        setParentNull();
        panel.repaint();
    }

    //removes all the walls in grid
    private void clearMaze(){
        for (int i = 0; i < h ; i++) {
            for (int j = 0; j < w ; j++) {
                board[i][j].setWall(false);
            }
        }
        panel.repaint();
    }

    //removes all the nodes stored in child nodes for back tracking
    private void setParentNull(){
        for (int i = 0; i < h ; i++) {
            for (int j = 0; j < w ; j++) {
                board[i][j].setParent(null);
            }
        }
    }

    //back tracks from end node to start node
    private void drawPath() {
        ArrayList<Node> temp = new ArrayList<>();
        Node current = board[endx][endy];
            try {
                while (current != board[startx][starty]) {
                    if (current.getParent() == null)
                        return;
                    current = current.getParent();
                    temp.add(current);
                }
            }
            catch (Exception e){
                System.out.println("Out of Memory");

            }
        temp.remove(board[startx][starty]);
        while (!temp.isEmpty())
        {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            path.add(temp.remove(temp.size()-1));
            panel.repaint();
        }
    }

    //recursion division maze generation
    private void divide (int x0, int y0, int w, int h) throws Exception{
        if (h < 4 || w < 4)
            return;

        Thread.sleep(delay);

        if (w < h || (w == h && random.nextBoolean())) {
            int y = y0 + 1 + random.nextInt(h - 1);

            for (int i = y0 + 1; i <= y; i++) {
                board[x0][i].setWall(true);
                panel.repaint();
            }
            int temp = ThreadLocalRandom.current().nextInt(y0 + 1, y + 1);
            board[x0][temp].setWall(false);

            divide(x0, y0, w, y - y0);
            divide(x0, y, w, h - (y - y0));
        }
        else {
            int x = x0 + 1 + random.nextInt(w - 1);
            for (int i = x0 + 1; i <= x; i++) {
                board[i][y0].setWall(true);
                panel.repaint();
            }
            int temp = ThreadLocalRandom.current().nextInt(x0+1, x + 1);
            board[temp][y0].setWall(false);

            divide(x0, y0, x - x0, h);
            divide(x, y0, w - (x - x0), h);
        }

    }


    //dijkstra's path finding algorithm
    /*
    * this algorithm visits every node until it finds end node
    * then backtracks path to start node
    * slower than a*
    */

    private void dij() throws Exception {
        Queue<Node> queue = new LinkedList<>();
        queue.add(board[startx][starty]);
        while (!queue.isEmpty()) {
            var current = queue.remove();
            dijHelper(current, queue);
        }
    }

    private void dijHelper(Node node, Queue<Node> queue) throws Exception {

        if (visited.contains(node) || node.isWall())
            return;

        if (node.isEnd())
            throw new EndNodeFoundException();

        Thread.sleep(delay);

        visited.add(node);
        panel.repaint();

        ArrayList<Node> list = getChildren(node);

        for (var child : list) {
            if (child.getParent() == null) {
                child.setParent(node);
            }
            queue.add(child);
        }
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
    private void astar() throws Exception {

        open = new ArrayList<>();

        close = new HashSet<>();

        open.add(board[startx][starty]);

        while (!open.isEmpty()) {
            var current = open.get(0);
            close.add(current);
            open.remove(current);
            Thread.sleep(delay);
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

    //here it calculates H cost,
    private double getDis(Node node) {

        double xd = (endx - node.getX());

        double yd = (endy - node.getY());

        return Math.sqrt((xd * xd) + (yd * yd));

    }

    //function to get all the neighbour nodes (diagonally or non)
    private ArrayList<Node> getChildren(Node current) {

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

                if (dx < 0 || dy < 0 || dx > board.length - 1 || dy > board[0].length - 1)
                    continue;
                ans.add(board[dx][dy]);

            }
        }

        return ans;
    }

    //Canvas class
    private static class MyCanvas extends JComponent {

        @Override
        public void paint(Graphics g) {
            for (int i = 0;  i/rectSize < h ; i+=rectSize) {
                for (int j = 0; j /rectSize < w ; j+=rectSize) {
                    if (board[i/rectSize][j/rectSize].isStart())
                        g.setColor(Color.green);
                    else if (visited.contains(board[i/rectSize][j/rectSize]))
                        g.setColor(Color.CYAN);
                    else if (open.contains(board[i/rectSize][j/rectSize]))
                        g.setColor(Color.yellow);
                    else if (board[i/rectSize][j/rectSize].isEnd())
                        g.setColor(Color.ORANGE);
                    else if (board[i/rectSize][j/rectSize].isWall())
                        g.setColor(Color.BLACK);
                    else if (close.contains(board[i/rectSize][j/rectSize]))
                        g.setColor(Color.PINK);
                    else
                        g.setColor(Color.WHITE);
                    if (path.contains(board[i/rectSize][j/rectSize]))
                        g.setColor(Color.RED);

                    g.fillRect(i, j, rectSize, rectSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(i, j, rectSize, rectSize);
                }
            }
        }
    }

}
