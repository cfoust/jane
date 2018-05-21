package com.sqweebloid.jane.automata.procedures;

import java.awt.Rectangle;
import org.someclient.api.GameState;

import com.sqweebloid.jane.automata.Automaton;

public class LogOut extends Automaton {
    @Override
    public void run() {
        while (!client.getGameState().equals(GameState.LOGIN_SCREEN)) {
            mouse(new Rectangle(637, 475, 653 - 637, 494 - 475)).left();
            sleep().more();
            mouse(new Rectangle(577, 423, 706 - 577, 446 - 423)).left();
            sleep().more();
        }
    }
}
