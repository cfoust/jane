package com.sqweebloid.jane.automata.events;

import java.awt.Rectangle;
import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.api.widgets.WidgetItem;
import static net.***REMOVED***.api.widgets.WidgetID.LEVEL_UP_GROUP_ID;
import static net.***REMOVED***.api.widgets.WidgetID.DIALOG_SPRITE_GROUP_ID;
import static net.***REMOVED***.api.widgets.WidgetInfo.TO_GROUP;

import com.sqweebloid.jane.automata.Automaton;
import com.sqweebloid.jane.controls.Input;
import net.***REMOVED***.client.plugins.jane.JanePlugin;

/**
 * Clicks on the level up dialog until it goes away.
 */
public class LevelUp extends Automaton {

    public static boolean shouldTrigger(Widget widget) {
        return (widget != null &&
                !widget.isHidden() &&
                TO_GROUP(widget.getId()) == LEVEL_UP_GROUP_ID);
    }

    public Widget getLevelUpWidget() {
        return client.getWidget(WidgetInfo.LEVEL_UP_LEVEL);
    }

    public String getText() {
        Widget levelUp = getLevelUpWidget();

        if (levelUp == null) return null;
        return levelUp.getText();
    }

    @Override
    public void run() {
        String text = getText();

        if (text != null) {
            push.sendShot(text);
        } else {
            push.sendShot("You leveled up in something.");
        }

        // The main levelup widget
        Widget levelUp = getLevelUpWidget();
        while (shouldTrigger(levelUp)) {
            sleep().some();
            mouse(new Rectangle(93, 435, 390, 17)).left();
            sleep().some();
        }

        sleep().more();

        // The dialog widget that might say what you can do now
        Widget dialog = client.getWidget(WidgetInfo.DIALOG_SPRITE);
        while (dialog != null && !dialog.isHidden()) {
            mouse(new Rectangle(116, 441, 380, 17)).left();
            sleep().some();
        }
    }
}
