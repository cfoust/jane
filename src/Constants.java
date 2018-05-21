package com.sqweebloid.jane;

import java.awt.Rectangle;

public class Constants {
    public enum TAB {
        COMBAT(new Rectangle(534, 177, 548 - 534, 197 - 177)),
        STATS(new Rectangle(564, 178, 583 - 564, 197 - 178)),
        QUESTS(new Rectangle(594, 178, 583 - 564, 197 - 178)),
        INVENTORY(new Rectangle(624, 178, 583 - 564, 197 - 178)),
        EQUIPMENT(new Rectangle(654, 178, 583 - 564, 197 - 178)),
        PRAYER(new Rectangle(684, 178, 583 - 564, 197 - 178));

        Rectangle bounds;

        TAB(Rectangle r) {
            this.bounds = r;
        }

        public Rectangle getBounds() {
            return bounds;
        }
    };
}
