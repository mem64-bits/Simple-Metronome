package com.github.mem64bits.simple.metronome.states;

/*Base implementation of State Pattern that is
* more powerful than enums + switch statements */

import com.github.mem64bits.simple.metronome.internals.MetronomeEngine;


public interface MetronomeState{
    // What will happen when code enters this state?
    public default void onEnter(MetronomeEngine engine){}

    // What will happen when code leaves this state?
    public default void onExit(MetronomeEngine engine){}

    // allows Engine to dynamically update in different ways depending on state
    public void update(double deltaTime, MetronomeEngine engine);

    // Each implemented class should return a specific Enum Type
    public default MetronomeMode getType(){return MetronomeMode.NULL; }
}
