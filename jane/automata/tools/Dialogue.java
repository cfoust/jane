package com.sqweebloid.jane.automata.tools;

import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetID;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.client.util.Text;

import com.sqweebloid.jane.automata.Automaton;

/**
 * This just talks until the NPC shuts up.
 */
public class Dialogue extends Automaton {
    private static final int DIALOG_PLAYER_GROUP = 217;
    private static final int DIALOG_PLAYER_CONTINUE = 2;
    private static final int DIALOG_PLAYER_TEXT = 3;

    private boolean isVisible(Widget widget) {
        return widget != null && !widget.isHidden();
    }

    private String getText(Widget widget) {
        if (widget == null) return null;
        return widget.getText();
    }

    private Widget getPlayerWidget() {
        return client.getWidget(DIALOG_PLAYER_GROUP, DIALOG_PLAYER_TEXT);
    }

    private Widget getNPCWidget() {
        return client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
    }

    private Widget getPlayerContinue() {
        return client.getWidget(DIALOG_PLAYER_GROUP, DIALOG_PLAYER_CONTINUE);
    }

    private Widget getNPCContinue() {
        return client.getWidget(WidgetInfo.DIALOG_NPC_CONTINUE);
    }

    private boolean playerIsVisible() {
        return isVisible(getPlayerWidget());
    }

    private boolean NPCIsVisible() {
        return isVisible(getNPCWidget());
    }

    private String getPlayerText() {
        return getText(getPlayerWidget());
    }

    private String getNPCText() {
        return getText(getNPCWidget());
    }

    private static final int WPM = 212;
    private void read(String text) {
        int words = Text.removeTags(text).split(" ").length;
        long time = (long) Math.ceil((((double) words) / ((double) WPM)) * 60000);
        sleep().some();
        sleepExact(time);
        sleep().some();
    }

    @Override
    public void run() {
        while (NPCIsVisible() || playerIsVisible()) {
            String text = NPCIsVisible() ? getNPCText() : getPlayerText();
            read(text);

            Widget continueText = NPCIsVisible() ? getNPCContinue() : getPlayerContinue();

            if (!continueText.isHidden()) {
                mouse(continueText.getBounds()).left();
            }

            sleep().more();
        }
    }
}
