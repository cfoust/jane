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
    }
}
