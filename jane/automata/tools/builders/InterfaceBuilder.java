package com.sqweebloid.jane.automata.tools.builders;

import com.sqweebloid.jane.automata.tools.Interface;

public class InterfaceBuilder extends Builder {
    private Interface getInterface() {
        return (Interface) automaton;
    }

    public InterfaceBuilder() {
        automaton = new Interface();
    }

    public void inventory() {
        getInterface().setSprite(Interface.SpriteLink.INVENTORY);
        done();
    }

    public void combat() {
        getInterface().setSprite(Interface.SpriteLink.COMBAT);
        done();
    }

    public void stats() {
        getInterface().setSprite(Interface.SpriteLink.STATS);
        done();
    }

    public void quests() {
        getInterface().setSprite(Interface.SpriteLink.QUESTS);
        done();
    }

    public void equipment() {
        getInterface().setSprite(Interface.SpriteLink.EQUIPMENT);
        done();
    }

    public void prayer() {
        getInterface().setSprite(Interface.SpriteLink.PRAYER);
        done();
    }

    public void magic() {
        getInterface().setSprite(Interface.SpriteLink.MAGIC);
        done();
    }

    public void clan() {
        getInterface().setSprite(Interface.SpriteLink.CLAN);
        done();
    }

    public void friends() {
        getInterface().setSprite(Interface.SpriteLink.FRIENDS);
        done();
    }

    public void ignore() {
        getInterface().setSprite(Interface.SpriteLink.IGNORE);
        done();
    }

    public void logout() {
        getInterface().setSprite(Interface.SpriteLink.LOG_OUT);
        done();
    }

    public void options() {
        getInterface().setSprite(Interface.SpriteLink.OPTIONS);
        done();
    }

    public void emotes() {
        getInterface().setSprite(Interface.SpriteLink.EMOTE);
        done();
    }

    public void music() {
        getInterface().setSprite(Interface.SpriteLink.MUSIC);
        done();
    }
}
