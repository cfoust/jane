package com.sqweebloid.jane.automata.tools.movement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.***REMOVED***.api.coords.WorldPoint;

/**
 * Yeah, yeah, I know, I don't like it either.
 */
public class MoveGraph {
    public static enum Node {

        DRAYNOR(new WorldPoint(3104, 3256, 0)),
        DRAYNOR_FISHING(new WorldPoint(3086, 3234, 0)),
        DRAYNOR_CROSSROADS(new WorldPoint(3109, 3294, 0)),

        LUMBRIDGE_CROSSROADS(new WorldPoint(3235, 3225, 0)),
        LUMBRIDGE_COW_PATCH(new WorldPoint(3251, 3266, 0)),

        ALKARID_NORTH_GATE(new WorldPoint(3268, 3330, 0)),
        ALKARID_MINE_ENTRANCE(new WorldPoint(3291, 3267, 0)),
        ALKARID_BANK(new WorldPoint(3271, 3166, 0)),

        VARROCK_SQUARE(new WorldPoint(3212, 3425, 0)),
        VARROCK_WEST_BANK_DOOR(new WorldPoint(3182, 3429, 0)),
        VARROCK_WEST_BANK(new WorldPoint(3182, 3438, 0)),
        GRAND_EXCHANGE(new WorldPoint(3164, 3484, 0)),

        BARBARIAN_VILLAGE_EAST(new WorldPoint(3099, 3420, 0)),
        BARBARIAN_VILLAGE_SQUARE(new WorldPoint(3081, 3416, 0));

        WorldPoint location;

        Node(WorldPoint location) {
            this.location = location;
        }

        public WorldPoint getLocation() {
            return location;
        }

        public List<Link> getLinks() {
            Link[] links = Link.values();
            List<Link> successors = new ArrayList();

            for (int i = 0; i < links.length; i++) {
                Link link = links[i];

                if (!link.getFirst().equals(this) && !link.getSecond().equals(this)) continue;
                successors.add(link);
            }

            return successors;
        }
    }

    public static enum Link {
        DRAYNOR_FISHING_TO_DRAYNOR(
                Node.DRAYNOR,
                Node.DRAYNOR_FISHING,
                new WorldPoint[]{
                    new WorldPoint(3106, 3257, 0),
                    new WorldPoint(3104, 3256, 0),
                    new WorldPoint(3104, 3252, 0),
                    new WorldPoint(3102, 3250, 0),
                    new WorldPoint(3096, 3248, 0),
                    new WorldPoint(3088, 3248, 0),
                    new WorldPoint(3086, 3246, 0),
                    new WorldPoint(3085, 3242, 0),
                    new WorldPoint(3084, 3238, 0),
                }),

        DRAYNOR_TO_CROSSROADS(
                Node.DRAYNOR,
                Node.DRAYNOR_CROSSROADS,
                new WorldPoint[]{
                    new WorldPoint(3103, 3256, 0),
                    new WorldPoint(3104, 3260, 0),
                    new WorldPoint(3103, 3267, 0),
                    new WorldPoint(3105, 3273, 0),
                    new WorldPoint(3105, 3280, 0),
                    new WorldPoint(3109, 3286, 0),
                    new WorldPoint(3109, 3294, 0)
                }),

        DRAYNOR_CROSSROADS_TO_LUMBRIDGE_CROSSROADS(
                Node.DRAYNOR_CROSSROADS,
                Node.LUMBRIDGE_CROSSROADS,
                new WorldPoint[]{
                    new WorldPoint(3109, 3294, 0),
                    new WorldPoint(3113, 3294, 0),
                    new WorldPoint(3116, 3296, 0),
                    new WorldPoint(3125, 3294, 0),
                    new WorldPoint(3134, 3295, 0),
                    new WorldPoint(3142, 3293, 0),
                    new WorldPoint(3148, 3293, 0),
                    new WorldPoint(3153, 3292, 0),
                    new WorldPoint(3157, 3291, 0),
                    new WorldPoint(3161, 3288, 0),
                    new WorldPoint(3165, 3287, 0),
                    new WorldPoint(3169, 3286, 0),
                    new WorldPoint(3175, 3285, 0),
                    new WorldPoint(3180, 3285, 0),
                    new WorldPoint(3186, 3282, 0),
                    new WorldPoint(3191, 3281, 0),
                    new WorldPoint(3195, 3279, 0),
                    new WorldPoint(3198, 3279, 0),
                    new WorldPoint(3203, 3277, 0),
                    new WorldPoint(3208, 3277, 0),
                    new WorldPoint(3214, 3274, 0),
                    new WorldPoint(3214, 3270, 0),
                    new WorldPoint(3216, 3261, 0),
                    new WorldPoint(3217, 3254, 0),
                    new WorldPoint(3218, 3248, 0),
                    new WorldPoint(3219, 3242, 0),
                    new WorldPoint(3223, 3236, 0),
                    new WorldPoint(3225, 3235, 0),
                    new WorldPoint(3229, 3232, 0),
                    new WorldPoint(3231, 3228, 0),
                    new WorldPoint(3234, 3225, 0)
                }),

