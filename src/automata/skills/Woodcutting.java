package com.sqweebloid.jane.automata.skills;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.List;
import javax.inject.Inject;
import org.someclient.api.Item;
import org.someclient.api.ItemID;
import org.someclient.api.NPC;
import org.someclient.api.coords.WorldPoint;
import org.someclient.api.queries.InventoryWidgetItemQuery;
import org.someclient.api.widgets.WidgetItem;

import com.sqweebloid.jane.Constants;
import com.sqweebloid.jane.automata.Automaton;
import com.sqweebloid.jane.automata.Loadout;

/**
 * Trains woodcutting.
 */
public class Woodcutting extends Automaton {
    private final int TREE = 1276;
    private final int OAK = 1751;

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
                object(OAK).interact();
                sleep().some();
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
