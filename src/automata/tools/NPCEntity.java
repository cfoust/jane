package com.sqweebloid.jane.automata.tools;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.someclient.api.NPC;
import org.someclient.api.coords.WorldPoint;
import org.someclient.api.queries.NPCQuery;
import org.someclient.client.util.QueryRunner;

public class NPCEntity extends Entity {
    private int[] targets = new int[0];
    private String nameTarget;
    private boolean fightable = false;

    public void setTarget(int... ids) {
        targets = ids;
    }

    public void setTarget(int id) {
        targets = new int[]{ id };
    }

    public void setTarget(String name) {
        nameTarget = name;
    }

    /**
     * Determines whether this automaton should only
     * look for NPC's that the player can fight. This just means
     * they're not interacting with other players and their
     * health is full.
     */
    public void setFightable() {
        this.fightable = true;
    }

    public WorldPoint yieldLocation(Object obj) {
        return ((NPC) obj).getWorldLocation();
    }

    public Polygon yieldPolygon(Object obj) {
        return ((NPC) obj).getConvexHull();
    }

    public Object[] getCandidates() {
		NPCQuery query = new NPCQuery();

        if (nameTarget != null && nameTarget.length() > 0) {
            query.nameEquals(nameTarget);
        } else {
            query.idEquals(targets);
        }

        NPC[] result = getQueryRunner().runQuery(query);

        if (!fightable) return result;

        return Arrays.stream(result)
            .filter(npc -> npc.getInteracting() == null &&
                    npc.getHealthRatio() == npc.getHealth())
            .toArray();
    }
}
