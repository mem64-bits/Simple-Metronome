package com.github.mem64bits.simple.metronome.internals;

import com.github.mem64bits.simple.metronome.states.MetronomeState;


public record SystemEvent(MetronomeState state, MetronomeSettings settings ){
}
