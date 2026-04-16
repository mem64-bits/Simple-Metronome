package com.github.mem64bits.simple.metronome.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.mem64bits.simple.metronome.config.CommandConfig;
import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandManager {
    private final Map<String, Command> registry = new HashMap<>();
    private final Queue<Command> inbox = new ConcurrentLinkedQueue<>();
    private final Deque<Command> history = new ArrayDeque<>();
    private final ObjectMapper objectMapper = new ObjectMapper();



    public CommandManager(){
        objectMapper.registerModule(new Jdk8Module());
    }
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

    public void loadFromJson(Path configPath) throws IOException {
        FileHandle configFileHandle = Gdx.files.internal(configPath.toString());
        if (!configFileHandle.exists()) {
            throw new IOException("Command config file not found: " + configPath);
        }
        String jsonContent = configFileHandle.readString();

        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, CommandConfig.class);
        List<CommandConfig> configs = objectMapper.readValue(jsonContent, listType);

        registry.clear(); // Clear old commands if reloading
        for (CommandConfig config : configs) {
            try {
                // The DTO itself acts as the factory
                Command cmd = config.toCommand();
                registerCommand(config.id(), cmd);
                System.out.println("[CommandManager] Registered command: " + config.id());
            } catch (Exception e) {
                System.err.println("[CommandManager] Error registering command " + config.id() + ": " + e.getMessage());
            }
        }
    }
}
