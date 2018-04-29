package com.sqweebloid.jane.automata.movement;

import com.sqweebloid.jane.automata.Builder;
import net.***REMOVED***.api.coords.WorldPoint;

public class MoverBuilder extends Builder {
    private Mover getMover() {
        return (Mover) automaton;
    }

    public MoverBuilder(MoveGraph.Node node) {
        automaton = new Mover(node);
    }

    public MoverBuilder(WorldPoint point) {
        automaton = new Mover(point);
    }
}
