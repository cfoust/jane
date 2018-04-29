package com.sqweebloid.jane.automata;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.NPC;
import net.***REMOVED***.api.coords.WorldPoint;
import net.***REMOVED***.api.queries.NPCQuery;
import net.***REMOVED***.client.util.QueryRunner;

public class NPCEntity extends Entity {
    private int[] targets = new int[0];
    private String nameTarget;

    public void setTarget(int... ids) {
        targets = ids;
    }

    public void setTarget(int id) {
        targets = new int[]{ id };
    }

    public void setTarget(String name) {
        nameTarget = name;
    }

    public WorldPoint yieldLocation(Object obj) {
        return ((NPC) obj).getWorldLocation();
    }

    public Polygon yieldPolygon(Object obj) {
        return ((NPC) obj).getCanvasTilePoly();
    }

    public Object[] getCandidates() {
		NPCQuery query = new NPCQuery();

        if (nameTarget != null && nameTarget.length() > 0) {
            query.nameEquals(nameTarget);
        } else {
            query.idEquals(targets);
        }

        return getQueryRunner().runQuery(query);
    }
}
