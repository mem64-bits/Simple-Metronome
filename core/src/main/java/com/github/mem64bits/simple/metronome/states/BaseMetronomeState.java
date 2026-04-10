package com.github.mem64bits.simple.metronome.states;

import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;
import com.github.mem64bits.simple.metronome.internals.MetronomeSettings;
import com.github.mem64bits.simple.metronome.listeners.TickEvent;


public class BaseMetronomeState implements MetronomeState{
    @Override
    public void update(double deltaTime, MetronomeEngine engine) {
        if (!engine.isActive()) return;

        engine.deltaAccumulate(deltaTime);

        final MetronomeSettings settings = engine.getSettings();
        final double interval = settings.getIntervalMillis();
        if (interval <= 0) return;

        double localAcc = engine.getBeatAccumulator();
        localAcc += (deltaTime * 1000.0);

        while (localAcc >= interval) {
            long current = engine.getCurrentTick() + 1;
            engine.setCurrentTick(current);

            final int num = settings.signature().numerator();
            final int sub = settings.subdivision();
            final int totalTicksInMeasure = num * sub;
            final int tickMeasure = (int) (current % totalTicksInMeasure);

            engine.notifyListeners(new TickEvent(
                tickMeasure,
                current,
                settings.bpm(),
                tickMeasure == 0,
                tickMeasure % sub != 0
            ));

            engine.setLastTick(current);
            localAcc -= interval;
        }
        engine.setBeatAccumulator(localAcc);
    }}
