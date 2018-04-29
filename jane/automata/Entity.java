package com.sqweebloid.jane.automata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.***REMOVED***.api.GameObject;
import net.***REMOVED***.api.NPC;
import net.***REMOVED***.api.queries.GameObjectQuery;
import net.***REMOVED***.api.queries.NPCQuery;
import net.***REMOVED***.client.util.QueryRunner;

import com.sqweebloid.jane.automata.Automaton;

/**
 * Clicks something in the game world.
 */
public class Entity extends Automaton {
    @Inject
    private QueryRunner queryRunner;

    public enum Type {
        NPC,
        GAME_OBJECT,
    };

    private int target;
    private Type type;

    // Whether or not we should far away for targets.
    private boolean shouldSearch;

    // Whether or not to choose a close target randomly.
    private boolean random;

    // This determines whether targets should be within reach.
    // We use the A* pathfinder to sort these instead.
    private boolean shouldInteract;

    public void setObject(int id) {
        target = id;
        type = Type.GAME_OBJECT;
    }

    public void setNpc(int id) {
        target = id;
        type = Type.NPC;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    /**
     * Compares using the A* algorithm.
     */
    public int comparePath(GameObject one, GameObject other) {
        return pathFinder.distanceTo(one.getWorldLocation()) -
            pathFinder.distanceTo(other.getWorldLocation());
    }

    /**
     * Compares using the Euclidean distance.
     */
    public int compare(GameObject one, GameObject other) {
        return getWorldLocation().distanceTo(one.getWorldLocation()) -
            getWorldLocation().distanceTo(other.getWorldLocation());
    }

    @Override
    public void run() {
        GameObjectQuery query = new GameObjectQuery().idEquals(target);
        GameObject[] objects = queryRunner.runQuery(query);
        List<GameObject> candidates = Arrays.asList(objects);

        Collections.sort(candidates, 
                (one, other) -> shouldInteract ? comparePath(one, other) : compare(one, other));

        logger.info("Found {} candidates", candidates.size());

        if (!shouldSearch) {
            candidates = candidates.stream()
                .filter(item -> item.getCanvasTilePoly() != null)
                .collect(Collectors.toList());
        }

        GameObject target = candidates.get(0);

        if (random) {
            target = candidates.get(rand(candidates.size()));
        }

        mouse(target.getCanvasTilePoly().getBounds()).left();
    }
}
