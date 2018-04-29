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

/**
 * Cooks in Al Karid.
 */
public class Cooking extends Automaton {
    private final WorldPoint HOUSE = new WorldPoint(3274, 3180, 0);
    private final WorldPoint BANK = new WorldPoint(3269, 3167, 0);

    private final int RANGE_ID = 26181;
    private final int COOKING_ANIM = 896;

    private final Rectangle COOK_BUTTON = new Rectangle(213, 395, 308 - 213, 465 - 395);

    private enum State {
        BANKING,
        TO_RANGE,
        USING_RANGE,
        COOKING,
        TO_BANK,
        DONE,
    };

    private boolean isCooking() {
        return client.getLocalPlayer().getAnimation() == COOKING_ANIM;
    }

    private boolean doneCooking() {
        return getRaw().length == 0;
    }

    private WidgetItem[] getRaw() {
        return inventory.getById(ItemID.RAW_ANCHOVIES);
    }

    boolean outOfRaw = false;

    @Override
    public void run() {
        machine.state(State.USING_RANGE)
            .base()
            .enter(() -> {
                if (outOfRaw || doneCooking() || !moving.closeTo(HOUSE)) return;

                WidgetItem[] raw = getRaw();

                while (!isCooking()) {
                    if (doneCooking()) break;

                    inventory.ensureOpen();
                    sleep().briefly();
                    input.rightClickBounds(raw[rand(raw.length)].getCanvasBounds());
                    sleep().briefly();
                    menu("Use").done();
                    sleep().more();
                    world.clickObject(RANGE_ID);
                    sleep().most();
                    input.click(COOK_BUTTON);
                    sleep().most();
                }
            })
            .finish(State.COOKING);

        machine.state(State.COOKING)
            .enter(() -> {
                if (doneCooking()) return;
                sleep().most();
            })
            .to(State.DONE).when(() -> outOfRaw)
            .to(State.TO_RANGE).when(() -> !moving.closeTo(HOUSE))
            .to(State.TO_BANK).when(() -> doneCooking())
            .to(State.USING_RANGE).when(() -> !isCooking());

        machine.state(State.TO_BANK)
            .enter(() -> {
                go(BANK);
                sleep().some();
            })
            .to(State.BANKING).when(() -> moving.closeTo(BANK));

        machine.state(State.TO_RANGE)
            .enter(() -> {
                go(HOUSE);
                sleep().some();
            })
            .to(State.USING_RANGE).when(() -> moving.closeTo(HOUSE));

        machine.state(State.BANKING)
            .enter(() -> {
                bank.openBank();
                bank.depositEverything();
                sleep().some();

                if (!bank.withdrawAll(ItemID.RAW_ANCHOVIES)) {
                    outOfRaw = true;
                }
            })
            .finish(State.TO_RANGE);

        machine.state(State.DONE).terminal();

        machine.start();
    }
}