        LUMBRIDGE_CROSSROADS_TO_COW_PATCH(
                Node.LUMBRIDGE_CROSSROADS,
                Node.LUMBRIDGE_COW_PATCH,
                new WorldPoint[]{
                    new WorldPoint(3236, 3225, 0),
                    new WorldPoint(3241, 3225, 0),
                    new WorldPoint(3247, 3225, 0),
                    new WorldPoint(3251, 3226, 0),
                    new WorldPoint(3256, 3226, 0),
                    new WorldPoint(3258, 3229, 0),
                    new WorldPoint(3259, 3233, 0),
                    new WorldPoint(3259, 3237, 0),
                    new WorldPoint(3259, 3241, 0),
                    new WorldPoint(3257, 3244, 0),
                    new WorldPoint(3255, 3247, 0),
                    new WorldPoint(3253, 3250, 0),
                    new WorldPoint(3251, 3253, 0),
                    new WorldPoint(3250, 3256, 0),
                    new WorldPoint(3250, 3259, 0),
                    new WorldPoint(3250, 3263, 0),
                    new WorldPoint(3250, 3266, 0)
                }),

        LUMBRIDGE_COW_PATH_TO_ALKARID_NORTH_GATE(
                Node.LUMBRIDGE_COW_PATCH,
                Node.ALKARID_NORTH_GATE,
                new WorldPoint[]{
                    new WorldPoint(3251, 3267, 0),
                    new WorldPoint(3248, 3271, 0),
                    new WorldPoint(3247, 3273, 0),
                    new WorldPoint(3245, 3274, 0),
                    new WorldPoint(3244, 3276, 0),
                    new WorldPoint(3242, 3278, 0),
                    new WorldPoint(3240, 3281, 0),
                    new WorldPoint(3239, 3283, 0),
                    new WorldPoint(3238, 3286, 0),
                    new WorldPoint(3239, 3288, 0),
                    new WorldPoint(3239, 3291, 0),
                    new WorldPoint(3238, 3294, 0),
                    new WorldPoint(3238, 3296, 0),
                    new WorldPoint(3238, 3299, 0),
                    new WorldPoint(3238, 3303, 0),
                    new WorldPoint(3240, 3305, 0),
                    new WorldPoint(3243, 3309, 0),
                    new WorldPoint(3245, 3312, 0),
                    new WorldPoint(3249, 3315, 0),
                    new WorldPoint(3250, 3317, 0),
                    new WorldPoint(3252, 3319, 0),
                    new WorldPoint(3255, 3322, 0),
                    new WorldPoint(3260, 3323, 0),
                    new WorldPoint(3265, 3325, 0),
                    new WorldPoint(3268, 3326, 0),
                    new WorldPoint(3268, 3330, 0)
                }),

