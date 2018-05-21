package com.sqweebloid.jane.automata.tools;

import com.sqweebloid.jane.Utils;
import com.sqweebloid.jane.automata.Automaton;

/**
 * Sleeps the thread. Used so that we can increase intervals
 * globally if necessary.
 */
public class Sleeper extends Automaton {
    public enum Length {
        BRIEFLY(250, 100),
        SOME(600, 200),
        MORE(1300, 200),
        MOST(4000, 2000),
        BREAK(60000, 60000);

        private long base;
        private long variance;

        long getBase() {
            return base;
        }

        long getVariance() {
            return variance;
        }

        Length(long base, long variance) {
            this.base = base;
            this.variance = variance;
        }
    };

    Length time = Length.BRIEFLY;

    public void setLength(Length time) {
        this.time = time;
    }

    @Override
    public void run() {
        sleepExact(time.getBase(), time.getVariance());
    }
}
