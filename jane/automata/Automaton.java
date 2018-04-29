package com.sqweebloid.jane.automata;

import java.awt.Rectangle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import javax.inject.Inject;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.Player;
import net.***REMOVED***.api.Point;
import net.***REMOVED***.api.coords.LocalPoint;
import net.***REMOVED***.api.coords.WorldPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqweebloid.jane.Push;

import net.***REMOVED***.client.plugins.jane.JanePlugin;
import com.sqweebloid.jane.automata.input.*;
import com.sqweebloid.jane.automata.movement.*;
import com.sqweebloid.jane.controls.*;

/**
 * Plans and performs a particular action in its own thread.
 * Can be paused.
 */
public class Automaton implements Runnable {
	protected Logger logger;

    @Inject
    public Client client;

    @Inject
    public Input input;

    @Inject
    public Moving moving;

    @Inject
    public Inventory inventory;

    @Inject
    public PathFinder pathFinder;

    @Inject
    public JanePlugin plugin;

    @Inject
    public Push push;

    protected StateMachine machine;

    private ExecutorService executor;

    public Automaton() {
        this.executor = null;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
        machine = new StateMachine(executor);
    }

    /**
     * Hacky as hell but I don't know any better right now.
     */
    public Builder inject(Builder builder) {
        builder.setPlugin(plugin);
        builder.setParent(this);
        return builder;
    }

    public StateMachine getMachine() {
        return machine;
    }

    /**
     * Log a message.
     */
    public void log(String text) {
        logger.info(text);
    }

    protected int rand(int max) {
        return ThreadLocalRandom.current().nextInt(0, max);
    }

    /**
     * This is the method that subclasses override to perform
     * their action.
     */
    public void run() {
    }

    //////////////////////////////////////////
    // QUASI-DSL FOR WRITING BOT FUNCTIONALITY
    //////////////////////////////////////////
    // The below looks messy but makes writing
    // automata really nice.
    //////////////////////////////////////////
    public MouseBuilder mouse(int x, int y) {
        MouseBuilder mouse = new MouseBuilder(new Point(x, y));
        inject(mouse);
        return mouse;
    }

    public MouseBuilder mouse(Rectangle rect) {
        MouseBuilder mouse = new MouseBuilder(input.getPointInBounds(rect));
        inject(mouse);
        return mouse;
    }

    public void type(String text) {
        KeyboardBuilder keys = new KeyboardBuilder(text);
        inject(keys);

        // There's no way to accept more input, so that's that.
        keys.done();
    }

    public SleeperBuilder sleep() {
        SleeperBuilder sleeper = new SleeperBuilder();
        inject(sleeper);
        return sleeper;
    }

    public void go(MoveGraph.Node node) {
        MoverBuilder sleeper = new MoverBuilder(node);
        inject(sleeper);
        sleeper.done();
    }

    public void go(WorldPoint point) {
        MoverBuilder sleeper = new MoverBuilder(point);
        inject(sleeper);
        sleeper.done();
    }

    public MenuBuilder menu(String verb) {
        MenuBuilder clicker = new MenuBuilder(verb);
        inject(clicker);
        return clicker;
    }

    public ObjectEntityBuilder object(int target) {
        ObjectEntityBuilder clicker = new ObjectEntityBuilder(target);
        inject(clicker);
        return clicker;
    }

    public ObjectEntityBuilder object(int... targets) {
        ObjectEntityBuilder clicker = new ObjectEntityBuilder(targets);
        inject(clicker);
        return clicker;
    }

    public NPCEntityBuilder npc(int target) {
        NPCEntityBuilder clicker = new NPCEntityBuilder(target);
        inject(clicker);
        return clicker;
    }

    public NPCEntityBuilder npc(int... targets) {
        NPCEntityBuilder clicker = new NPCEntityBuilder(targets);
        inject(clicker);
        return clicker;
    }

    public NPCEntityBuilder npc(String target) {
        NPCEntityBuilder clicker = new NPCEntityBuilder(target);
        inject(clicker);
        return clicker;
    }

    //////////////////////////////////////////

    protected void sleepExact(long delay) {
        try { Thread.sleep(delay); } catch (Exception e) {}
    }

    protected void sleepExact(long base, long variance) {
        sleepExact(base + (long) rand((int) variance));
    }

    protected LocalPoint getLocalLocation() {
        return client.getLocalPlayer().getLocalLocation();
    }

    protected WorldPoint getWorldLocation() {
        return client.getLocalPlayer().getWorldLocation();
    }

    protected Player getPlayer() {
        return client.getLocalPlayer();
    }
}
