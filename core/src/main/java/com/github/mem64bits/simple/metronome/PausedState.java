package com.github.mem64bits.simple.metronome;

public class PausedState implements MetronomeState{
    @Override
    public void onEnter(MetronomeEngine engine){
        engine.setActive(false);
    }

    @Override
    public void onExit(MetronomeEngine engine){
        engine.setActive(true);
    }


    @Override
    public MetronomeMode getType(){
        return MetronomeMode.PAUSED;
    }
}

