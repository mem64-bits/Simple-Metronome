package com.github.mem64bits.simple.metronome.listeners;

import com.github.mem64bits.simple.metronome.internals.SystemEvent;


public class UIUpdater implements MetronomeListener{
    @Override
    public void onMetronomeTick(TickEvent tick){
        MetronomeListener.super.onMetronomeTick(tick);
    }

    @Override
    public void onSystemChange(SystemEvent event){
        MetronomeListener.super.onSystemChange(event);
    }

    @Override
    public void onMeasureEnd(){
        MetronomeListener.super.onMeasureEnd();
    }

    @Override
    public void onBpmChanged(){
        MetronomeListener.super.onBpmChanged();
    }

    @Override
    public void onStart(){
        MetronomeListener.super.onStart();
    }

    @Override
    public void onStop(){
        MetronomeListener.super.onStop();
    }

    @Override
    public void onPause(){
        System.out.println("Paused Metronome");
    }
}
