package com.sqweebloid.jane.controls;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Value;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.GameObject;
import net.***REMOVED***.api.NPC;
import net.***REMOVED***.api.queries.GameObjectQuery;
import net.***REMOVED***.api.queries.NPCQuery;
import net.***REMOVED***.client.util.QueryRunner;
import java.util.Collections;

import com.sqweebloid.jane.controls.Input;
import net.***REMOVED***.client.plugins.jane.JanePlugin;

/**
 * Handles interaction with the world in abstract terms.
 * Things like clicking NPC's, game objects, and otherwise.
 */
@Singleton
public class World {
    @Inject
    private JanePlugin plugin;

    @Inject
    private Client client;

    @Inject
    private Input input;

    @Inject
    private PathFinder pathFinder;

    @Inject
    private QueryRunner queryRunner;

    public List<NPC> getNPCs(String... names) {
		NPCQuery query = new NPCQuery().nameEquals(names);
        NPC[] npcs = queryRunner.runQuery(query);
        return Arrays.asList(npcs);
    }

    @Value
    private class NPCDistance {
        private final NPC npc;
        private final int distance;
    }

    /**
     * Sorts NPCs using the A* pathfinder algorithm.
     * The first NPC in the returned list is the closest.
     */
    public List<NPC> getSortedNPCs(String... names) {
        List<NPC> npcs = getNPCs(names);
        List<NPCDistance> sortable = new ArrayList();

        for (NPC npc : npcs) {
            int distance = pathFinder.distanceTo(npc.getWorldLocation());
            if (distance == Integer.MAX_VALUE) continue;
            sortable.add(new NPCDistance(npc, distance));
        }

        Collections.sort(sortable, (one, other) -> 
                one.getDistance() -
                other.getDistance());

        List<NPC> sorted = new ArrayList();
        for (NPCDistance pair : sortable) {
            sorted.add(pair.getNpc());
        }

        return sorted;
    }

    public List<GameObject> getObjects(int... objectIds) {
        GameObjectQuery query = new GameObjectQuery().idEquals(objectIds);
        GameObject[] objects = queryRunner.runQuery(query);
        List<GameObject> candidates = new ArrayList();

        for (GameObject object : objects) {
            if (object == null) continue;

            Polygon p = object.getConvexHull();
            if (p == null) continue;

            Rectangle bounds = p.getBounds();
            int centerX = (int) bounds.getCenterX();
            int centerY = (int) bounds.getCenterY();

            if (centerX > client.getViewportWidth() || centerY > client.getViewportHeight()) {
                continue;
            }

            candidates.add(object);
        }

        return candidates;
    }

    public void clickObject(int... objectIds) {
        List<GameObject> candidates = getObjects(objectIds);

        if (candidates.size() == 0) {
            return;
        }

        GameObject target = candidates.get(input.rand(candidates.size()));
        input.click(target.getCanvasTilePoly().getBounds());
    }
}
