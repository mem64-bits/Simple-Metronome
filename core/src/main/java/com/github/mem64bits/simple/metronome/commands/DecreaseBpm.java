package com.github.mem64bits.simple.metronome.commands;

import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;
import com.github.mem64bits.simple.metronome.internals.MetronomeSettings;

public class DecreaseBpm implements Command{
    float amount;
    float lastBpm;

    public DecreaseBpm(float decrease){
        this.amount = decrease;

    }

    @Override
    public void execute(MetronomeEngine engine){
        this.lastBpm = engine.getSettings().bpm();
        float newBpm = lastBpm - amount;
        engine.setSettings(new MetronomeSettings(newBpm, engine.getSettings().signature(), engine.getSettings().subdivision()));
    }

    @Override
    public void undo(MetronomeEngine engine){
        engine.setSettings(new MetronomeSettings(lastBpm, engine.getSettings().signature(), engine.getSettings().subdivision()));
    }
}
