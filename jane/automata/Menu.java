package com.sqweebloid.jane.automata;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.MenuEntry;
import net.***REMOVED***.api.Point;
import net.***REMOVED***.client.util.Text;

import com.sqweebloid.jane.automata.Automaton;

/**
 * Clicks buttons on the game tooltip.
 */
public class Menu extends Automaton {
    List<Rectangle> bounds = new ArrayList();

    @Inject
    MenuState state;

    private String verb;
    private String target = "";

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isTarget(MenuEntry entry) {
        return entry.getOption().equals(verb) &&
            (target.length() == 0 || Text.removeTags(entry.getTarget()).equals(target));
    }

    private void dumpEntries(List<MenuEntry> entries) {
        for (MenuEntry entry : entries) {
            logger.info("'{}' '{}'", entry.getOption(), Text.removeTags(entry.getTarget()));
        }
    }

    @Override
    public void run() {
        List<MenuEntry> entries = state.getEntries();
        bounds.clear();

        if (entries.size() == 0) {
            logger.info("Menu had no entries.");
            return;
        }

        final int BASE_HEIGHT = 18;
        final int LINE_HEIGHT = 15;
        int x = client.getMenuX();
        int y = client.getMenuY();
        int i = 0;
        int width = 100; // TODO use text width

        y += BASE_HEIGHT;

        for (MenuEntry entry : entries) {
            Rectangle bound = new Rectangle(x, y, width, LINE_HEIGHT);
            y += LINE_HEIGHT;
            bounds.add(bound);
            i++;
        }

        i = 0;

        for (i = 0; i < entries.size(); i++) {
            MenuEntry entry = entries.get(i);
            String entryVerb = entry.getOption();
            String entryTarget = Text.removeTags(entry.getTarget());

            if (!isTarget(entry)) {
                continue;
            }

            mouse(bounds.get(i)).left();
            return;
        }
    }
}
