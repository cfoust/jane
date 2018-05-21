package com.sqweebloid.jane.automata.tools.builders;

import com.sqweebloid.jane.automata.tools.ItemUser;

public class ItemUserBuilder extends Builder {
    private ItemUser getItemUser() {
        return (ItemUser) automaton;
    }

    public ItemUserBuilder(int id) {
        automaton = new ItemUser();
        getItemUser().setTool(id);
    }

    public void onObject(int id) {
        getItemUser().setGameObject(id);
        done();
    }

    public void onItem(int id) {
        getItemUser().setTarget(id);
        done();
    }
}
