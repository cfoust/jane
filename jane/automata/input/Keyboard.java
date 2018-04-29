package com.sqweebloid.jane.automata.input;

import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.client.ui.ClientUI;

import com.sqweebloid.jane.automata.Automaton;
import com.sqweebloid.jane.Utils;

/**
 * Types realistically. In the future it could make typing mistakes and fix
 * them.
 */
public class Keyboard extends Automaton {
    @Inject
    MouseState state;

    @Inject
    private ClientUI clientUI;

    private static final int WPM = 100;
    private static final int CHARACTERS_PER_WORD = 5;

    @Inject
    private Client client;

    private String text = "";

    public void setText(String text) {
        this.text = text;
    }

    private Component getTarget() {
        return clientUI.client.getComponent(0);
    }

    @Override
    public void run() {
        Component component = getTarget();
        int length = text.length();

        long interval = (long) Math.ceil((1 / (float) (WPM * CHARACTERS_PER_WORD)) * 60000);

        for (int i = 0; i < length; i++) {
            char character = text.charAt(i);

            component.dispatchEvent(new KeyEvent(component,
                        KeyEvent.KEY_TYPED,
                        System.currentTimeMillis(),
                        0,
                        KeyEvent.VK_UNDEFINED,
                        character));

            sleepExact(interval, 5);
        }
    }
}
