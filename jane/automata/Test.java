package com.sqweebloid.jane.automata;

public class Test extends Automaton {
    @Override
    public void run() {
        Loadout l = new Loadout();
        l.hasExactly(995, 2);
        l.hasExactly(555, 2);
        l.hasAtLeast(526, 1);
        ensure(l);
    }
}

