package com.sqweebloid.jane.automata.tools.builders;

import com.sqweebloid.jane.automata.tools.input.Keyboard;

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
