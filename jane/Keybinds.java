package com.sqweebloid.jane;

import java.awt.event.KeyEvent;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.client.input.KeyListener;

import net.***REMOVED***.client.plugins.jane.JanePlugin;
import com.sqweebloid.jane.controls.Input;
import com.sqweebloid.jane.automata.movement.MoveGraph;
import com.sqweebloid.jane.automata.movement.Mover;
import com.sqweebloid.jane.automata.Banker;
import com.sqweebloid.jane.automata.skills.Woodcutting;
import com.sqweebloid.jane.automata.ObjectEntity;
import com.sqweebloid.jane.automata.Test;
import net.***REMOVED***.api.Item;

import com.sqweebloid.jane.controls.Inventory;

public class Keybinds implements KeyListener
{
    @Inject
    private JanePlugin plugin;

    @Inject
    private Client client;

    @Inject
    private Inventory inventory;

    @Inject
    private Input input;

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
            plugin.getSupervisor().push(new Woodcutting());
        } else if (e.getKeyCode() == KeyEvent.VK_F2) {
            input.dumpWidgets(true);
        } else if (e.getKeyCode() == KeyEvent.VK_F3) {
        } else if (e.getKeyCode() == KeyEvent.VK_F4) {
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
        } else if (e.getKeyCode() == KeyEvent.VK_F6) {
        }
    }
}
