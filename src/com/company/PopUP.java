package com.company;

import javax.swing.*;
import java.awt.*;

public class PopUP{
    Font font = new Font("Bold" ,Font.ITALIC, 17);
    public PopUP(int pathFound) {
        JFrame f = new JFrame();
        JLabel label;
        String string;

        if(pathFound == 0){
            string = "No Possible Path";
        }
        else if(pathFound == 1){
            string = "Path Found!!";
        }
        else if(pathFound == 2){
            string = "StackOverFlowError";
        }
        else {
            string = "Unknown Error";
        }
        label = new JLabel(string,JLabel.CENTER);
        label.setFont(font);
        f.setSize(200,100);
        f.setBackground(Color.DARK_GRAY);
        f.add(label, JLabel.CENTER);
        f.setTitle("Result");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
