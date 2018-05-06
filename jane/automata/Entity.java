package com.sqweebloid.jane.automata;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.***REMOVED***.api.MenuEntry;
import net.***REMOVED***.api.coords.WorldPoint;
import net.***REMOVED***.client.util.QueryRunner;

/**
 * Clicks something in the game world.
 */
abstract public class Entity extends Automaton {
    @Inject
    private QueryRunner queryRunner;

    // Whether or not we should look offscreen for targets and
    // move to them if necessary.
    private boolean search = false;

    // Whether or not to choose a close target randomly.
    // If this is false, we just choose the closest.
    private boolean random = true;

    // This determines whether targets should be within reach.
    // We use the A* pathfinder to sort these instead.
    // This is different from calling npc(blah).interact().
    // That just clicks on whatever the entity is.
    private boolean interact = true;

    @Inject
    MenuState menuState;

    // Instead of just clicking on the entity, open its menu.
    String menuVerb;

    public void setRandom(boolean random) {
        this.random = random;
    }

    public void setInteract(boolean interact) {
        this.interact = interact;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public void setMenuVerb(String verb) {
        this.menuVerb = verb;
    }

    public QueryRunner getQueryRunner() {
        return queryRunner;
    }

    /**
     * Transforms an Object (one of the candidates) into a WorldPoint
     * which represents its location.
     */
    abstract public WorldPoint yieldLocation(Object obj);

    /**
     * Transforms an Object (one of the candidates) into a Polygon
     * which represents its clickbox on the screen.
     */
    abstract public Polygon yieldPolygon(Object obj);

    /**
     * Fetches the list of candidates.
     */
    abstract public Object[] getCandidates();

    /**
     * Compares using the A* algorithm.
     */
    public int comparePath(Object one, Object other) {
        return pathFinder.distanceTo(yieldLocation(one)) -
            pathFinder.distanceTo(yieldLocation(other));
    }

    /**
     * Compares using the Euclidean distance.
     */
    public int compare(Object one, Object other) {
        return getWorldLocation().distanceTo(yieldLocation(one)) -
            getWorldLocation().distanceTo(yieldLocation(other));
    }

    public boolean isTarget(MenuEntry entry) {
        return entry.getOption().equals(menuVerb);
    }

    @Override
    public void run() {
        List<Object> candidates = Arrays.asList(getCandidates());

        if (candidates.size() == 0) {
            logger.error("Found no candidates.");
            return;
        }

        Collections.sort(candidates, (one, other) -> compare(one, other));

        int index = 0;
        Object target = null;
        for (int i = 0; i < candidates.size(); i++) {
            Object candidate = candidates.get(i);
            WorldPoint location = yieldLocation(candidate);

            if (pathFinder.findPointPath(location) != null) {
                target = candidate;
                break;
            }
        }

        if (target == null) {
            logger.error("Found no path to any candidates.");
            return;
        }

        Polygon targetPolygon = yieldPolygon(target);
        int distance = getWorldLocation().distanceTo(yieldLocation(target));

        if (!search && targetPolygon == null) {
            logger.error("Search is not enabled and target was not on screen.");
            return;
        }

        // Sometimes we get polygons that aren't actually on the screen.
        if (targetPolygon != null) {
            Rectangle bounds = targetPolygon.getBounds();
            int x = (int) bounds.getCenterX();
            int y = (int) bounds.getCenterY();

            if (x > client.getViewportWidth() ||
                y > client.getViewportHeight() ||
                x < 0 ||
                y < 0) {
                targetPolygon = null;
            }
        }

        if (targetPolygon == null || distance > 8) {
            go(yieldLocation(target));
            sleep().more();
        }

        if (yieldPolygon(target) == null) {
            run();
            return;
        }

        // Click it indiscriminately if there's no menu item.
        if (menuVerb == null) {
            mouse(yieldPolygon(target).getBounds()).left();
            return;
        }

        while (true) {
            mouse(yieldPolygon(target).getBounds()).move();
            List<MenuEntry> entries = menuState.getEntries();

            // We can just click on the entity if doing so performs
            // the action we're looking for.
            if (entries.size() > 0 && isTarget(entries.get(0))) {
                mouse().left();
                break;
            }

            mouse(yieldPolygon(target).getBounds()).right();

            if (client.isMenuOpen()) {
                menu(menuVerb).done();
                break;
            }

            sleep().more();
        }
    }
}
