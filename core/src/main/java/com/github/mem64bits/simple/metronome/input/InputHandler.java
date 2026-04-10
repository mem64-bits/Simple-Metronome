package com.github.mem64bits.simple.metronome.input;

import com.badlogic.gdx.InputAdapter;
import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;

/*Class that executes actions for keys if found in keybind hashmap,
* hooking to libgdx's in built keyDown function*/

public class InputHandler extends InputAdapter {
    private final InputBinder binder;
    private final MetronomeEngine engine;

    public InputHandler(InputBinder binder, MetronomeEngine engine) {
        this.binder = binder;
        this.engine = engine;
    }

    /*Overrides libgdx input press function to execute
     a key if found in the bound keys hashmap*/
    @Override
    public boolean keyDown(int keycode) {
         // Map physical key to string "action"
        String action = binder.getAction(keycode);

        if (!action.equals("NONE")) {
            engine.sendInput(action); // Calls commandManager.invoke(action)
            return true; // returns true if valid key command is found and executed
        }
        return false;
    }
}
