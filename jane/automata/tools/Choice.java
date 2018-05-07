package com.sqweebloid.jane.automata.tools;

import java.util.Arrays;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetID;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.client.util.Text;

import com.sqweebloid.jane.automata.Automaton;

/**
 * Chooses something inside of a dialogue.
 */
public class Choice extends Automaton {
    private static final int CHOICE_GROUP = 219;

    private boolean isVisible(Widget widget) {
        return widget != null && !widget.isHidden();
    }

    private Widget getChoiceWidget() {
        return client.getWidget(219, 0);
    }

    private String getText(Widget widget) {
        if (widget == null) return null;
        return widget.getText();
    }

    private Widget[] getOptions() {
        if (!isVisible(getChoiceWidget())) return null;

        Widget[] children = getChoiceWidget().getChildren();
        return Arrays.copyOfRange(children, 1, children.length - 2);
    }

    int optionNumber = 0;

    /**
     * Zero-indexed option number to choose.
     */
    public void setOption(int number) {
        optionNumber = number;
    }

    @Override
    public void run() {
        if (getOptions() == null) {
            logger.error("Choice widget is not on screen.");
            return;
        }

        Widget[] options = getOptions();

        if (optionNumber >= options.length) {
            logger.error("Option number {} is greater than options {}", optionNumber, options.length);
            return;
        }

        sleep().more();
        mouse(options[optionNumber].getBounds()).left();
    }
}

