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

    boolean haveTarget() {
        return target != null && target.getInteracting() == null;
    }

    void findTarget() {
        List<NPC> npcs = world.getSortedNPCs(npcName);

        if (npcs.size() == 0) {
            logger.info("No NPC's in range");
            return;
        }

        List<NPC> filtered = new ArrayList();
        for (NPC npc : npcs) {
            boolean isInteracting = npc.getInteracting() != null;

            if (isInteracting ||
                npc.getHealthRatio() != npc.getHealth()) {
                continue;
            }

            filtered.add(npc);
        }

        if (filtered.size() == 0) {
            logger.info("Couldn't find any targets");
            target = null;
            return;
        }

        target = filtered.get(0);
    }

    void attackTarget() {
        if (!haveTarget()) return;

        Polygon objectClickbox = target.getConvexHull();

        int distance = getWorldLocation().distanceTo(target.getWorldLocation());

        if (objectClickbox == null || distance > 5) {
            net.***REMOVED***.api.Point loc = target.getMinimapLocation();

            if (loc == null) {
                target = null;
                return;
            }

            input.click(loc.getX(), loc.getY());
            return;
        }

        Rectangle bounds = objectClickbox.getBounds();
        input.click(bounds);
    }

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
        machine.state(State.FINDING)
            .base()
            .enter(() -> {
                if (!haveFood()) return;

                findTarget();
                attackTarget();
                sleep().more();
            })
            .to(State.RETURNING).when(() -> moving.distanceTo(FIGHT) > 30)
            .to(State.BANKING).when(() -> !haveFood())
            .to(State.FIGHTING).when(() -> isInCombat());

        machine.state(State.BANKING)
            .enter(() -> {
                if (moving.closeTo(BANK)) {
                    bank.openBank();
                    bank.depositEverything();
                    sleep().some();
                    bank.withdrawAll(ItemID.SHRIMPS);
                    return;
                }

                go(BANK);
                sleep().more();
            })
            .to(State.RETURNING).when(() -> haveFood());

        machine.state(State.RETURNING)
            .enter(() -> {
                go(FIGHT);
            })
            .finish(State.FINDING);

        machine.state(State.FIGHTING)
            .enter(() -> {
                sleep().some();

                if (shouldEat()) eat();
            })
            .to(State.FINDING).when(() -> !isInCombat());

        machine.start();
    }
}
