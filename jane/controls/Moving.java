package com.sqweebloid.jane.controls;

import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.Perspective;
import net.***REMOVED***.api.Point;
import net.***REMOVED***.api.coords.LocalPoint;
import net.***REMOVED***.api.coords.WorldPoint;

import com.sqweebloid.jane.automata.movement.MoveGraph;
import net.***REMOVED***.client.plugins.jane.JanePlugin;

public class Moving {
    @Inject
    private JanePlugin plugin;

    @Inject
    private Client client;

    @Inject
    private Input input;

    public WorldPoint getWorld() {
        return client.getLocalPlayer().getWorldLocation();
    }

    public int distanceTo(WorldPoint point) {
        WorldPoint playerLoc = getWorld();
        return point.distanceTo(playerLoc);
    }

    public int distanceTo(MoveGraph.Node node) {
        return distanceTo(node.getLocation());
    }

    public boolean closeTo(WorldPoint point) {
        return distanceTo(point) < 5;
    }

    public boolean closeTo(MoveGraph.Node node) {
        return distanceTo(node) < 5;
    }

    public Point getMinimapLocation(WorldPoint point)
    {
        LocalPoint local = LocalPoint.fromWorld(client, point);
        return Perspective.worldToMiniMap(client, local.getX() + input.rand(5), local.getY() + input.rand(5));
    }
}
