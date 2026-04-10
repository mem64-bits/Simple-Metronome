package com.github.mem64bits.simple.metronome.states;

import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;


public class PausedState implements MetronomeState{
    @Override
    public void onEnter(MetronomeEngine engine){
        // pauses when entering state
        engine.setActive(false);
    }

    @Override
    public void onExit(MetronomeEngine engine){
        // Unpauses on leaving
        engine.setActive(true);
    }

    @Override
    public void update(double deltaTime, MetronomeEngine engine){}

    /*We have enums to check types as passing in Objects for state checking is
    * expensive*/

    @Override
    public MetronomeMode getType(){
        return MetronomeMode.PAUSED;
    }
}

