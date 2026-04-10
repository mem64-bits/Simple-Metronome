package com.github.mem64bits.simple.metronome.internals;

import com.github.mem64bits.simple.metronome.commands.CommandManager;
import com.github.mem64bits.simple.metronome.listeners.MetronomeListener;
import com.github.mem64bits.simple.metronome.states.MetronomeMode;
import com.github.mem64bits.simple.metronome.states.StateManager;
import com.github.mem64bits.simple.metronome.listeners.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class MetronomeEngine extends Timer{
    // Record storing parameters for the way the metronome runs
    private volatile MetronomeSettings settings;

    /* Design Pattern that dynamically defines how the engine should be running,
     entering & leaving a new State*/
    private final StateManager stateManager = new StateManager();

    /*Command Pattern used to handle Input "Commands" and actions requested by UI
    * in a centralised queue*/
    private final CommandManager cmdMgr = new CommandManager();

    /*Observer pattern used to update any listeners in List on onMetronomeTick */
    private final List<MetronomeListener> listeners = new ArrayList<>();

    /*Tracks metronome Beat Position at a given time*/
    private long lastTick = -1;
    private long currentTick = 0;
    private double beatAccumulator = 0; // Tracks progress toward the next beat

    public MetronomeEngine(MetronomeSettings settings) {
        this.settings = settings;
        // Starts metronome in paused state without updating
        this.stateManager.setMode(MetronomeMode.PAUSED, this);
    }

    /**
     * Advances metronome ticks on time accumulation; notifies listeners of accents and subdivisions
     */
    @Override
    public void update(double deltaTime) {
        // Handles UI actions and requests
        cmdMgr.processInbox(this);
        //Handles internal logic based on the current state
        stateManager.update(deltaTime, this);
    }

    @Override
    public void start() {
        this.lastTick = -1;
        this.currentTick = -1;
        this.setBeatAccumulator(settings.getIntervalMillis());
        stateManager.setMode(MetronomeMode.RUNNING, this);
    }

    @Override
    public void stop() {
        // Changes update() method to "paused" state
        reset();
        stateManager.setMode(MetronomeMode.PAUSED, this);
    }

    @Override
    public void pause(){
        stateManager.setMode(MetronomeMode.PAUSED, this);
    }

    @Override
    public void resume(){
        stateManager.setMode(MetronomeMode.RUNNING, this);
    }

    @Override
    public void reset() {
        // Resets internal Timer class "clock" and goes to paused state
        super.reset();
        this.lastTick = -1;
        this.currentTick = 0;
        this.beatAccumulator = 0;
        stateManager.setMode(MetronomeMode.PAUSED, this);
    }

    public long getCurrentTick() { return currentTick; }
    public long getLastTick() { return lastTick; }

    public void setLastTick(long lastTick){ this.lastTick = lastTick; }
    public void setCurrentTick(long currentTick){ this.currentTick = currentTick; }

    public double getBeatAccumulator(){ return beatAccumulator; }
    public void setBeatAccumulator(double beatAccumulator){ this.beatAccumulator = beatAccumulator; }

    public MetronomeSettings getSettings(){ return this.settings; }
    public void setSettings(MetronomeSettings settings){
        this.settings = settings;
    }

    public MetronomeMode getCurrentMode(){
        return stateManager.getCurrentMode();
    }

    // Observer Pattern methods to handle listeners
    public void addListener(MetronomeListener listener) {
        listeners.add(listener);
    }
    public void removeListener(MetronomeListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners(TickEvent event) {
        for (MetronomeListener listener : listeners) {
            listener.onMetronomeTick(event);
        }
    }

    public CommandManager getCmdMgr(){
        return this.cmdMgr;
    }
    // Method to allow Engine to Execute commands from input
    public void sendInput(String action) {
        cmdMgr.invoke(action);
    }

}
