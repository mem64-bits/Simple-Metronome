package com.github.mem64bits.simple.metronome.states;

import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;

import java.util.EnumMap;
import java.util.Map;

public class StateManager {
    private MetronomeState currentState;

    // Fast Special Hashmap, mapping enum type to a real Class object, only loaded once in memory
    private final Map<MetronomeMode, MetronomeState> statePool = new EnumMap<>(MetronomeMode.class);

    public StateManager() {
        statePool.put(MetronomeMode.RUNNING, new RunningState());
        statePool.put(MetronomeMode.PAUSED, new PausedState());
            }

    /**
     * The UI/Engine calls this with an Enum (e.g. setMode(MetronomeMode.ALERT))
     */
    public void setMode(MetronomeMode mode, MetronomeEngine engine) {
        MetronomeState next = statePool.get(mode);

        // Safety: Don't restart the same state or transition to a missing state
        if (next == null || next == currentState) return;

        swapState(next, engine);
    }

    private void swapState(MetronomeState newState, MetronomeEngine engine) {
        if (currentState != null) {
            currentState.onExit(engine);
        }

        this.currentState = newState;

        if (currentState != null) {
            currentState.onEnter(engine);
        }
    }

    public void update(double dt, MetronomeEngine engine) {
        // Standard Null Check for systems safety
        if (currentState != null) {
            currentState.update(dt, engine);
        }
    }

    public MetronomeState getCurrentState() {
        return currentState;
    }

    // Returns the Enum representation (Great for UI Labels or Network packets)
    public MetronomeMode getCurrentMode() {
        return (currentState != null) ? currentState.getType() : null;
    }
}
