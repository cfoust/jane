package com.sqweebloid.jane.automata;

import java.awt.Rectangle;
import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.api.widgets.WidgetItem;
import static net.***REMOVED***.api.widgets.WidgetID.LEVEL_UP_GROUP_ID;
import static net.***REMOVED***.api.widgets.WidgetID.DIALOG_SPRITE_GROUP_ID;
import static net.***REMOVED***.api.widgets.WidgetInfo.TO_GROUP;

import net.***REMOVED***.client.plugins.jane.JanePlugin;
import com.sqweebloid.jane.controls.Input;

/**
 * Clicks on the level up dialog until it goes away.
 */
public class LevelUp extends Automaton {

    public static boolean shouldTrigger(Widget widget) {
        return (widget != null &&
                !widget.isHidden() &&
                TO_GROUP(widget.getId()) == LEVEL_UP_GROUP_ID);
    }

    @Override
    public void run() {
        Rectangle bound = new Rectangle(192, 448, 326 - 192, 455 - 448);

        for (int i = 0; i < 5; i++) {
            sleep().some();
            mouse(bound).left();
        }
    }
}
