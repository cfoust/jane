package com.sqweebloid.jane.automata.input;

import net.***REMOVED***.api.Point;

import com.sqweebloid.jane.automata.Builder;

/**
 * Makes Mouse automata.
 */
public class MouseBuilder extends Builder {
    private Mouse getMouse() {
        return (Mouse) automaton;
    }

    public MouseBuilder(Point destination) {
        automaton = new Mouse();
        getMouse().setTarget(destination);
    }

    public void left() {
        getMouse().setClick(true);
        done();
    }

    public void right() {
        getMouse().setRight(true);
        done();
    }

    public void move() {
        done();
    }
}
