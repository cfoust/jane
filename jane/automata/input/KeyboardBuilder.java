package com.sqweebloid.jane.automata.input;

import com.sqweebloid.jane.automata.Builder;

/**
 * Makes Keyboard automata.
 */
public class KeyboardBuilder extends Builder {
    private Keyboard getKeyboard() {
        return (Keyboard) automaton;
    }

    public KeyboardBuilder(String text) {
        automaton = new Keyboard();
        getKeyboard().setText(text);
    }
}
