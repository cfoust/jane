package com.sqweebloid.jane.controls;

import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.InventoryID;
import net.***REMOVED***.api.Item;
import net.***REMOVED***.api.ItemContainer;
import net.***REMOVED***.api.Player;
import net.***REMOVED***.api.Query;
import net.***REMOVED***.api.queries.InventoryWidgetItemQuery;
import net.***REMOVED***.api.queries.WidgetItemQuery;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.api.widgets.WidgetItem;
import com.sqweebloid.jane.Constants;
import net.***REMOVED***.client.util.QueryRunner;
import net.***REMOVED***.client.plugins.jane.JanePlugin;

/**
 * Performs everything related to inventory management.
 */
public class Inventory {
    @Inject
    private Client client;

    @Inject
    private Input input;

    private final QueryRunner queryRunner;

    @Inject
    public Inventory(QueryRunner queryRunner) {
        this.queryRunner = queryRunner;
    }

    public ItemContainer getBags() {
        return client.getItemContainer(InventoryID.INVENTORY);
    }

    public int numFreeSlots() {
        int freeSlots = 28;

        if (getBags() == null) {
            return freeSlots;
        }

        for (Item item : getBags().getItems()) {
            if (item.getId() == -1 || item.getQuantity() == 0) continue;
            freeSlots--;
        }

        return freeSlots;
    }

    public boolean isFull() {
        return numFreeSlots() == 0;
    }

    public Item[] getItems() {
        return getBags().getItems();
    }

    // Make sure we're on the inventory tab.
    public void ensureOpen() {
        Widget bags = client.getWidget(WidgetInfo.INVENTORY);

        if (bags.isHidden()) {
            input.click(Constants.TAB.INVENTORY.getBounds());
        }
    }

    public WidgetItem[] getById(int... ids) {
        ensureOpen();
		WidgetItemQuery inventoryQuery = new InventoryWidgetItemQuery();

        for (int id : ids) {
            inventoryQuery.idEquals(id);
        }

		return queryRunner.runQuery(inventoryQuery);
    }
}
