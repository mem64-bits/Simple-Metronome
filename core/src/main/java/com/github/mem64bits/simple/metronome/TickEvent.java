package com.github.mem64bits.simple.metronome;


// Holds information that MetronomeListener will use to update app elements with
public record TickEvent(
    long tickMeasure,
    long currentTick,
    int bpm,
    boolean isAccent, // The isAccent boolean tells the listener if beat updated is the first in measure
    boolean isSub
){
}
