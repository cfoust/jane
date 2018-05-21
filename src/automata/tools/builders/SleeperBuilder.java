package com.sqweebloid.jane.automata.tools.builders;

import com.sqweebloid.jane.automata.tools.Sleeper;

public class SleeperBuilder extends Builder {
    private Sleeper getSleeper() {
        return (Sleeper) automaton;
    }

    public SleeperBuilder() {
        automaton = new Sleeper();
    }

    public void briefly() {
        getSleeper().setLength(Sleeper.Length.BRIEFLY);
        done();
    }

    public void some() {
        getSleeper().setLength(Sleeper.Length.SOME);
        done();
    }

    public void more() {
        getSleeper().setLength(Sleeper.Length.MORE);
        done();
    }

    public void most() {
        getSleeper().setLength(Sleeper.Length.MOST);
        done();
    }

    // Pretend like the human went and did something else.
    public void andGetUp() {
        getSleeper().setLength(Sleeper.Length.BREAK);
        done();
    }
}
