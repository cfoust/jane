package com.sqweebloid.jane.automata.tools.movement;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.someclient.api.Point;
import org.someclient.api.coords.WorldPoint;
import org.someclient.api.coords.LocalPoint;
import org.someclient.api.Perspective;

import com.sqweebloid.jane.automata.Automaton;

public class Mover extends Automaton {
    private WorldPoint destination;

    private enum State {
        MOVING,
        CLICKING,
        DONE,
    };

    public Mover(MoveGraph.Node dest) {
        destination = dest.getLocation();
    }

    public Mover(WorldPoint dest) {
        destination = dest;
    }

    public WorldPoint currentLocation() {
        return client.getLocalPlayer().getWorldLocation();
    }

    public int distanceTo(WorldPoint point) {
        return currentLocation().distanceTo(point);
    }

    public Point getMinimapLocation(WorldPoint point) {
        LocalPoint local = LocalPoint.fromWorld(client, point);
        return Perspective.worldToMiniMap(client, local.getX(), local.getY());
    }

    public void clickPoint(WorldPoint point) {
        Point minimap = getMinimapLocation(point);
        if (minimap == null) return;
        mouse(minimap.getX(), minimap.getY()).left();
    }

    public boolean isDone() {
        return destination.distanceTo(currentLocation()) < 5;
    }

    private int index = 0;

    private List<WorldPoint> generatePath() {
        WorldPoint start = currentLocation();
        WorldPoint end = destination;

        int startRegion = pathFinder.pointToRegion(start);
        int endRegion = pathFinder.pointToRegion(end);

        // Just use the A* pathfinder if we're in the
        // same region.
        if (startRegion == endRegion) {
            logger.info("Path to {} is in region.", end);
            return pathFinder.findPointPath(end);
        }

        // Check to see whether there are any accessible nodes
        // within our region.
        MoveGraph.Node closestNode = MoveGraph.closeTo(start);
        WorldPoint closestNodePoint = closestNode.getLocation();
        int closestNodeRegion = pathFinder.pointToRegion(closestNodePoint);

        if (closestNode == null || closestNodeRegion != startRegion) {
            // There's no way for us to get to the node network.
            logger.error("Could not find node close to us.");
            return null;
        }

        // Cool, it's in the region.

        List<WorldPoint> toClosest = new ArrayList();
        if (closestNodePoint.distanceTo(start) > 10) {
            // Now we look for a A* path from our current position to the node start.
            toClosest = pathFinder.findPointPath(closestNodePoint);

            if (toClosest == null || toClosest.size() == 0) {
                // Looks like we can't get to the node either.
                logger.error("Couldn't find path to start node.");
                return null;
            }
        }

        MoveGraph.Node endNode = MoveGraph.closeTo(end);
        if (endNode == null) return null;

        List<WorldPoint> path = MoveGraph.getPoints(closestNode, endNode);
        path.addAll(0, toClosest);

        return path;
    }

    @Override
    public void run() {
        List<WorldPoint> path = generatePath();

        if (path == null) {
            logger.error("No path to destination {}", destination.toString());
            return;
        }

        logger.info("Path to destination {}", destination.toString());

        index = 0;

        machine.state(State.CLICKING)
            .base()
            .enter(() -> {
                if (path == null) {
                    logger.error("Null path.");
                    return;
                }

                WorldPoint current = path.get(index);

                for (int i = index; i < path.size(); i++) {
                    WorldPoint next = path.get(i);
                    if (distanceTo(next) > 12) break;
                    index = i;
                }

                logger.info("Index in path is {}/{}", index + 1, path.size());

                clickPoint(path.get(index));
            })
            .finish(State.MOVING);

        machine.state(State.MOVING)
            .enter(() -> {
                sleep().more();
            })
            .to(State.DONE).when(() -> isDone())
            .to(State.CLICKING).when(() -> !isDone());

        machine.state(State.DONE).terminal();

        machine.start();
    }
}

