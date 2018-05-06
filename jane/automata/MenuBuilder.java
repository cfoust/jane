package com.sqweebloid.jane.automata;

public class MenuBuilder extends Builder {
    private Menu getMenu() {
        return (Menu) automaton;
    }

    public MenuBuilder(String verb) {
        automaton = new Menu();
        getMenu().setVerb(verb);
    }

    public void target(String target) {
        getMenu().setTarget(target);
        done();
    }
}
