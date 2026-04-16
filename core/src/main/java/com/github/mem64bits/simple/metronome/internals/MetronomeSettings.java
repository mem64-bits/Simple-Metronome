package com.github.mem64bits.simple.metronome.internals;


// Holds important parameters about how and when the metronome should be played
public record MetronomeSettings(
    float bpm,
    TimeSignature signature,
    int subdivision
){
    /*An interval in music is the space between two notes, in this case the time between two beats*/
    public double getIntervalMillis(){
        return (60000.0 / bpm) * (4.0 / signature.denominator()) / subdivision;
    }
}
