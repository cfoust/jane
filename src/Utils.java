package com.sqweebloid.jane;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static void sleep(long delay) {
        try { Thread.sleep(delay); } catch (Exception e) {}
    }

    public static int rand(int max) {
        return ThreadLocalRandom.current().nextInt(0, max);
    }
}
