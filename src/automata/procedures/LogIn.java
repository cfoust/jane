package com.sqweebloid.jane.automata.procedures;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import org.someclient.api.GameState;

import com.sqweebloid.jane.automata.Automaton;

public class LogIn extends Automaton {
    private final static String USERNAME = "JANE_USERNAME";
    private final static String PASSWORD = "JANE_PASSWORD";

    private final int WORLDS_PER_COLUMN = 18;
    private final int[] FREE_WORLDS = new int[] {
        0, 7, 15, 25, 34, 75, 76, 77, 86, 87
    };

    private final int BUTTON_WIDTH = 78;
    private final int BUTTON_HEIGHT = 19;
    private final int BUTTON_GAP = 5;
    private final int HORIZONTAL_BUTTON_GAP = 94;
    private final int TOP_LEFT_X = 151;
    private final int TOP_LEFT_Y = 51;

    @Override
    public void run() {
        int worldIndex = FREE_WORLDS[rand(FREE_WORLDS.length)];

        int worldRow = worldIndex % WORLDS_PER_COLUMN;
        int worldColumn = (worldIndex - worldRow) / WORLDS_PER_COLUMN;

        int worldX = TOP_LEFT_X + (worldColumn * HORIZONTAL_BUTTON_GAP);
        int worldY = TOP_LEFT_Y + (worldRow * (BUTTON_HEIGHT + BUTTON_GAP));

        Rectangle world = new Rectangle(worldX, worldY, BUTTON_WIDTH, BUTTON_HEIGHT);

        String username = System.getenv(USERNAME);
        String password = System.getenv(PASSWORD);

        if (username == null || password == null) {
            logger.error("Username and/or password not set. Not logging in.");
            return;
        }

        while (!client.getGameState().equals(GameState.LOGGED_IN)) {
            sleep().most();
            // Click cancel (to prevent issue with failing to log in)
            mouse(473, 294).left();
            mouse(49, 472).left();
            sleep().more();
            mouse(world).left();
            sleep().some();
            mouse(473, 294).left(); // Click existing user
            sleep().some();
            type(username);
            sleep().some();
            input.typeKey(KeyEvent.VK_ENTER);
            sleep().some();
            type(password);
            mouse(283, 320).left(); // Login
            sleep().most();

            //// Play bounds
            mouse(new Rectangle(270, 294, 494 - 270, 381 - 294)).left();
            sleep().most();
        }
    }
}
