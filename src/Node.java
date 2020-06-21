package com.company;

//Node class
public class Node{
    private final int x;
    private final int y;
    private boolean isWall;
    private boolean isStart;
    private boolean isEnd;
    private Node parent;
    private double h;
    private double f;
    private double g;

    Node(int x, int y, boolean isWall, boolean isStart, boolean isEnd) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
        this.isStart = isStart;
        this.isEnd = isEnd;
    }

    boolean isWall() {
        return isWall;
    }

    void setWall(boolean wall) {
        isWall = wall;
    }

    Node getParent() {
        return parent;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }

    boolean isStart() {
        return isStart;
    }

    void setStart(boolean start) {
        isStart = start;
    }

    boolean isEnd() {
        return isEnd;
    }

    void setEnd(boolean end) {
        isEnd = end;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    double getH() {
        return h;
    }

    void setH(double h) {
        this.h = h;
    }

    double getF() {
        return f;
    }

    void setF(double f) {
        this.f = f;
    }

    double getG() {
        return g;
    }

    void setG(double g) {
        this.g = g;
    }

    @Override
    public String toString() {
        return "x=" + x +
                ", y=" + y;
    }
}
