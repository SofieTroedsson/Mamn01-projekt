package com.example.sofietroedsson.tax;

import android.graphics.drawable.Drawable;

/**
 * Created by sofietroedsson on 15-04-12.
 */
public class Point {

    public int x, y;
    public static final int HEAD = 1;
    public static final int TAIL = 2;
    public static final int NONE = 3;
    private int type;
    private Direction direction;


    public Point(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        direction = Direction.NORTH;

    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        type = Point.NONE;
        direction = Direction.NORTH;
    }

    public boolean equals(Object other) {
        if (other instanceof Point) {
            Point p = (Point) other;
            return this.x == p.x && this.y == p.y;
        }
        return false;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public int getType() {
        return type;
    }
    public void setType(int newType){
        type = newType;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
