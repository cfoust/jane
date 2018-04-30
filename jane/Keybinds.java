package com.sqweebloid.jane;

import java.awt.event.KeyEvent;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.client.input.KeyListener;

import net.***REMOVED***.client.plugins.jane.JanePlugin;
import com.sqweebloid.jane.automata.movement.MoveGraph;
import com.sqweebloid.jane.automata.movement.Mover;
import com.sqweebloid.jane.automata.Banker;
import com.sqweebloid.jane.automata.ObjectEntity;
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
            Banker obj = new Banker();
            obj.setSlot(0);
            obj.setInto(true);
            obj.setAmount(1);
            plugin.getSupervisor().push(obj);
        } else if (e.getKeyCode() == KeyEvent.VK_F2) {
            inventory.dumpItems();
        } else if (e.getKeyCode() == KeyEvent.VK_F3) {
            inventory.dumpWidgets();
        } else if (e.getKeyCode() == KeyEvent.VK_F4) {
            Banker obj = new Banker();
            obj.setSlot(0);
            obj.setInto(false);
            obj.setAmount(2);
            plugin.getSupervisor().push(obj);
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
        } else if (e.getKeyCode() == KeyEvent.VK_F6) {
        }
    }
}
