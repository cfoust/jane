package com.sqweebloid.jane.automata.tools.builders;

import net.***REMOVED***.api.coords.WorldPoint;

import com.sqweebloid.jane.automata.tools.movement.Mover;
import com.sqweebloid.jane.automata.tools.movement.MoveGraph;

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
