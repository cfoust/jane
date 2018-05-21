package com.sqweebloid.jane.automata.tools.builders;

import org.someclient.api.Point;

import com.sqweebloid.jane.automata.tools.input.Mouse;

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
