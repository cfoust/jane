package com.sqweebloid.jane.automata.skills;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.Item;
import net.***REMOVED***.api.NPC;
import net.***REMOVED***.api.coords.WorldPoint;
import com.sqweebloid.jane.Constants;

import net.***REMOVED***.client.plugins.jane.JanePlugin;
import com.sqweebloid.jane.automata.Automaton;
import com.sqweebloid.jane.controls.Input;

/**
 * Fishes in Draynor Village.
 */
public class Fishing extends Automaton {
    private final WorldPoint FISHING_SPOT = new WorldPoint(3087, 3230, 0);
    private final WorldPoint BANK = new WorldPoint(3093, 3242, 0);

    private enum State {
        FINDING_SPOT,
        FISHING,
        HANDLE_LEVELUP,
        TO_BANK,
        BANKING,
        TO_FISH
    };

    public boolean isFishing() {
        return client.getLocalPlayer().getInteracting() != null;
    }

    NPC target;

    public void findSpot() {
        List<NPC> npcs = client.getNpcs();

        if (npcs.size() == 0) {
            log("No fishing spots");
        }

        for (NPC npc : npcs) {
            if (!npc.getName().equals("Fishing spot")) continue;
            target = npc;
            return;
        }

        target = null;
    }

    public void clickSpot() {
        if (target == null) return;

        Polygon objectClickbox = target.getConvexHull();

        if (objectClickbox == null) {
            net.***REMOVED***.api.Point loc = target.getMinimapLocation();
            input.click(loc.getX(), loc.getY());
            target = null;
            return;
        }

        Rectangle bounds = objectClickbox.getBounds();

        int x = (int) bounds.getCenterX();
        int y = (int) bounds.getCenterY();

        if (x > client.getViewportWidth() || y > client.getViewportHeight()) {
            net.***REMOVED***.api.Point loc = target.getMinimapLocation();
            input.click(loc.getX(), loc.getY());
            return;
        }

        input.click(bounds);
    }

    @Override
    public void run() {
        machine.state(State.FINDING_SPOT)
            .base()
            .enter(() -> {
                if (inventory.isFull()) return;

                while (!isFishing()) {
                    findSpot();
                    clickSpot();
                    sleep().more();
                }
            })
            .finish(State.FISHING);

        machine.state(State.FISHING)
            .enter(() -> {
                if (inventory.isFull()) return;

                // To simulate a human forgetting about things
                sleep().most();
            })
            .to(State.TO_BANK).when(() -> inventory.isFull())
            .to(State.FINDING_SPOT).when(() -> !isFishing());

        machine.state(State.TO_BANK)
            .enter(() -> {
                go(BANK);
            })
            .finish(State.BANKING);

        machine.state(State.BANKING)
            .enter(() -> {
                bank.openBank();

                while (inventory.numFreeSlots() != 27) {
                    Item[] items = inventory.getItems();

                    int slot = input.rand(26) + 1;
                    while (items[slot].getId() == -1) slot = input.rand(26) + 1;

                    Item item = items[slot];
                    bank.depositAllInSlot(slot);
                    sleep().most();
                }

                sleep().some();
            })
            .to(State.TO_FISH).when(() -> inventory.numFreeSlots() == 27);

        machine.state(State.TO_FISH)
            .enter(() -> {
                go(FISHING_SPOT);
            })
            .to(State.FINDING_SPOT).when(() -> moving.closeTo(FISHING_SPOT));

        machine.start();
    }
}
