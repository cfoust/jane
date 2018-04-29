package com.sqweebloid.jane.automata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.Query;
import net.***REMOVED***.api.queries.BankItemQuery;
import net.***REMOVED***.api.queries.WidgetItemQuery;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.api.widgets.WidgetItem;
import net.***REMOVED***.client.util.QueryRunner;

import com.sqweebloid.jane.automata.Automaton;
import com.sqweebloid.jane.automata.movement.MoveGraph;

/**
 * Deals with all things banking.
 */
public class Banker extends Automaton {
    enum Bank {
        VARROCK_WEST(MoveGraph.Node.VARROCK_WEST_BANK),
        ALKARID(MoveGraph.Node.ALKARID_BANK);

        MoveGraph.Node location;

        Bank(MoveGraph.Node node) {
            location = node;
        }

        MoveGraph.Node getNode() {
            return location;
        }
    }

    private final int[] BOOTH_ID = new int[]{6943, 7409};

    public boolean isBoothOpen() {
        Widget box = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        return box != null && !box.isHidden();
    }

    public MoveGraph.Node getNearestBank() {
        List<MoveGraph.Node> banks = new ArrayList();

        for (Bank bank : Bank.values()) {
            banks.add(bank.getNode());
        }

        // This is a bad approximation.
        Collections.sort(banks, (one, other) -> 
                getWorldLocation().distanceTo(one.getLocation()) - 
                getWorldLocation().distanceTo(other.getLocation()));

        return banks.get(0);
    }

    @Override
    public void run() {
        MoveGraph.Node closest = getNearestBank();
        int distance = closest.getLocation().distanceTo(getWorldLocation());

        if (!isBoothOpen()) {
            if (distance > 8) go(closest);
            object(BOOTH_ID).closest().interact("Bank");
        }
    }
}
