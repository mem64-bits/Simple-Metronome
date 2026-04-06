package com.github.mem64bits.simple.metronome;

import java.util.ArrayList;
import java.util.List;

public class MetronomeEngine extends Timer {
    private MetronomeSettings settings;
    private final StateManager stateManager = new StateManager();
    private final List<MetronomeListener> listeners = new ArrayList<>();

    private long lastTick = -1;
    private long currentTick = 0;

    public MetronomeEngine(MetronomeSettings settings) {

        this.settings = settings;
        this.stateManager.setMode(MetronomeMode.PAUSED, this);
    }

    /**
     * Advances metronome ticks on time accumulation; notifies listeners of accents and subdivisions
     */
    @Override
    public void update(double deltaTime) {
        stateManager.update(deltaTime, this);
    }

    @Override
    public void start() {
        this.lastTick = -1;
        stateManager.setMode(MetronomeMode.RUNNING, this);
    }

    @Override
    public void stop() {
        stateManager.setMode(MetronomeMode.PAUSED, this);
    }

    @Override
    public void reset() {
        super.reset();
        this.lastTick = -1;
        this.currentTick = 0;
        stateManager.setMode(MetronomeMode.PAUSED, this);
    }

    public long getCurrentTick() { return currentTick; }
    public long getLastTick() { return lastTick; }

    public void setLastTick(long lastTick){ this.lastTick = lastTick; }
    public void setCurrentTick(long currentTick){ this.currentTick = currentTick; }

    public MetronomeSettings getSettings(){ return this.settings; }
    public void setSettings(MetronomeSettings settings){
        this.settings = settings;
    }

    public void addListener(MetronomeListener listener) {
        listeners.add(listener);
    }
    public void removeListener(MetronomeListener listener) {
        listeners.remove(listener);
    }

    protected void notifyListeners(TickEvent event) {
        for (MetronomeListener listener : listeners) {
            listener.onMetronomeTick(event);
        }
    }

}
