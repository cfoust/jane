package com.sqweebloid.jane.automata.skills;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.ChatMessageType;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.ItemID;
import net.***REMOVED***.api.NPC;
import net.***REMOVED***.api.coords.LocalPoint;
import net.***REMOVED***.api.queries.InventoryWidgetItemQuery;
import net.***REMOVED***.api.widgets.WidgetItem;

import net.***REMOVED***.client.plugins.jane.JanePlugin;
import com.sqweebloid.jane.automata.Automaton;
import com.sqweebloid.jane.automata.movement.MoveGraph;
import com.sqweebloid.jane.controls.Input;

public class Combat extends Automaton {
    private String npcName;

    MoveGraph.Node BANK = MoveGraph.Node.VARROCK_WEST_BANK;
    MoveGraph.Node FIGHT = MoveGraph.Node.BARBARIAN_VILLAGE_SQUARE;

    private enum State {
        FINDING,
        BANKING,
        RETURNING,
        LOOTING,
        FIGHTING
    };

    public void setTarget(String npc) {
        npcName = npc;
        logger.info("tracking %s", npcName);
    }

    private boolean isInCombat() {
        return client.getLocalPlayer().getInteracting() != null ||
            (target != null && target.getInteracting() == getPlayer());
    }

    NPC target;

    private boolean haveFood() {
        return getFood().length > 0;
    }

    private WidgetItem[] getFood() {
        return inventory.getById(ItemID.SHRIMPS);
    }

    public float getHealthPercent() {
        return (float) getPlayer().getHealthRatio() / (float) getPlayer().getHealth();
    }

    public boolean shouldEat() {
        return getHealthPercent() < 0.7;
    }

    public void eat() {
        if (!haveFood()) return;

        WidgetItem[] food = getFood();
        input.click(food[rand(food.length)].getCanvasBounds());
    }

    @Override
    public void run() {
    }
}
