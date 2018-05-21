package com.sqweebloid.jane.automata.tools;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import net.***REMOVED***.api.Item;
import net.***REMOVED***.api.SpriteID;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetID;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.api.widgets.WidgetItem;

import com.sqweebloid.jane.automata.Automaton;

/**
 * Performs certain interface manipulations.
 */
public class Interface extends Automaton {
    /**
     * Gives a widget that should be showing and a sprite
     * to click if it's not.
     */
    public enum SpriteLink {
        COMBAT(593, 28, 168),
        STATS(320, 1, 898),
        QUESTS(399, 0, 776),
        INVENTORY(WidgetID.INVENTORY_GROUP_ID, 0, 884),
        EQUIPMENT(387, 17, 901),
        PRAYER(541, 4, 902),
        MAGIC(218, 1, 780),
        CLAN(7, 1, 895),
        FRIENDS(429, 13, 904),
        IGNORE(432, 11, 905),
        LOG_OUT(182, 12, 906),
        OPTIONS(261, 21, 907),
        EMOTE(216, 1, 908),
        MUSIC(239, 9, 909);

        int group;
        int child;
        int sprite;

        SpriteLink(int group, int child, int sprite) {
            this.group = group;
            this.child = child;
            this.sprite = sprite;
        }

        int getGroup() {
            return group;
        }

        int getChild() {
            return child;
        }

        int getSprite() {
            return sprite;
        }
    }

    SpriteLink link;

    public void setSprite(SpriteLink link) {
        this.link = link;
    }

    @Override
    public void run() {
        Widget target = client.getWidget(link.getGroup(), link.getChild());
        if (target == null) {
            logger.error("Target node did not exist");
            return;
        }

        if (!target.isHidden()) return;

        Widget touch = input.getWidgetWithSprite(link.getSprite());
        if (touch == null) {
            logger.error("Could not find widget by sprite {}", link.getSprite());
            return;
        }

        mouse(touch.getBounds()).left();
    }
}

