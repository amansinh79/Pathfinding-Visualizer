package com.company;

import javax.swing.*;
import java.awt.*;
import static com.company.Frame.*;


//Canvas class
class MyCanvas extends JComponent {
    @Override
    public void paint(Graphics g) {
        for (int i = 0;  i/rectSize < w ; i+=rectSize) {
            for (int j = 0; j /rectSize < h ; j+=rectSize) {
                    if (board[i/rectSize][j/rectSize].isStart())
                        g.setColor(Color.green);
                    else if (board[i/rectSize][j/rectSize].isEnd())
                        g.setColor(Color.ORANGE);
                    else if (board[i/rectSize][j/rectSize].isWall())
                        g.setColor(Color.BLACK);
                    else if (path.contains(board[i/rectSize][j/rectSize]))
                        g.setColor(Color.RED);
                    else if ((visited.contains(board[i/rectSize][j/rectSize])) || (ddijVisited.contains(board[i/rectSize][j/rectSize])))
                        g.setColor(Color.CYAN);
                    else if (open.contains(board[i/rectSize][j/rectSize]))
                        g.setColor(Color.yellow);
                    else if (close.contains(board[i/rectSize][j/rectSize]))
                        g.setColor(Color.PINK);
                    else
                        g.setColor(Color.WHITE);


                g.fillRect(i, j, rectSize, rectSize);
                g.setColor(Color.BLACK);
                g.drawRect(i, j, rectSize, rectSize);
            }
        }
    }
}