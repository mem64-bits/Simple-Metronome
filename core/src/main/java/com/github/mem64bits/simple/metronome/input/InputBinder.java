package com.github.mem64bits.simple.metronome.input;

import java.util.HashMap;
import java.util.Map;

/*Class for assigning "actions" to keybinds to be accessed by name later*/
public class InputBinder {
    private final Map<Integer, String> keyBindings = new HashMap<>();

    public void bind(int keyCode, String actionName) {
        keyBindings.put(keyCode, actionName.toUpperCase());
    }

    public String getAction(int keyCode) {
        return keyBindings.getOrDefault(keyCode, "NONE");
    }
}