        ALKARID_NORTH_GATE_TO_ALKARID_MINE_ENTRANCE(
                Node.ALKARID_NORTH_GATE,
                Node.ALKARID_MINE_ENTRANCE,
                new WorldPoint[]{
                    new WorldPoint(3270, 3330, 0),
                    new WorldPoint(3274, 3331, 0),
                    new WorldPoint(3277, 3331, 0),
                    new WorldPoint(3281, 3331, 0),
                    new WorldPoint(3281, 3330, 0),
                    new WorldPoint(3281, 3326, 0),
                    new WorldPoint(3283, 3322, 0),
                    new WorldPoint(3284, 3319, 0),
                    new WorldPoint(3287, 3316, 0),
                    new WorldPoint(3286, 3310, 0),
                    new WorldPoint(3286, 3307, 0),
                    new WorldPoint(3286, 3304, 0),
                    new WorldPoint(3286, 3302, 0),
                    new WorldPoint(3286, 3300, 0),
                    new WorldPoint(3286, 3297, 0),
                    new WorldPoint(3286, 3293, 0),
                    new WorldPoint(3287, 3291, 0),
                    new WorldPoint(3287, 3287, 0),
                    new WorldPoint(3287, 3285, 0),
                    new WorldPoint(3287, 3282, 0),
                    new WorldPoint(3286, 3280, 0),
                    new WorldPoint(3286, 3276, 0),
                    new WorldPoint(3286, 3273, 0),
                    new WorldPoint(3286, 3270, 0),
                    new WorldPoint(3289, 3268, 0),
                    new WorldPoint(3291, 3267, 0)
                }),

        ALKARID_MINE_ENTRANCE_TO_ALKARID_BANK(
                Node.ALKARID_MINE_ENTRANCE,
                Node.ALKARID_BANK,
                new WorldPoint[]{
                    new WorldPoint(3290, 3267, 0),
                    new WorldPoint(3290, 3264, 0),
                    new WorldPoint(3290, 3260, 0),
                    new WorldPoint(3291, 3255, 0),
                    new WorldPoint(3291, 3250, 0),
                    new WorldPoint(3290, 3246, 0),
                    new WorldPoint(3289, 3241, 0),
                    new WorldPoint(3289, 3238, 0),
                    new WorldPoint(3285, 3234, 0),
                    new WorldPoint(3281, 3231, 0),
                    new WorldPoint(3279, 3229, 0),
                    new WorldPoint(3278, 3226, 0),
                    new WorldPoint(3277, 3222, 0),
                    new WorldPoint(3277, 3218, 0),
                    new WorldPoint(3276, 3215, 0),
                    new WorldPoint(3276, 3210, 0),
                    new WorldPoint(3274, 3205, 0),
                    new WorldPoint(3276, 3202, 0),
                    new WorldPoint(3279, 3197, 0),
                    new WorldPoint(3280, 3195, 0),
                    new WorldPoint(3281, 3190, 0),
                    new WorldPoint(3281, 3185, 0),
                    new WorldPoint(3280, 3181, 0),
                    new WorldPoint(3279, 3177, 0),
                    new WorldPoint(3278, 3174, 0),
                    new WorldPoint(3277, 3171, 0),
                    new WorldPoint(3274, 3167, 0),
                    new WorldPoint(3271, 3166, 0)
                }),

        ALKARID_NORTH_GATE_TO_VARROCK_SQUARE(
                Node.ALKARID_NORTH_GATE,
                Node.VARROCK_SQUARE,
                new WorldPoint[]{
                    new WorldPoint(3268, 3330, 0),
                    new WorldPoint(3264, 3331, 0),
                    new WorldPoint(3257, 3333, 0),
                    new WorldPoint(3253, 3333, 0),
                    new WorldPoint(3248, 3336, 0),
                    new WorldPoint(3243, 3336, 0),
                    new WorldPoint(3238, 3336, 0),
                    new WorldPoint(3234, 3336, 0),
                    new WorldPoint(3230, 3337, 0),
                    new WorldPoint(3226, 3339, 0),
                    new WorldPoint(3225, 3343, 0),
                    new WorldPoint(3226, 3346, 0),
                    new WorldPoint(3225, 3350, 0),
                    new WorldPoint(3224, 3354, 0),
                    new WorldPoint(3221, 3357, 0),
                    new WorldPoint(3218, 3362, 0),
                    new WorldPoint(3216, 3366, 0),
                    new WorldPoint(3213, 3368, 0),
                    new WorldPoint(3212, 3372, 0),
                    new WorldPoint(3211, 3376, 0),
                    new WorldPoint(3210, 3380, 0),
                    new WorldPoint(3210, 3386, 0),
                    new WorldPoint(3211, 3390, 0),
                    new WorldPoint(3211, 3393, 0),
                    new WorldPoint(3211, 3397, 0),
                    new WorldPoint(3211, 3400, 0),
                    new WorldPoint(3210, 3404, 0),
                    new WorldPoint(3211, 3407, 0),
                    new WorldPoint(3210, 3410, 0),
                    new WorldPoint(3210, 3413, 0),
                    new WorldPoint(3211, 3416, 0),
                    new WorldPoint(3211, 3419, 0),
                    new WorldPoint(3211, 3422, 0),
                    new WorldPoint(3212, 3425, 0)
                }),

