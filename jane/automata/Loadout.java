package com.sqweebloid.jane.automata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Value;
import net.***REMOVED***.api.Item;
import net.***REMOVED***.api.coords.WorldPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines what the player's inventory and equipment should look like.
 * We don't really care about slots, just that the items are there.
 *
 * This class does NOT protect you against asking for impossible things.
 * The bot will just get stuck. Sorry.
 */
public class Loadout {
	private static final Logger logger = LoggerFactory.getLogger(Loadout.class);
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

    /**
     * Have at LEAST this many free slots.
     */
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

    private int getFreeSlots(Automaton automaton) {
        Item[] items = automaton.inventory.getInventoryItems();
        int freeSlots = 28 - items.length;

        for (Item item : items) {
            if (item.getId() == -1) freeSlots++;
        }

        return freeSlots;
    }

    /**
     * If there's a predicate involving free slots, bank
     * everything necessary to make it satisfied.
     */
    private boolean ensureFreeSlots(Automaton automaton) {
        // Construct a set of items that are otherwise already
        // handled by the other predicates.
        Set<Integer> ignore = new HashSet();
        int desired = -1;

        for (Predicate predicate : predicates) {
            int id = predicate.getId();

            if (id == -1) {
                desired = predicate.getQuantity();
                continue;
            }

            ignore.add(predicate.getId());
        }

        // There was never a clause that included free slots.
        // Just carry on.
        if (desired == -1) return true;

        // First count the free spots remaining.
        Item[] items = automaton.inventory.getInventoryItems();
        int freeSlots = getFreeSlots(automaton);

        // Don't worry if we have at least one free slot
        if (freeSlots >= 1) return true;

        int discrepancy = desired - freeSlots;

        // Go through and deposit any items we can.
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];

            if (item.getId() == -1) {
                freeSlots++;
                continue;
            }

            if (ignore.contains(item.getId())) continue;

            int quantity = item.getQuantity();

            // Just deposit all for untracked items.
            if (quantity == 1) {
                int before = getFreeSlots(automaton);
                automaton.bank(i).depositAll();
                desired -= getFreeSlots(automaton) - before;
                items = automaton.inventory.getInventoryItems();
            } else {
                automaton.bank(i).deposit(item.getQuantity());
                desired--;
            }

            if (desired == 0) break;
        }

        return desired == 0;
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

        boolean changed = false;
        for (Predicate predicate : predicates) {
            if (predicate.evaluate(items)) continue;

            int id = predicate.getId();
            int required = predicate.getQuantity();
            int actual = predicate.getCount(items);
            int amount = required - actual;

            if (id == -1) continue;

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

            changed = true;
            automaton.bank();

            int slot = (amount > 0) ?
                getSlot(automaton.inventory.getBankItems(), id) :
                getSlot(items, id);

            if (slot == -1) {
                logger.error("Could not find slot for item matching id {}", id);
                return false;
            }

            if (amount > 0) {
                automaton.bank(slot).withdraw(amount);
            } else {
                automaton.bank(slot).deposit(amount);
            }
        }

        // TODO: There's an edge case here that I don't feel like solving.
        // If your free slots add up to more than you have after resolving
        // the predicates, that probably means that there's an AT_LEAST
        // clause that has a surplus. We could handle this, but it's probably
        // not a big problem for now.

        if (!ensureFreeSlots(automaton)) return false;

        // Go home.
        if (changed) automaton.go(position);
        return true;
    }
}
