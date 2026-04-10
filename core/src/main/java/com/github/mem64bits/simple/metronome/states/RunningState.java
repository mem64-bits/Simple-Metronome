package com.github.mem64bits.simple.metronome.states;

import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;


public class RunningState extends BaseMetronomeState{
    @Override
    public void onEnter(MetronomeEngine engine){
        if(!engine.isActive())
            engine.setActive(true);
    }

    @Override
    public void update(double deltaTime, MetronomeEngine engine){
     super.update(deltaTime, engine);
    }

    @Override
    public MetronomeMode getType(){
        return MetronomeMode.RUNNING;
    }
}
