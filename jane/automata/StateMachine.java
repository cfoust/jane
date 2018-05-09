package com.sqweebloid.jane.automata;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describes a set of states and the transitions between them.
 */
public class StateMachine<T> {
	private static final Logger logger = LoggerFactory.getLogger(StateMachine.class);
    private Map<T, State<T>> states = new HashMap();
    private T currentState;

    // So that you can use a function to determine the
    // next state rather than having to do it conditionally.
    private Callable<T> stateChooser = null;

    private ExecutorService executor;

    public StateMachine(ExecutorService executor) {
        this.executor = executor;
    }

    public State<T> state(T node) {
        State s = new State(node);
        states.put(node, s);
        return s;
    }

    private boolean shouldPause = false;
    private boolean paused = false;
    private boolean shouldStop = false;

    public void setStateChooser(Callable<T> determiner) {
        stateChooser = determiner;
    }

    public void stop() {
        shouldStop = true;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        shouldPause = true;
    }

    public void resume() {
        shouldPause = false;

        synchronized (this) {
            this.notifyAll();
        }
    }

    public void checkPause() {
        synchronized (this) {
            try {
                if (shouldPause) {
                    paused = true;
                    this.wait();
                    paused = false;
                }
            } catch (Exception e) {}
        }
    }

    public T runStateChooser() {
        try {
            return stateChooser.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void start() {
        T rawState = null;
        State<T> state = null;

        if (stateChooser == null) {
            for (Map.Entry<T, State<T>> pair : states.entrySet()) {
                State<T> pairState = pair.getValue();

                if (!pairState.isStart()) continue;

                rawState = pair.getKey();
                state = pairState;
            }

            if (state == null) {
                logger.error("It looks like you never defined a base state. Returning.");
            }
        } else {
            rawState = runStateChooser();
            state = states.get(rawState);
        }

        if (state.hasOnEnter()) {
            state.getOnEnter().run();
        }

        checkPause();

        while (!state.isTerminal()) {
            if (shouldStop) {
                return;
            }

            checkPause();

            // Check a condition or just the state chooser.
            T nextState = (stateChooser == null) ? state.checkNext() : runStateChooser();

            logger.info("NEXT -> {}", nextState);

            if (nextState != rawState) {
                state = states.get(nextState);
                rawState = nextState;
            }

            checkPause();

            if (state.hasOnEnter()) {
                state.getOnEnter().run();
            }

            checkPause();

            sleep(500);
        }
    }

    protected void sleep(long delay) {
        try { Thread.sleep(delay); } catch (Exception e) {}
    }
}
