package com.sqweebloid.jane.automata;

import net.***REMOVED***.api.Point;

import com.sqweebloid.jane.automata.Builder;

abstract public class EntityBuilder extends Builder {
    private Entity getEntity() {
        return (Entity) automaton;
    }

    public EntityBuilder closest() {
        getEntity().setRandom(false);
        return this;
    }

    public EntityBuilder search() {
        getEntity().setSearch(true);
        return this;
    }

    public void examine() {
        getEntity().setInteract(false);
        getEntity().setRandom(true);
        interact("Examine");
    }

    public void interact() {
        done();
    }

    public void interact(String verb) {
        getEntity().setMenuVerb(verb);
        done();
    }
}
