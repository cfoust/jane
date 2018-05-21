package com.sqweebloid.jane;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import javax.inject.Singleton;
import org.someclient.api.events.WidgetHiddenChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqweebloid.jane.automata.*;
import com.sqweebloid.jane.automata.events.*;
import com.sqweebloid.jane.automata.procedures.*;

/**
 * Essentially the planner that controls
 * what should happen during this session.
 */
@Singleton
public class Supervisor {
	private static final Logger logger = LoggerFactory.getLogger(Supervisor.class);

    ExecutorService executor;

	Injector injector;

    Deque<Automaton> stack = new ArrayDeque<Automaton>();

    public Supervisor(ExecutorService executor, Injector injector) {
        this.executor = executor;
        this.injector = injector;
    }

    private final long MINUTE = 60 * 1000;
    private final long HOUR = 60 * MINUTE;

    private String getCurrentName() {
        if (stack.peek() == null) return "";
        return stack.peek().getClass().getSimpleName();
    }

    /**
     * Push an automaton on the plugin's stack, pausing
     * the one below it if necessary.
     */
    public void push(Automaton automaton) {
        pause();

        automaton.setExecutor(executor);

        // This injects all of the automaton's dependencies
        // specified with @Inject.
        injector.injectMembers(automaton);
        stack.push(automaton);

        // When the automaton exits we want to resume
        // the automaton below it on the stack.
        (new Thread(() -> {
            String name = getCurrentName();

            Callable<Boolean> predicate = automaton.getUntil();

            if (predicate == null) {
                automaton.run();
            } else {
                try {
                    while (!predicate.call()) automaton.run();
                } catch (Exception e) {
                    logger.error("There was an error executing the predicate.");
                    e.printStackTrace();
                }
            }

            pop();
            onStopped(name);
        })).start();
    }

    private void onStopped(String automaton) {
        if (automaton.equals("LogIn")) {
            logger.info("Logged in");
        }
    }

    private void resume() {
        Automaton top = stack.peek();

        if (top == null) return;

        StateMachine machine = top.getMachine();
        if (machine == null) return;

        machine.resume();
    }

    private void pause() {
        Automaton top = stack.peek();

        if (top == null) return;

        StateMachine machine = top.getMachine();
        if (machine == null) return;

        machine.pause();
    }

    public void pop() {
        executor.submit(() -> {
            Automaton top = stack.pop();

            if (top == null) return;

            StateMachine machine = top.getMachine();
            if (machine != null) {
                machine.stop();
            }

            resume();
        });
    }

    public void stopAllAutomata() {
        while (stack.size() > 0) pop();
    }

    public void logIn() {
        push(new LogIn());
    }

    public void logOut() {
        push(new LogOut());
    }

	@Subscribe
	public void hideWidgets(WidgetHiddenChanged event) {
        if (!getCurrentName().equals("LevelUp") && LevelUp.shouldTrigger(event.getWidget())) {
            push(new LevelUp());
        }
    }
}
