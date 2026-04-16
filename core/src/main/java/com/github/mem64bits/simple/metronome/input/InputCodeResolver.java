package com.github.mem64bits.simple.metronome.input;

import com.badlogic.gdx.Input;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/*Uses Reflection to Get Enum Data at runtime for all inputs and
* add to hashmap, so that enum code Gdx.Input.Keys.Right, can be
* referenced as a String "Right" in JSON config*/

public class InputCodeResolver {
    private static final InputCodeResolver INSTANCE = new InputCodeResolver();
    private final Map<String, Integer> keyNameToCode = new HashMap<>();
    private final Map<String, Integer> buttonNameToCode = new HashMap<>();

    private InputCodeResolver() {
        // Build the keyboard key map using reflection
        for (Field field : Input.Keys.class.getFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && field.getType() == int.class) {
                try {
                    // Gets the name of an enum at runtime and saves it as a string
                    String name = field.getName().toUpperCase(Locale.ROOT);
                    int value = field.getInt(null); // Get the int value of the enum
                    keyNameToCode.put(name, value); // These are stored together as key-values
                } catch (IllegalAccessException ignored) {
                    // This should not happen for public static final fields
                }
            }
        }

        // Build the mouse button map (Input.Buttons has static fields)
        for (Field field : Input.Buttons.class.getFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && field.getType() == int.class) {
                try {
                    String name = field.getName().toUpperCase(Locale.ROOT);
                    int value = field.getInt(null);
                    buttonNameToCode.put(name, value);
                } catch (IllegalAccessException ignored) {}
            }
        }

        System.out.println("[InputResolver] Loaded " + keyNameToCode.size() + " keyboard keys.");
        System.out.println("[InputResolver] Loaded " + buttonNameToCode.size() + " mouse buttons.");
    }

    public static InputCodeResolver getInstance() {
        return INSTANCE;
    }

    /**
     * Converts a keyboard key name (e.g., "SPACE", "UP") to its LibGDX integer code.
     * Returns -1 if the name is not found.
     */
    public int getKeyCode(String keyName) {
        return keyNameToCode.getOrDefault(keyName.toUpperCase(Locale.ROOT), -1);
    }

    /**
     * Converts a mouse button name (e.g., "LEFT_BUTTON", "RIGHT_BUTTON") to its LibGDX integer code.
     * Returns -1 if the name is not found.
     */
    public int getMouseButtonCode(String buttonName) {
        return buttonNameToCode.getOrDefault(buttonName.toUpperCase(Locale.ROOT), -1);
    }
}
