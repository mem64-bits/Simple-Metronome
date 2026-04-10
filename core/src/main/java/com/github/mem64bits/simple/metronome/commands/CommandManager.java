package com.github.mem64bits.simple.metronome.commands;

import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandManager {
    private final Map<String, Command> registry = new HashMap<>();
    private final Queue<Command> inbox = new ConcurrentLinkedQueue<>();
    private final Deque<Command> history = new ArrayDeque<>();

    public void registerCommand(String name, Command cmd) {
        registry.put(name.toUpperCase(), cmd);
    }

    /**
     * Pushes a command into the mailbox by its registered name.
     */
    public void invoke(String actionName) {
        Command cmd = registry.get(actionName.toUpperCase());
        if (cmd != null) {
            inbox.add(cmd);
        } else {
            System.err.println("[CommandManager] Unknown action: " + actionName);
        }
    }

    /**
     * Executes everything in the inbox.
     * Call this at the absolute START of your MetronomeEngine.update() loop.
     */
    public void processInbox(MetronomeEngine engine) {
        while (!inbox.isEmpty()) {
            Command cmd = inbox.poll();
            if (cmd != null) {
                cmd.execute(engine);
                // After execution, push to history for LIFO undoing
                history.push(cmd);
            }
        }
    }

    public void undoLast(MetronomeEngine engine) {
        if (!history.isEmpty()) {
            Command lastCmd = history.pop();
            lastCmd.undo(engine); // Requires 'undo' method in your Command interface
            System.out.println("[System] Undo executed for: " + lastCmd.getClass().getSimpleName());
        }
    }
}
