package com.sqweebloid.jane.automata.tools.builders;

import com.sqweebloid.jane.automata.tools.Choice;

public class ChoiceBuilder extends Builder {
    private Choice getChoice() {
        return (Choice) automaton;
    }

    public ChoiceBuilder(int option) {
        automaton = new Choice();
        getChoice().setOption(option);
    }
}