        VARROCK_SQUARE_TO_VARROCK_WEST_BANK_DOOR(
                Node.VARROCK_SQUARE,
                Node.VARROCK_WEST_BANK_DOOR,
                new WorldPoint[]{
                    new WorldPoint(3211, 3424, 0),
                    new WorldPoint(3208, 3426, 0),
                    new WorldPoint(3205, 3428, 0),
                    new WorldPoint(3202, 3429, 0),
                    new WorldPoint(3195, 3430, 0),
                    new WorldPoint(3192, 3430, 0),
                    new WorldPoint(3186, 3429, 0),
                    new WorldPoint(3182, 3429, 0)
                }),

        VARROCK_WEST_BANK_DOOR_TO_GRAND_EXCHANGE(
                Node.VARROCK_WEST_BANK_DOOR,
                Node.GRAND_EXCHANGE,
                new WorldPoint[]{
                    new WorldPoint(3182, 3429, 0),
                    new WorldPoint(3177, 3432, 0),
                    new WorldPoint(3177, 3433, 0),
                    new WorldPoint(3176, 3436, 0),
                    new WorldPoint(3176, 3439, 0),
                    new WorldPoint(3177, 3443, 0),
                    new WorldPoint(3177, 3447, 0),
                    new WorldPoint(3175, 3452, 0),
                    new WorldPoint(3173, 3455, 0),
                    new WorldPoint(3171, 3457, 0),
                    new WorldPoint(3169, 3458, 0),
                    new WorldPoint(3166, 3460, 0),
                    new WorldPoint(3165, 3464, 0),
                    new WorldPoint(3164, 3468, 0),
                    new WorldPoint(3164, 3472, 0),
                    new WorldPoint(3164, 3474, 0),
                    new WorldPoint(3163, 3476, 0),
                    new WorldPoint(3164, 3480, 0),
                    new WorldPoint(3165, 3482, 0),
                    new WorldPoint(3164, 3484, 0)
                }),

        VARROCK_WEST_BANK_DOOR_TO_BARBARIAN_VILLAGE_EAST(
                Node.VARROCK_WEST_BANK_DOOR,
                Node.BARBARIAN_VILLAGE_EAST,
                new WorldPoint[]{
                    new WorldPoint(3181, 3429, 0),
                    new WorldPoint(3178, 3428, 0),
                    new WorldPoint(3175, 3428, 0),
                    new WorldPoint(3171, 3428, 0),
                    new WorldPoint(3168, 3428, 0),
                    new WorldPoint(3166, 3426, 0),
                    new WorldPoint(3164, 3424, 0),
                    new WorldPoint(3162, 3421, 0),
                    new WorldPoint(3158, 3419, 0),
                    new WorldPoint(3156, 3417, 0),
                    new WorldPoint(3150, 3416, 0),
                    new WorldPoint(3146, 3416, 0),
                    new WorldPoint(3143, 3416, 0),
                    new WorldPoint(3140, 3416, 0),
                    new WorldPoint(3136, 3416, 0),
                    new WorldPoint(3133, 3416, 0),
                    new WorldPoint(3130, 3415, 0),
                    new WorldPoint(3127, 3414, 0),
                    new WorldPoint(3124, 3415, 0),
                    new WorldPoint(3122, 3415, 0),
                    new WorldPoint(3119, 3417, 0),
                    new WorldPoint(3116, 3419, 0),
                    new WorldPoint(3114, 3420, 0),
                    new WorldPoint(3109, 3420, 0),
                    new WorldPoint(3107, 3420, 0),
                    new WorldPoint(3104, 3421, 0),
                    new WorldPoint(3101, 3420, 0),
                    new WorldPoint(3099, 3420, 0)
                }),

