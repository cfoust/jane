package com.sqweebloid.jane.automata.tools.input;

import javax.inject.Singleton;
import net.***REMOVED***.api.Point;

/**
 * Stores the location of the mouse.
 */
@Singleton
public class MouseState {
    private int x;
    private int y;

    public Point getPoint() {
        return new Point(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
