package com.github.mem64bits.simple.metronome;

public interface MetronomeState{
    public void onEnter(MetronomeEngine engine);
    public void onExit(MetronomeEngine engine);

    public default void update(double deltaTime, MetronomeEngine engine){}
    public MetronomeMode getType();
}