        BARBARIAN_VILLAGE_EAST_TO_BARBARIAN_VILLAGE_SQUARE(
                Node.BARBARIAN_VILLAGE_EAST,
                Node.BARBARIAN_VILLAGE_SQUARE,
                new WorldPoint[]{
                    new WorldPoint(3098, 3420, 0),
                    new WorldPoint(3095, 3420, 0),
                    new WorldPoint(3092, 3419, 0),
                    new WorldPoint(3088, 3420, 0),
                    new WorldPoint(3086, 3420, 0),
                    new WorldPoint(3085, 3418, 0),
                    new WorldPoint(3083, 3416, 0),
                    new WorldPoint(3081, 3416, 0)
                }),

        VARROCK_WEST_BANK_DOOR_TO_VARROCK_WEST_BANK(
                Node.VARROCK_WEST_BANK_DOOR,
                Node.VARROCK_WEST_BANK,
                new WorldPoint[]{
                    new WorldPoint(3182, 3430, 0),
                    new WorldPoint(3182, 3433, 0),
                    new WorldPoint(3182, 3438, 0)
                });

        Link(Node one, Node two, WorldPoint[] path) {
            this.node1 = one;
            this.node2 = two;
            this.path = path;
        }

        public Node getFirst() {
            return node1;
        }

        public Node getSecond() {
            return node2;
        }

        public WorldPoint[] getPath() {
            return path;
        }

        Node node1;
        Node node2;
        WorldPoint[] path;
    }

    public static Node closeTo(WorldPoint point) {
        Node[] nodes = Node.values();

        int least = Integer.MAX_VALUE;
        int leastIndex = -1;
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            int distance = point.distanceTo(node.getLocation());

            if (distance < least) {
                least = distance;
                leastIndex = i;
            }
        }

        if (leastIndex == -1) return null;
        return nodes[leastIndex];
    }

    private static void storeLink(Node from, Node to, Map<Node, List<Node>> successors) {
        List<Node> nodes = successors.get(from);

        if (nodes == null) {
            successors.put(from, new ArrayList());
            nodes = successors.get(from);
        }

        nodes.add(to);
    }

    private static List<Node> recursePath(Node from,
            Node to, 
            Map<Node, List<Node>> successors,
            Map<Node, Boolean> visited) {
        visited.put(from, true);
        if (from == to) {
            List<Node> nodes = new ArrayList();
            nodes.add(to);
            return nodes;
        }

        for (Node node : successors.get(from)) {
            if (visited.get(node)) continue;

            List<Node> nodes = recursePath(node, to, successors, visited);

            if (nodes.size() == 0) continue;

            nodes.add(0, from);
            return nodes;
        }

        return new ArrayList();
    }

    private static List<Node> getPath(Node from, Node to) {
        if (from == to) return new ArrayList();

        Map<Node, Boolean> visited = new HashMap();

        Node[] nodes = Node.values();
        for (Node node : nodes) {
            visited.put(node, false);
        }

        // Precompute successors
        Map<Node, List<Node>> successors = new HashMap();
        Link[] links = Link.values();
        for (Link link : links) {
            storeLink(link.getFirst(), link.getSecond(), successors);
            storeLink(link.getSecond(), link.getFirst(), successors);
        }

        return recursePath(from, to, successors, visited);
    }

    public static List<WorldPoint> getPoints(WorldPoint from, WorldPoint to) {
        // Generate the path
        MoveGraph.Node start = MoveGraph.closeTo(from),
            end = MoveGraph.closeTo(to);

        List<Node> path = getPath(start, end);

        if (path.size() == 0) {
            return new ArrayList();
        }

        List<WorldPoint> pointPath = new ArrayList();

        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);

            for (Link link : Link.values()) {
                if (!link.getFirst().equals(current) && !link.getSecond().equals(current)) continue;
                if (!link.getFirst().equals(next) && !link.getSecond().equals(next)) continue;

                List<WorldPoint> points = Arrays.asList(link.getPath());

                if (!link.getFirst().equals(current)) {
                    Collections.reverse(points);
                }

                pointPath.addAll(points);
                break;
            }
        }

        return pointPath;
    }

    public static List<WorldPoint> getPoints(Node from, Node to) {
        return getPoints(from.getLocation(), to.getLocation());
    }
}
