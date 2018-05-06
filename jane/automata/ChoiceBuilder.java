package com.sqweebloid.jane.automata;

public class ChoiceBuilder extends Builder {
    private Choice getChoice() {
        return (Choice) automaton;
    }

    public ChoiceBuilder(int option) {
        automaton = new Choice();
        getChoice().setOption(option);
    }
}

