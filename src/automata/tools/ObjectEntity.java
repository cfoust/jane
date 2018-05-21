package com.sqweebloid.jane.automata.tools;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.someclient.api.GameObject;
import org.someclient.api.Point;
import org.someclient.api.coords.WorldPoint;
import org.someclient.api.queries.GameObjectQuery;
import org.someclient.client.util.QueryRunner;

public class ObjectEntity extends Entity {
    private int[] targets = new int[0];

    public void setTarget(int... ids) {
        targets = ids;
    }

    public void setTarget(int id) {
        targets = new int[]{ id };
    }

    /**
     * Transforms an Object (one of the candidates) into a WorldPoint
     * which represents its location.
     */
    public WorldPoint yieldLocation(Object obj) {
        GameObject gameObject = (GameObject) obj;

        Point minRegion = gameObject.getRegionMinLocation();
        Point maxRegion = gameObject.getRegionMaxLocation();
        WorldPoint center = gameObject.getWorldLocation();

        // The GameObject is only one tile wide.
        if (minRegion == maxRegion) {
            return center;
        }

        // TODO find the nearest unoccupied tile next to the game object
        // The problem is that if the game object is too big (i.e occupies >= 9 tiles)
        // the pathfinder can't find a path to it. We should just look for the nearest
        // unoccupied tile and path to it.
        WorldPoint minWorld = WorldPoint.fromRegion(client, minRegion.getX(), minRegion.getY(), center.getPlane());
        WorldPoint maxWorld = WorldPoint.fromRegion(client, maxRegion.getX(), maxRegion.getY(), center.getPlane());
        return minWorld;
    }

    /**
     * Transforms an Object (one of the candidates) into a Polygon
     * which represents its clickbox on the screen.
     */
    public Polygon yieldPolygon(Object obj) {
        return ((GameObject) obj).getCanvasTilePoly();
    }

    /**
     * Fetches the list of candidates.
     */
    public Object[] getCandidates() {
        GameObjectQuery query = new GameObjectQuery().idEquals(targets);
        GameObject[] objects = getQueryRunner().runQuery(query);
        return objects;
    }
}
