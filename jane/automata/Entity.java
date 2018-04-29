package com.sqweebloid.jane.automata;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
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

    @Override
    public void run() {
        List<Object> candidates = Arrays.asList(getCandidates());

        Collections.sort(candidates, (one, other) -> interact ?
                comparePath(one, other) :
                compare(one, other));

        if (!search) {
            candidates = candidates.stream()
                .filter(item -> yieldPolygon(item) != null)
                .collect(Collectors.toList());
        }

        if (candidates.size() == 0) {
            logger.error("Found no candidates.");
            return;
        }

        Object target = candidates.get(0);

        if (random) {
            target = candidates.get(rand(candidates.size()));
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

            if (x > client.getViewportWidth() || y > client.getViewportHeight()) {
                targetPolygon = null;
            }
        }

        if (search && (targetPolygon == null || distance > 8)) {
            go(yieldLocation(target));
            sleep().more();
        }

        if (menuVerb != null && menuVerb.length() > 0) {
            while (!client.isMenuOpen()) {
                mouse(yieldPolygon(target).getBounds()).right();
            }

            menu(menuVerb).done();
            return;
        }

        // Otherwise we just click it indiscriminately.
        mouse(yieldPolygon(target).getBounds()).left();
    }
}
