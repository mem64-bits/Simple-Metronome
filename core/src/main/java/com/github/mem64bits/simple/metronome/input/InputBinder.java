package com.github.mem64bits.simple.metronome.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.mem64bits.simple.metronome.config.InputConfig;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class InputBinder {
    private final Map<Integer, String> keyBindings = new HashMap<>();
    private final Map<Integer, String> mouseBindings = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InputBinder() {
        objectMapper.registerModule(new Jdk8Module());
    }

    /**
     * Loads and resolves keyboard/mouse bindings from JSON config
     */
    public void loadFromJson(Path configPath) throws IOException {
        FileHandle configFileHandle = Gdx.files.internal(configPath.toString());
        if (!configFileHandle.exists()) throw new IOException("File not found: " + configPath);

        InputConfig config = objectMapper.readValue(configFileHandle.readString(), InputConfig.class);

        keyBindings.clear();
        mouseBindings.clear();

        for (Map.Entry<String, String> entry : config.keyboard().entrySet()) {
            int code = InputCodeResolver.getInstance().getKeyCode(entry.getKey());
            // Resolves key code; stores binding if valid, logs unknown keys
            if (code != -1) {
                keyBindings.put(code, entry.getValue().toUpperCase());
            } else {
                System.err.println("[InputBinder] Unknown Key: " + entry.getKey());
            }
        }

        config.mouse().ifPresent(mouseMap -> {
            // Loads mouse bindings from config using resolved button codes
            for (Map.Entry<String, String> entry : mouseMap.entrySet()) {
                int code = InputCodeResolver.getInstance().getMouseButtonCode(entry.getKey());
                if (code != -1) {
                    mouseBindings.put(code, entry.getValue().toUpperCase());
                }
            }
        });

        System.out.println("[InputBinder] Loaded " + keyBindings.size() + " keys, " + mouseBindings.size() + " buttons.");
    }

    public String getAction(int keycode) {
        return keyBindings.getOrDefault(keycode, "NONE");
    }

    public String getMouseAction(int button) {
        return mouseBindings.getOrDefault(button, "NONE");
    }

    // For manual fallback support
    public void bind(int keyCode, String actionName) {
        keyBindings.put(keyCode, actionName.toUpperCase());
    }
}
