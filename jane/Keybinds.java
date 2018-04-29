package com.sqweebloid.jane;

import java.awt.event.KeyEvent;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.client.input.KeyListener;

import net.***REMOVED***.client.plugins.jane.JanePlugin;
import com.sqweebloid.jane.automata.movement.MoveGraph;
import com.sqweebloid.jane.automata.movement.Mover;
import com.sqweebloid.jane.automata.Menu;
import com.sqweebloid.jane.controls.Input;

public class Keybinds implements KeyListener
{
    @Inject
    private Input input;

    @Inject
    private JanePlugin plugin;

    @Inject
    private Client client;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            input.dumpWidgets(true);
        } else if (e.getKeyCode() == KeyEvent.VK_F2) {
        } else if (e.getKeyCode() == KeyEvent.VK_F3) {
            Menu menu = new Menu();
            menu.setVerb("Withdraw-All");
            plugin.getSupervisor().push(menu);
        } else if (e.getKeyCode() == KeyEvent.VK_F4) {
            plugin.getSupervisor().push(new Mover(MoveGraph.Node.GRAND_EXCHANGE));
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
        } else if (e.getKeyCode() == KeyEvent.VK_F6) {
        }
    }
}
