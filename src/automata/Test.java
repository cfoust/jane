package com.sqweebloid.jane.automata;

public class Test extends Automaton {
    @Override
    public void run() {
        ui().inventory();
        use(2309).onItem(1205);
        use(2309).onObject(1278);
    }
}
