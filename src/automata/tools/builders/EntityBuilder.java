package com.sqweebloid.jane.automata.tools.builders;

import org.someclient.api.Point;

import com.sqweebloid.jane.automata.tools.Entity;

abstract public class EntityBuilder extends Builder {
    protected Entity getEntity() {
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
