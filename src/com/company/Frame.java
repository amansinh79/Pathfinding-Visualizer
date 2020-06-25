package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import static com.company.Algorithms.*;

class Frame {

    //Variables
    public static JPanel panel;
    public static int rectSize = 12;
    public static Node[][] board;
    public static Random random;
    public static int w, h;
    public static int pw , ph;
    public static Set<Node> visited, path, close , ddijVisited;
    public static int startx, starty , endx, endy;
    public static ArrayList<Node> open;
    public static JCheckBox diagonals;
    public static int delay;
    private static final Font font = new Font("Bold" , Font.BOLD , 14);
    public static int pathFound = 0;

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
        String[] algos = {"A*","BFS" , "Dijkstra's","DFS"};
        final JComboBox<String> algocb = new JComboBox<>(algos);
        JLabel label = new JLabel("PathFinding Algos");
        label.setForeground(Color.white);
        JLabel nodeslb = new JLabel("           Nodes");
        nodeslb.setForeground(Color.white);
        String[] nodes = {"Start" , "End" , "Wall" , "Remove Walls"};
        JComboBox<String> nodescb = new JComboBox<>(nodes);
        String[] mazealgos = {"Recursion" , "Prim's", "Random"};
        JComboBox<String> mazegencb = new JComboBox<>(mazealgos);
        JLabel gridSize = new JLabel("        Grid Size");
        gridSize.setForeground(Color.white);
        JLabel delaylb = new JLabel("           Delay");
        JLabel mazelb = new JLabel("MazeGeneration Algos");
        mazelb.setForeground(Color.white);
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
        mazegencb.setPreferredSize(new Dimension(buttonW,buttonH));
        mazelb.setPreferredSize(new Dimension(buttonW,buttonH));
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
                if(i < 0 || i > w || j < 0 || j > h)
                    return;
                int index = nodescb.getSelectedIndex();
                if(index == 0){
                    if(startx < w && starty < h)
                    board[startx][starty].setStart(false);
                    startx = i;
                    starty = j;
                    board[startx][starty].setStart(true);
                }
                else if(index == 1)
                {
                    if(endx < w && endy < h)
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
                if(i < 0 || i > w - 1|| j < 0 || j > h - 1)
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
                         pathFound = 0;
                         if(algocb.getSelectedIndex() == 0)
                             astar();
                         else if(algocb.getSelectedIndex() == 1)
                             BFS();
                         else if(algocb.getSelectedIndex() == 2)
                             dijkstra();
                         else if(algocb.getSelectedIndex() == 3)
                             DFS();
                     } catch (EndNodeFoundException e) {
                         pathFound = 1;
                         drawPath();
                     }
                     catch (StackOverflowError error){
                         pathFound = 2;
                     }
                     catch (Exception e)
                     {
                         pathFound = 3;
                        e.printStackTrace();
                     }
                     finally {
                         new PopUP(pathFound);
                     }
                 }).start();
        });
        clearpath.addActionListener(actionEvent -> clearPath());
        clearMaze.addActionListener(actionEvent -> clearMaze());
        reset.addActionListener(actionEvent -> initialize());
        GenerateMaze.addActionListener(actionEvent -> new Thread(() -> {
                if(mazegencb.getSelectedIndex() == 0) {
                        recursionDivision(0,0,w,h);
                }
                else if(mazegencb.getSelectedIndex() == 1){
                    primMazeGeneration();
                }
                else {
                    randomMaze();
                }
            board[startx][starty].setWall(false);
            board[endx][endy].setWall(false);
            f.repaint();
        }).start());
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
        buttonPanel.add(mazelb);
        buttonPanel.add(mazegencb);
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
        f.setLocationRelativeTo(null);
        f.setVisible(true);

    }

    //if frame resizes this function resets everything
    private static void initialize() {
        if(rectSize <= 12)
            board = new Node[pw / rectSize - 18][ph / rectSize - 3];
        else
            board = new Node[pw / rectSize][ph / rectSize];

        w = board.length;
        h = board[0].length;
        for (int i = 0; i < w ; i++) {
            for (int j = 0; j < h ; j++) {
                board[i][j] = new Node(i, j);
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
        ddijVisited = new HashSet<>();

        panel.repaint();
    }

    //clears path and visited nodes in grid
    private void clearPath(){
        visited = new HashSet<>();
        path = new HashSet<>();
        close = new HashSet<>();
        open = new ArrayList<>();
        ddijVisited = new HashSet<>();
        setParentNull();
        panel.repaint();
    }

    //removes all the walls in grid
    private void clearMaze(){
        for (int i = 0; i < w ; i++) {
            for (int j = 0; j < h ; j++) {
                board[i][j].setWall(false);
            }
        }
        panel.repaint();
    }

    //removes all the nodes stored in child nodes for back tracking
    private void setParentNull(){
        for (int i = 0; i < w ; i++) {
            for (int j = 0; j < h ; j++) {
                var node = board[i][j];
                node.setParent(null);
                node.setF(0);
                node.setG(0);
                node.setH(0);
            }
        }
    }

    //back tracks from end node to start node
    private void drawPath() {
        ArrayList<Node> temp = new ArrayList<>();
        Node current = board[endx][endy];
                while (current != board[startx][starty]) {
                    if (current == null) {
                        return;
                    }
                    temp.add(current);
                    current = current.getParent();
            }
        temp.remove(board[startx][starty]);
        while (!temp.isEmpty())
        {
            delay();
            path.add(temp.remove(temp.size()-1));
            panel.repaint();
        }
    }


    public static void delay(){
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
