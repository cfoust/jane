package com.sqweebloid.jane.automata.procedures;

import java.awt.Rectangle;
import net.***REMOVED***.api.GameState;

import com.sqweebloid.jane.automata.Automaton;

public class LogOut extends Automaton {
    @Override
    public void run() {
        while (!client.getGameState().equals(GameState.LOGIN_SCREEN)) {
            input.click(new Rectangle(637, 475, 653 - 637, 494 - 475));
            sleep().more();
            input.click(new Rectangle(577, 423, 706 - 577, 446 - 423));
            sleep().more();
        }
    }
}
