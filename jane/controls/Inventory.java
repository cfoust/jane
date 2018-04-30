package com.sqweebloid.jane.controls;

import com.sqweebloid.jane.Constants;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.InventoryID;
import net.***REMOVED***.api.Item;
import net.***REMOVED***.api.ItemContainer;
import net.***REMOVED***.api.Player;
import net.***REMOVED***.api.Query;
import net.***REMOVED***.api.queries.InventoryWidgetItemQuery;
import net.***REMOVED***.api.queries.BankItemQuery;
import net.***REMOVED***.api.queries.WidgetItemQuery;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.api.widgets.WidgetItem;
import net.***REMOVED***.client.plugins.jane.JanePlugin;
import net.***REMOVED***.client.util.QueryRunner;

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

    // From RuneLite
    private List<WidgetItem> getWidgets(WidgetInfo info) {
        Widget inventory = client.getWidget(info);
        List<WidgetItem> items = new ArrayList();

        if (inventory == null || inventory.isHidden()) {
            return null;
        }

        if (info == WidgetInfo.INVENTORY) {
            items.addAll(inventory.getWidgetItems());
        } else {
            Widget[] children = inventory.getDynamicChildren();

            for (int i = 0; i < children.length; i++) {
                // set bounds to same size as default inventory
                Rectangle bounds = children[i].getBounds();
                bounds.setBounds(bounds.x - 1, bounds.y - 1, 32, 32);
                items.add(new WidgetItem(children[i].getItemId(), children[i].getItemQuantity(), i, bounds));
            }
        }

        return items;
    }

    public ItemContainer getInventory() {
        return client.getItemContainer(InventoryID.INVENTORY);
    }

    public Item[] getInventoryItems() {
        ItemContainer inventory = getInventory();
        if (inventory == null) return new Item[0];
        return inventory.getItems();
    }

    public List<WidgetItem> getInventoryWidgets() {
		Query inventoryQuery = new InventoryWidgetItemQuery();
		return Arrays.asList(queryRunner.runQuery(inventoryQuery));
    }

    public ItemContainer getBank() {
        return client.getItemContainer(InventoryID.BANK);
    }

    public Item[] getBankItems() {
        ItemContainer inventory = getBank();
        if (inventory == null) return new Item[0];
        return inventory.getItems();
    }

    public List<WidgetItem> getBankWidgets() {
		Query bankQuery = new BankItemQuery();
		return Arrays.asList(queryRunner.runQuery(bankQuery));
    }

    public ItemContainer getEquipment() {
        return client.getItemContainer(InventoryID.EQUIPMENT);
    }

    public Item[] getEquipmentItems() {
        ItemContainer inventory = getEquipment();
        if (inventory == null) return new Item[0];
        return inventory.getItems();
    }

    public List<WidgetItem> getEquipmentWidgets() {
        return getWidgets(WidgetInfo.EQUIPMENT_INVENTORY_ITEMS_CONTAINER);
    }

    public int numFreeSlots() {
        int freeSlots = 28;

        if (getInventory() == null) {
            return freeSlots;
        }

        for (Item item : getInventory().getItems()) {
            if (item.getId() == -1 || item.getQuantity() == 0) continue;
            freeSlots--;
        }

        return freeSlots;
    }

    public boolean isFull() {
        return numFreeSlots() == 0;
    }

    public WidgetItem[] getById(int... ids) {
		WidgetItemQuery inventoryQuery = new InventoryWidgetItemQuery();

        for (int id : ids) {
            inventoryQuery.idEquals(id);
        }

		return queryRunner.runQuery(inventoryQuery);
    }

    public String formatItem(Item item) {
        return String.format("Item{id=%d, quantity=%d}", item.getId(), item.getQuantity());
    }

    public void dumpItems() {
        System.out.println("Dumping items");

        System.out.println("INVENTORY");
        for (Item item : getInventoryItems()) {
            System.out.println(formatItem(item));
        }

        System.out.println("BANK");
        for (Item item : getBankItems()) {
            System.out.println(formatItem(item));
        }
    }

    public void dumpWidgets() {
        System.out.println("Dumping widgets");

        System.out.println("INVENTORY");
        for (WidgetItem item : getInventoryWidgets()) {
            System.out.println(item);
        }

        System.out.println("BANK");
        for (WidgetItem item : getBankWidgets()) {
            System.out.println(item);
        }
    }
}
