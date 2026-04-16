package com.github.mem64bits.simple.metronome.input;

import com.badlogic.gdx.InputAdapter;
import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;

public class InputHandler extends InputAdapter {
    private final InputBinder binder;
    private final MetronomeEngine engine;

    public InputHandler(InputBinder binder, MetronomeEngine engine) {
        this.binder = binder;
        this.engine = engine;
    }

    @Override
    public boolean keyDown(int keycode) {
        String action = binder.getAction(keycode);
        return trySendInput(action);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // 'button' is Input.Buttons.LEFT, RIGHT, etc.
        String action = binder.getMouseAction(button);
        return trySendInput(action);
    }

    private boolean trySendInput(String action) {
        if (!action.equals("NONE")) {
            engine.sendInput(action);
            return true;
        }
        return false;
    }
}
