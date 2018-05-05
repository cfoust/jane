package com.sqweebloid.jane.automata;

import net.***REMOVED***.api.Point;

import com.sqweebloid.jane.automata.Builder;

public class NPCEntityBuilder extends EntityBuilder {
    private NPCEntity getNPC() {
        return (NPCEntity) automaton;
    }

    public NPCEntityBuilder(int target) {
        automaton = new NPCEntity();
        getNPC().setTarget(target);
    }

    public NPCEntityBuilder(int... targets) {
        automaton = new NPCEntity();
        getNPC().setTarget(targets);
    }

    public NPCEntityBuilder(String target) {
        automaton = new NPCEntity();
        getNPC().setTarget(target);
    }

    public void talk() {
        interact("Talk-to");
    }

    public void attack() {
        getNPC().setFightable();
        interact("Attack");
    }

    public void pickpocket() {
        interact("Pickpocket");
    }
}
