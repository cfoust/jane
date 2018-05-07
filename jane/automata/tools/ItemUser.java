package com.sqweebloid.jane.automata.tools;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import net.***REMOVED***.api.Item;
import net.***REMOVED***.api.Query;
import net.***REMOVED***.api.queries.BankItemQuery;
import net.***REMOVED***.api.queries.WidgetItemQuery;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.api.widgets.WidgetItem;
import net.***REMOVED***.client.util.QueryRunner;

import com.sqweebloid.jane.automata.Automaton;

/**
 * Uses items on things.
 */
public class ItemUser extends Automaton {
    private final int[] BOOTH_ID = new int[]{6943, 7409};

    // The item id we want to use.
    private int tool;

    // The target of the item we want to use.
    // Can be an object id or another item.
    private int reagent;

    // Whether or not the reagent is a game object.
    private boolean isObject;

    public void setTool(int tool) {
        this.tool = tool;
    }

    public void setGameObject(int reagent) {
        this.isObject = true;
        this.reagent = reagent;
    }

    public void setTarget(int reagent) {
        this.isObject = false;
        this.reagent = reagent;
    }

    private WidgetItem getWidget(int id) {
        List<WidgetItem> widgets = inventory.getInventoryWidgets();

        Optional<WidgetItem> targetWidget = widgets
            .stream()
            .filter(widget -> widget.getId() == id)
            .findFirst();

        if (!targetWidget.isPresent()) {
            return null;
        }

        return targetWidget.get();
    }

    @Override
    public void run() {
        ui().inventory();

        WidgetItem source = getWidget(tool);
        if (source == null) {
            logger.error("Could not find tool {}", tool);
            return;
        }

        mouse(source.getCanvasBounds()).right();
        sleep().some();
        menu("Use").done();
        sleep().some();

        if (isObject) {
            object(reagent).interact();
            return;
        }

        WidgetItem target = getWidget(reagent);
        if (target == null) {
            logger.error("Could not find reagent {}", reagent);
            return;
        }

        mouse(target.getCanvasBounds()).left();
    }
}
