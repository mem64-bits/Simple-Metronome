package com.github.mem64bits.simple.metronome.commands;

import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;


/*Abstract Template for Command Pattern to be implemented*/
public interface Command{
    default void execute(MetronomeEngine engine){ execute(); }
    default void execute(){};
    default void undo(MetronomeEngine engine){}
}
