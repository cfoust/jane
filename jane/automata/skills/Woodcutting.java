package com.sqweebloid.jane.automata.skills;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.Item;
import net.***REMOVED***.api.ItemID;
import net.***REMOVED***.api.NPC;
import net.***REMOVED***.api.coords.WorldPoint;
import net.***REMOVED***.api.queries.InventoryWidgetItemQuery;
import net.***REMOVED***.api.widgets.WidgetItem;

import com.sqweebloid.jane.Constants;
import com.sqweebloid.jane.automata.Automaton;
import com.sqweebloid.jane.automata.Loadout;

/**
 * Trains woodcutting.
 */
public class Woodcutting extends Automaton {
    private final int TREE_ID = 1276;

    private enum State {
        FINDING,
        CUTTING,
    };

    private boolean isCutting() {
        return client.getLocalPlayer().getAnimation() == 879;
    }

    @Override
    public void run() {
        Loadout gathering = new Loadout();
        gathering.hasFreeSpots(1);

        machine.state(State.FINDING)
            .base()
            .enter(() -> {
                ensure(gathering);
                object(TREE_ID).interact();
            })
            .to(State.CUTTING).when(() -> isCutting());

        machine.state(State.CUTTING)
            .enter(() -> {
                sleep().most();
            })
            .to(State.FINDING).when(() -> !isCutting());

        machine.start();
    }
}
