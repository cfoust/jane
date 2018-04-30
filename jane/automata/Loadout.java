package com.sqweebloid.jane.automata;

import java.util.ArrayList;
import java.util.List;
import lombok.Value;
import net.***REMOVED***.api.Item;
import net.***REMOVED***.api.coords.WorldPoint;

/**
 * Defines what the player's inventory and equipment should look like.
 * We don't really care about slots, just that the items are there.
 *
 * This class does NOT protect you against asking for impossible things.
 * The bot will just get stuck. Sorry.
 */
public class Loadout {
    private static final int INVENTORY_SIZE = 28;

    private List<Predicate> predicates = new ArrayList();

    private enum Quantifier {
        EXACTLY,
        AT_LEAST,
        AT_MOST;
    }

    @Value
    private class Predicate {
        int id;
        int quantity;
        Quantifier quantifier;

        int getCount(Item[] inventory) {
            int count = 0;

            for (Item item : inventory) {
                if (id != item.getId()) continue;
                count += item.getQuantity();
            }

            // Empty slots
            if (id == -1) {
                count += INVENTORY_SIZE - inventory.length;
            }

            return count;
        }

        boolean evaluate(Item[] inventory) {
            int count = getCount(inventory);

            switch (quantifier) {
                case EXACTLY:
                    return count == quantity;
                case AT_LEAST:
                    return count >= quantity;
                case AT_MOST:
                    return count <= quantity;
            }

            return false;
        }
    }

    public void hasExactly(int id, int quantity) {
        predicates.add(new Predicate(id, quantity, Quantifier.EXACTLY));
    }

    public void hasAtMost(int id, int quantity) {
        predicates.add(new Predicate(id, quantity, Quantifier.AT_MOST));
    }

    public void hasAtLeast(int id, int quantity) {
        predicates.add(new Predicate(id, quantity, Quantifier.AT_LEAST));
    }

    public void hasFreeSpots(int quantity) {
        predicates.add(new Predicate(-1, quantity, Quantifier.AT_LEAST));
    }

    /**
     * Get the index of the first item with the given id
     * or -1 of no such item exists.
     */
    private int getSlot(Item[] items, int id) {
        if (items == null) return -1;

        for (int i = 0; i < items.length; i++) {
            if (items[i].getId() == id) return i;
        }

        return -1;
    }

    /**
     * Ensures that the predicates hold.
     * If they don't, goes to a bank and tries to make them work.
     * Returns false if the loadout is unresolvable.
     */
    public boolean ensure(Automaton automaton) {
        // The place we'll come back to.
        WorldPoint position = automaton.getWorldLocation();

        Item[] items = automaton.inventory.getInventoryItems();

        for (Predicate predicate : predicates) {
            if (predicate.evaluate(items)) continue;

            int id = predicate.getId();
            int required = predicate.getQuantity();
            int actual = predicate.getCount(items);
            int amount = required - actual;

            // holy shit this is pretty
            switch (predicate.getQuantifier()) {
                case AT_LEAST:
                    // Don't worry about it if we have too much.
                    if (amount < 0) amount = 0;
                    break;
                case AT_MOST:
                    // Also if we have too little.
                    if (amount > 0) amount = 0;
                    break;
                case EXACTLY:
                    break;
            }

            if (amount == 0) continue;
            
            automaton.bank();

            int slot = (amount > 0) ?
                getSlot(automaton.inventory.getBankItems(), id) :
                getSlot(items, id);

            if (slot == -1) return false;

            if (amount > 0) {
                automaton.bank(slot).withdraw(amount);
            } else {
                automaton.bank(slot).deposit(amount);
            }
        }

        // Go home.
        automaton.go(position);
        return true;
    }
}
