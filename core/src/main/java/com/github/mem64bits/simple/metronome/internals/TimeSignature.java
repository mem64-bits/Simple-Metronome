package com.github.mem64bits.simple.metronome.internals;


// Simple Record to hold time signatures in a C++-like struct style
public record TimeSignature(
    int numerator,
    int denominator
    ){
    public TimeSignature(){
        // 4 / 4 is a default time standard for music
        this(4, 4);
    }

    public double getBeatMultiplier(){
        return denominator * 4.0;
    }
}
