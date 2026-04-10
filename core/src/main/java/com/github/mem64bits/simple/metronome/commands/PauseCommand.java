package com.github.mem64bits.simple.metronome.commands;

import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;
import com.github.mem64bits.simple.metronome.states.MetronomeMode;


public class PauseCommand implements Command{
    @Override
    public void execute(MetronomeEngine engine){
        if(engine.getCurrentMode() != MetronomeMode.PAUSED){
            engine.pause();
        }
        else{
            engine.resume();
        }
    }
}
