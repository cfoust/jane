package com.sqweebloid.jane.automata.tools.builders;

import javax.inject.Inject;
import net.***REMOVED***.api.Point;

import com.sqweebloid.jane.automata.Automaton;
import java.util.concurrent.Callable;
import net.***REMOVED***.client.plugins.jane.JanePlugin;

/**
 * Helper class for making new automata.
 */
abstract public class Builder {
    private JanePlugin plugin;
    protected Automaton parent;
    protected Automaton automaton;

    public void setPlugin(JanePlugin plugin) {
        this.plugin = plugin;
    }

    public void setParent(Automaton parent) {
        this.parent = parent;
    }

    public Builder until(Callable<Boolean> predicate) {
        automaton.setUntil(predicate);
        return this;
    }

    public void done() {
        plugin.getSupervisor().push(automaton);

        // Pause the parent's execution
        parent.getMachine().checkPause();
    }
}
