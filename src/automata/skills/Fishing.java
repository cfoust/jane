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

    @Override
    public void run() {
    }
}
