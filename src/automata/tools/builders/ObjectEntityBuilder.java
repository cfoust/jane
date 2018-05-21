package com.sqweebloid.jane.automata.tools.builders;

import com.sqweebloid.jane.automata.tools.ObjectEntity;

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
