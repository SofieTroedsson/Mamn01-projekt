package com.example.sofietroedsson.tax;

/**
 * Created by sofietroedsson on 15-04-12.
 */
public class Point {

    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
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
}
