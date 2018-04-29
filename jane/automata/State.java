package com.sqweebloid.jane.automata;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * A particular state in a state machine.
 * Evaluates an ordered set of conditions to determine
 * the next state.
 */
public class State<T> {
    // The state label of this node
    private T label;

    // Whatever state we're waiting to get a condition for
    private T making;

    private boolean hasNext = false;
    private T next;

    private boolean isEnd = false;
    private boolean isEntry = false;
    private Runnable onEnter;

    // A bit hacky, but we need an ordered list of pairs
    private List<T> states = new ArrayList();
    private List<Callable<Boolean>> conditions = new ArrayList();

    public State(T label) {
        this.label = label;
    }

    public boolean isStart() {
        return isEntry;
    }

    public boolean isTerminal() {
        return isEnd;
    }

    public T getLabel() {
        return this.label;
    }

    public boolean hasOnEnter() {
        return onEnter != null;
    }

    public boolean hasDefault() {
        return hasNext;
    }

    public T getDefault() {
        return next;
    }

    public Runnable getOnEnter() {
        return onEnter;
    }

    public State base() {
        this.isEntry = true;
        return this;
    }

    public State terminal() {
        this.isEnd = true;
        return this;
    }

    public State to(T dest) {
        making = dest;
        return this;
    }

    // The state to go to if no conditions are satisfied.
    public void finish(T dest) {
        this.next = dest;
        this.hasNext = true;
    }

    public State when(Callable<Boolean> condition) {
        states.add(making);
        conditions.add(condition);
        return this;
    }

    public State enter(Runnable body) {
        this.onEnter = body;
        return this;
    }

    public T checkNext() {
        if (hasDefault()) {
            return getDefault();
        }

        for (int i = 0; i < states.size(); i++) {
            T key = states.get(i);
            Callable<Boolean> conditional = conditions.get(i);

            boolean result;
            try {
                result = conditional.call();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            if (!result) continue;

            return key;
        }

        return this.label;
    }

    @Override
    public String toString() {
        return label.toString();
    }
}
