package com.github.mem64bits.simple.metronome;

public interface MetronomeListener{

    // Is called whenever the tick of the metronome is updated
    default void onMetronomeTick(TickEvent tick){}

    // Is Called less Often for changes that don't happen every frame
    default void onSystemChange(SystemEvent event){};
    default void onMeasureEnd(){}
    default void onBpmChanged(){};


    default void onStart(){}
    default void onStop(){}
    default void onPause(){}
}
