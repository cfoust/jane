package com.sqweebloid.jane.automata;

import javax.inject.Inject;
import net.***REMOVED***.api.Point;

import com.sqweebloid.jane.automata.Automaton;
import net.***REMOVED***.client.plugins.jane.JanePlugin;

/**
 * Helper class for making new automata.
 */
public class Builder {
    private JanePlugin plugin;
    protected Automaton parent;
    protected Automaton automaton;

    public void setPlugin(JanePlugin plugin) {
        this.plugin = plugin;
    }

    public void setParent(Automaton parent) {
        this.parent = parent;
    }

    public void done() {
        plugin.getSupervisor().push(automaton);

        // Pause the parent's execution
        parent.getMachine().checkPause();
    }
}
