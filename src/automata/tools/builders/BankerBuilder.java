package com.sqweebloid.jane.automata.tools.builders;

import com.sqweebloid.jane.automata.tools.Banker;

public class BankerBuilder extends Builder {
    private Banker getBanker() {
        return (Banker) automaton;
    }

    public BankerBuilder(int slot) {
        automaton = new Banker();
        getBanker().setSlot(slot);
    }

    public BankerBuilder() {
        automaton = new Banker();
        getBanker().setJustOpen(true);
    }

    public void withdraw(int amount) {
        getBanker().setAmount(amount);
        getBanker().setInto(false);
        done();
    }

    public void withdrawAll() {
        getBanker().setAll();
        getBanker().setInto(false);
        done();
    }

    public void depositAll() {
        getBanker().setAll();
        getBanker().setInto(true);
        done();
    }

    public void deposit(int amount) {
        getBanker().setAmount(amount);
        getBanker().setInto(true);
        done();
    }
}
