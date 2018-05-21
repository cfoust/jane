package com.sqweebloid.jane.automata.tools.builders;

import com.sqweebloid.jane.automata.tools.WallEntity;

public class WallEntityBuilder extends EntityBuilder {
    private WallEntity getWall() {
        return (WallEntity) automaton;
    }

    public WallEntityBuilder(int target) {
        automaton = new WallEntity();
        getWall().setTarget(target);
    }

    public WallEntityBuilder(int... targets) {
        automaton = new WallEntity();
        getWall().setTarget(targets);
    }
}

