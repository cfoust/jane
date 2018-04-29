package com.sqweebloid.jane.controls;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.Item;
import net.***REMOVED***.api.Point;
import net.***REMOVED***.api.Query;
import net.***REMOVED***.api.queries.BankItemQuery;
import net.***REMOVED***.api.queries.WidgetItemQuery;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.api.widgets.WidgetItem;
import net.***REMOVED***.client.util.QueryRunner;

/**
 * Deposit and withdraw things from the bank.
 */
@Singleton
public class Bank {
    //@Inject
    //private Input input;

    //@Inject
    //private Client client;

    //@Inject
    //private World world;

    //@Inject
    //private Inventory inventory;

    //@Inject
    //private QueryRunner queryRunner;

    //private final int[] BOOTH_ID = new int[]{6943, 7409};
    //public final int BOX_ID = 6948;

    //public boolean isDepositBoxOpen() {
        //Widget box = client.getWidget(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER);
        //return box != null && !box.isHidden();
    //}

    //public boolean isBoothOpen() {
        //Widget box = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        //return box != null && !box.isHidden();
    //}

    //public boolean isBankOpen() {
        //return isDepositBoxOpen() || isBoothOpen();
    //}

    //public void openBank() {
        //while (!isBankOpen()) {
            //world.clickObject(BOOTH_ID);
            //input.sleep(4000 + input.rand(3000));
        //}
    //}

    //private Widget[] getBoxWidgets() {
        //Widget box = client.getWidget(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER);

        //if (box == null) return new Widget[0];

        //return box.getDynamicChildren();
    //}

    //private Widget[] getBankWidgets() {
        //Widget box = client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);

        //if (box == null) return new Widget[0];

        //return box.getDynamicChildren();
    //}

    //public WidgetItem[] getById(int... ids) {
		//WidgetItemQuery query = new BankItemQuery();
        //query.idEquals(ids);
		//return queryRunner.runQuery(query);
    //}

    //public boolean withdrawAll(int id) {
        //WidgetItem[] candidates = getById(id);
        //if (candidates.length == 0) return false;

        //input.rightClickBounds(candidates[0].getCanvasBounds());
        //input.delay(1000);
        //input.clickMenu("Withdraw-All", "");
        //return true;
    //}

    //public void depositAllInSlot(int slot) {
        //Widget[] slots = isDepositBoxOpen() ? getBoxWidgets() : getBankWidgets();

        //for (int i = 0; i < slots.length; i++) {
            //if (i != slot) continue;

            //Widget child = slots[i];
            //if (child.getItemId() == -1) continue;

            //Point p = input.getPointInBounds(child.getBounds());
            //input.rightClick(p.getX(), p.getY());
            //input.delay(1000);
            //input.rightClick(p.getX(), p.getY());
            //input.delay(1000);
            //input.clickMenu("Deposit-All", "");
            //input.delay(1000);
        //}
    //}

    //public void depositEverything() {
        //while (inventory.numFreeSlots() != 28) {
            //inventory.ensureOpen();

            //Item[] items = inventory.getItems();

            //int slot = 0;
            //while (slot >= items.length || items[slot].getId() == -1) slot = input.rand(28);

            //depositAllInSlot(slot);
            //input.sleep(1000);
        //}
    //}
}
