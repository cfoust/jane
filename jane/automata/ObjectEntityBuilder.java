package com.sqweebloid.jane.automata;

import net.***REMOVED***.api.Point;

import com.sqweebloid.jane.automata.Builder;

public class ObjectEntityBuilder extends EntityBuilder {
    private ObjectEntity getObject() {
        return (ObjectEntity) automaton;
    }

    public ObjectEntityBuilder(int target) {
        automaton = new ObjectEntity();
        getObject().setTarget(target);
    }

    public ObjectEntityBuilder(int... targets) {
        automaton = new ObjectEntity();
        getObject().setTarget(targets);
    }
}
