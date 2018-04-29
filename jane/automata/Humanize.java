package com.sqweebloid.jane.automata;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.inject.Inject;

import com.sqweebloid.jane.controls.Input;
import com.sqweebloid.jane.Constants;

/**
 * Make it seem like a human is playing by taking breaks
 * and clicking random things on the screen.
 */
public class Humanize extends Automaton {
    public static void randomlyMoveViewport(Input input) {
        int lateral = input.rand(2) == 1 ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT;
        input.pressKey(lateral);
        input.sleep(500 + input.rand(500));
        input.pressKey(KeyEvent.VK_UP);
        input.sleep(500 + input.rand(500));
        input.releaseKey(KeyEvent.VK_UP);
        input.sleep(250 + input.rand(250));
        input.releaseKey(lateral);
    }

    @Override
    public void run() {
        if (rand(15) == 1) {
            long breakTime = 30000 + rand(20000);
            logger.info("Taking a break for %dms.", breakTime);
            input.sleep(breakTime);
            logger.info("Done taking a break.");
            return;
        }

        Constants.TAB[] values = Constants.TAB.values();

        int numToClick = rand(values.length);
        boolean[] clicked = new boolean[values.length];

        for (int i = 0; i < numToClick; i++) {
            int index = rand(values.length);
            while (clicked[index]) index = rand(values.length);

            Constants.TAB target = values[index];
            clicked[index] = true;
            input.click(target.getBounds());
            input.sleep(2000 + rand(2000));
        }
    }
}
