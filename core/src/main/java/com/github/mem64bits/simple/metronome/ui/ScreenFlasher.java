package com.github.mem64bits.simple.metronome.ui;

import com.badlogic.gdx.graphics.Color;
import com.github.mem64bits.simple.metronome.listeners.TickEvent;
import com.github.mem64bits.simple.metronome.listeners.MetronomeListener;


public class ScreenFlasher implements MetronomeListener{

    private final Color currentColour = new Color(0, 0, 0, 1);

    @Override
    public void onMetronomeTick(TickEvent tick){
        if(tick.isAccent()){
            currentColour.set(0.8f,0.0f,0.0f,1.0f);
        }

        else if(!tick.isSub()){
            currentColour.set(0.1f,0.5f,0.1f,1.0f);
        }

        else{
            currentColour.set(0.05f, 0.2f, 0.05f, 1.0f);
        }

        System.out.print("\r"+"Beat: "+(tick.currentTick()+1));
    }

    public void update(float delta){
        currentColour.lerp(Color.BLACK, delta * 5.0f);
    }

    public Color getColour(){
        return currentColour;
    }

    public void setColour(Color colour){
        this.currentColour.set(colour);
    }
}
