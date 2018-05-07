package com.sqweebloid.jane.automata.tools;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import net.***REMOVED***.api.MenuEntry;
import net.***REMOVED***.api.events.MenuOpened;

/**
 * Stores the entries in the menu if it's open.
 */
@Singleton
public class MenuState {
    private List<MenuEntry> entries = new ArrayList();

    public List<MenuEntry> getEntries() {
        return entries;
    }

    public void update(MenuOpened event) {
        entries.clear();

        for (MenuEntry entry : event.getMenuEntries()) {
            entries.add(0, entry);
        }
    }
}
