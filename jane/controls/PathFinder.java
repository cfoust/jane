package com.sqweebloid.jane.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.GameObject;
import net.***REMOVED***.api.Player;
import net.***REMOVED***.api.Point;
import net.***REMOVED***.api.Region;
import net.***REMOVED***.api.Tile;
import net.***REMOVED***.api.coords.LocalPoint;
import net.***REMOVED***.api.coords.WorldPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PathFinder {
	private static final Logger logger = LoggerFactory.getLogger(PathFinder.class);

    private static final int REGION_SIZE = 104;
    private static final int MAX_DISTANCE = 2400;

    Client client;
    int currentRegion = 0;

    @Inject
    public PathFinder(Client client) {
        this.client = client;
    }

    private Player getPlayer() {
        return client.getLocalPlayer();
    }

    public int pointToRegion(WorldPoint point) {
        int regionX = point.getX() >> 6;
        int regionY = point.getY() >> 6;
        return regionX << 8 | regionY;
    }

    private int getPlayerRegion() {
		return pointToRegion(getPlayerLocation());
    }

    /**
     * Wraps a tile.
     */
    private class Node {
        Tile tile;
        boolean barrier = false;
        List<Node> neighbors = new ArrayList();

        Node(Tile tile) {
            this.tile = tile;
        }

        // Whether or not this node can be passed through.
        void setBarrier(boolean barrier) {
            this.barrier = barrier;
        }

        void addNeighbor(Node neighbor) {
            neighbors.add(neighbor);
        }

        List<Node> getNeighbors() {
            return neighbors;
        }

        boolean isBarrier() {
            return barrier;
        }

        Point getLocation() {
            return tile.getRegionLocation();
        }

        WorldPoint getWorldLocation() {
            return tile.getWorldLocation();
        }

        int getPlane() {
            return tile.getPlane();
        }

        private int distanceTo(Node other) {
            return getLocation().distanceTo(other.getLocation());
        }

        @Override
        public int hashCode() {
            Point point = getLocation();

            // This probably works, but eh
            return point.getX() ^ point.getY() ^ tile.getPlane();
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (this == other) return true;
            if (this.getClass() != other.getClass()) return false;

            Node node = (Node) other;
            Point thisPoint = getLocation();
            Point otherPoint = node.getLocation();

            return thisPoint.getX() == otherPoint.getX() &&
                thisPoint.getY() == otherPoint.getY() &&
                getPlane() == node.getPlane();
        }
    }

    private Node[][] graph = new Node[REGION_SIZE][REGION_SIZE];

    private interface NodeRun {
        void run(Node node);
    }

    /**
     * Attempt to get a node and run something if it's found.
     * Does not run the action on nodes outside of the graph.
     */
    public void grabNode(int x, int y, NodeRun action) {
        if (x < 0 || y < 0 || x >= REGION_SIZE || y >= REGION_SIZE) {
            return;
        }

        action.run(graph[x][y]);
    }

    Set<Integer> IGNORED_OBJECTS = new HashSet();
    {
        IGNORED_OBJECTS.add(11772);
        IGNORED_OBJECTS.add(11787);
        IGNORED_OBJECTS.add(11786);
        IGNORED_OBJECTS.add(1530);
        IGNORED_OBJECTS.add(1529);
        IGNORED_OBJECTS.add(1536);
    }

    public boolean shouldIgnore(int objectId) {
        return IGNORED_OBJECTS.contains(objectId);
    }

    /**
     * Build the graph for the player's current location.
     */
    public void buildGraph() {
        currentRegion = getPlayerRegion();

        Region region = client.getRegion();
        Tile[][][] tiles = region.getTiles();
        int z = client.getPlane();

        Player player = getPlayer();

        // The first pass just generates all the nodes and
        // detects whether they can be walked through.
        graph = new Node[REGION_SIZE][REGION_SIZE];
        for (int x = 0; x < REGION_SIZE; ++x) {
            for (int y = 0; y < REGION_SIZE; ++y) {
                Tile tile = tiles[z][x][y];
                Node node = new Node(tile);

                // This isn't always true but is fine for now.
                // That is, walls have orientations and thus
                // sometimes you can go past them, but not through
                // at a certain angle.
                boolean isWall = tile.getWallObject() != null &&
                    !shouldIgnore(tile.getWallObject().getId());
                boolean hasObject = tile.getGameObjects() != null &&
                    tile.getGameObjects().length > 0;

                // Sometimes the list of objects is deceptive.
                if (hasObject) {
                    int realObjects = 0;
                    for (GameObject object : tile.getGameObjects()) {
                        if (object == null) continue;
                        realObjects++;
                    }

                    hasObject = realObjects > 0;
                }

                node.setBarrier(isWall || hasObject);

                graph[x][y] = node;
            }
        }

        // In the second pass we add neighbors.
        for (int x = 0; x < REGION_SIZE; ++x) {
            for (int y = 0; y < REGION_SIZE; ++y) {
                Node node = graph[x][y];
                NodeRun adder = (neighbor) -> {
                    if (neighbor.isBarrier()) return;
                    node.addNeighbor(neighbor);
                };

                grabNode(x - 1, y, adder);
                grabNode(x + 1, y, adder);
                grabNode(x, y + 1, adder);
                grabNode(x, y - 1, adder);
            }
        }
    }

    public Node worldToNode(WorldPoint point) {
        return graph[point.getX() - client.getBaseX()][point.getY() - client.getBaseY()];
    }

    private WorldPoint getPlayerLocation() {
        return getPlayer().getWorldLocation();
    }

    public Node getPlayerNode() {
        return worldToNode(getPlayerLocation());
    }

    public boolean shouldRebuild() {
        return currentRegion != getPlayerRegion();
    }

    private void initializeHeuristicMap(Map<Node, Integer> map) {
        for (int x = 0; x < REGION_SIZE; ++x) {
            for (int y = 0; y < REGION_SIZE; ++y) {
                Node node = graph[x][y];
                map.put(node, Integer.MAX_VALUE);
            }
        }
    }

    /**
     * Find a path from the player's current position to the node
     * at specified x and y;
     *
     * Runs A*.
     */
    public List<Node> findPath(Node goal) {
        if (shouldRebuild()) buildGraph();

        Node start = getPlayerNode();

        // Map of visited nodes
        Map<Node, Boolean> visited = new HashMap();

        // We want the priority queue to order based on the
        // fuzzy heuristic of hypotenuse distance.
        Comparator<Node> compare = (one, other) -> {
            return one.distanceTo(goal) - other.distanceTo(goal);
        };
        PriorityQueue<Node> openSet = new PriorityQueue(REGION_SIZE * REGION_SIZE, compare);
        openSet.add(start);

        // Node from which this each node can be most efficiently reached
        // Mapping from [to be reached (dest)] -> [most efficient (src)]
        Map<Node, Node> from = new HashMap();

        // Cost of getting from the start node to each node
        Map<Node, Integer> trueScore = new HashMap();
        initializeHeuristicMap(trueScore);

        // No distance to base node
        trueScore.put(start, 0);

        // Heuristic map
        Map<Node, Integer> fuzzyScore = new HashMap();
        initializeHeuristicMap(fuzzyScore);

        // The heuristic here is just the hypotenuse between
        // the two nodes.
        trueScore.put(start, start.distanceTo(goal));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(goal)) {
                return constructPath(from, current);
            }

            visited.put(current, true);

            int possibleScore = trueScore.get(current) + 1;

            for (Node neighbor : current.getNeighbors()) {
                if (visited.containsKey(neighbor)) continue;

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }

                // Skip if it doesn't make sense to add this to the path.
                if (possibleScore >= trueScore.get(neighbor)) continue;

                // As far as we know, this is a good path.
                from.put(neighbor, current);
                trueScore.put(neighbor, possibleScore);
                fuzzyScore.put(neighbor, possibleScore + neighbor.distanceTo(goal));
            }
        }

        return null;
    }

    /**
     * Find a path to this world point if it's within the current region.
     */
    public List<WorldPoint> findPointPath(WorldPoint point) {
        if (getPlayerRegion() != pointToRegion(point)) return null;
        if (shouldRebuild()) buildGraph();

        Node goal = worldToNode(point);
        List<Node> path = findPath(goal);

        if (path == null) return null;

        List<WorldPoint> pointPath = new ArrayList();

        // Can probably skip some of these points but there's no harm
        // in giving all of them.
        for (Node node : path) {
            pointPath.add(node.getWorldLocation());
        }

        return pointPath;
    }

    public int distanceTo(WorldPoint world) {
        if (shouldRebuild()) buildGraph();

        Node goal = worldToNode(world);
        List<Node> path = findPath(goal);
        if (path == null) return Integer.MAX_VALUE;
        return path.size();
    }

    private List<Node> constructPath(Map<Node, Node> from, Node current) {
        List<Node> path = new ArrayList();
        path.add(current);

        while (from.containsKey(current)) {
            current = from.get(current);
            path.add(current);
        }

        Collections.reverse(path);

        return path;
    }

    public void dumpGraph() {
        logger.info("%d", getPlayerRegion());
        for (int x = 0; x < REGION_SIZE; ++x) {
            String row = "";
            for (int y = 0; y < REGION_SIZE; ++y) {
                Node node = graph[x][y];
                row += node.isBarrier() ? "X" : " ";
            }
            System.out.println(row);
        }
    }

    public void dumpNeighbors() {
        for (int x = 0; x < REGION_SIZE; ++x) {
            String row = "";
            for (int y = 0; y < REGION_SIZE; ++y) {
                Node node = graph[x][y];
                row += String.format("%d", node.getNeighbors().size());
            }
            System.out.println(row);
        }
    }
}
