package com.sqweebloid.jane;

import java.awt.event.KeyEvent;
import java.util.List;
import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.Item;
import net.***REMOVED***.api.NPC;
import net.***REMOVED***.api.queries.NPCQuery;
import net.***REMOVED***.client.input.KeyListener;
import net.***REMOVED***.client.plugins.jane.JanePlugin;
import net.***REMOVED***.client.util.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqweebloid.jane.automata.Banker;
import com.sqweebloid.jane.automata.ObjectEntity;
import com.sqweebloid.jane.automata.Test;
import com.sqweebloid.jane.automata.Dialogue;
import com.sqweebloid.jane.automata.Choice;
import com.sqweebloid.jane.automata.movement.MoveGraph;
import com.sqweebloid.jane.automata.movement.Mover;
import com.sqweebloid.jane.automata.skills.Woodcutting;
import com.sqweebloid.jane.controls.Input;

import com.sqweebloid.jane.controls.Inventory;

public class Keybinds implements KeyListener
{
	private static final Logger logger = LoggerFactory.getLogger(Keybinds.class);

    @Inject
    private JanePlugin plugin;

    @Inject
    private Client client;

    @Inject
    private Inventory inventory;

    @Inject
    private Input input;

    @Inject
    private QueryRunner queryRunner;

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
            plugin.getSupervisor().push(new Dialogue());
        } else if (e.getKeyCode() == KeyEvent.VK_F3) {
            Choice choice = new Choice();
            choice.setOption(1);
            plugin.getSupervisor().push(choice);
        } else if (e.getKeyCode() == KeyEvent.VK_F4) {
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
            input.dumpWidgets(true);
        } else if (e.getKeyCode() == KeyEvent.VK_F6) {
            // Hit this when you get a random.
            NPCQuery query = new NPCQuery();
            NPC[] result = queryRunner.runQuery(query);

            logger.info("Keybind NPC dump");
            for (NPC npc : result) {
                logger.info("{} {} {}", npc.getId(), npc.getName(), npc.getInteracting() == client.getLocalPlayer());
            }
        }
    }
}
