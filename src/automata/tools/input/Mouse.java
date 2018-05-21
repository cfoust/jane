package com.sqweebloid.jane.automata.tools.input;

import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.Point;
import net.***REMOVED***.client.ui.ClientUI;

import com.sqweebloid.jane.automata.Automaton;

/**
 * Moves the mouse realistically.
 */
public class Mouse extends Automaton {
    @Inject
    MouseState state;

    @Inject
    private ClientUI clientUI;

    // The interval, in pixels, at which we should
    // send mouse move events.
    private static final int MIN_DISTANCE = 2;

    // The rate, in pixels per second, at which the mouse
    // should move.
    private static final int RATE = 500;

    private boolean click = false;
    private boolean right = false;
    private Point destination = new Point(0, 0);

    public void setTarget(Point destination) {
        this.destination = destination;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    /**
     * If this is a click, make it a right click.
     */
    public void setRight(boolean right) {
        click = true;
        this.right = right;
    }

    private Component getTarget() {
        return clientUI.client.getComponent(0);
    }

    private void sendMove() {
        Point current = state.getPoint();
        Component component = getTarget();

        component.dispatchEvent(new MouseEvent(component,
                    MouseEvent.MOUSE_MOVED,
                    System.currentTimeMillis(),
                    0,
                    current.getX(), current.getY(),
                    0,
                    false));
    }

    private void sendClick() {
        Point current = state.getPoint();
        Component component = getTarget();

        int button = right ? MouseEvent.BUTTON3 : MouseEvent.BUTTON1;
        int mask = button == MouseEvent.BUTTON1 ?
            MouseEvent.BUTTON1_MASK :
            MouseEvent.BUTTON3_MASK;

        component.dispatchEvent(new MouseEvent(component,
                    MouseEvent.MOUSE_PRESSED,
                    System.currentTimeMillis(),
                    mask,
                    current.getX(), current.getY(),
                    0,
                    button == MouseEvent.BUTTON3));

        sleepExact(30);

        component.dispatchEvent(new MouseEvent(component,
                    MouseEvent.MOUSE_RELEASED,
                    System.currentTimeMillis(),
                    mask,
                    current.getX(), current.getY(),
                    0,
                    button == MouseEvent.BUTTON3));
    }

    @Override
    public void run() {
        Point start = state.getPoint();

        int distance = start.distanceTo(destination);
        // me love you
        long time = (long) Math.floor(((float) distance / (float) RATE) * 1000);
        float iterations = (float) distance / (float) MIN_DISTANCE;
        float interval = (float) time / iterations;

        float deltaX = (float) (destination.getX() - start.getX()) / iterations;
        float deltaY = (float) (destination.getY() - start.getY()) / iterations;

        for (int i = 0; i < iterations; i++) {
            state.update(
                    start.getX() + (int) Math.ceil(deltaX * (i + 1)),
                    start.getY() + (int) Math.ceil(deltaY * (i + 1)));
            sendMove();
            sleepExact((long) Math.ceil(interval));
        }

        if (click) sendClick();
    }
}
