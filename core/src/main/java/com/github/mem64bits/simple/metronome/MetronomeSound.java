package com.github.mem64bits.simple.metronome;
import com.badlogic.gdx.audio.Sound; // LibGDX wrapper on OpenAL used to play sound clips


public class MetronomeSound{
    String name;
    Sound highBeat; // Accent Beat
    Sound lowBeat; // Metronome sound for the rest of the measure

    public MetronomeSound(String name, Sound high, Sound low){
        this.name = name;
        this.highBeat = high;
        this.lowBeat = low;
    }


    public void dispose(){
        highBeat.dispose();
        lowBeat.dispose();
    }


    public void playHighBeat(){
        highBeat.play();
    }

    public void playHighBeat(float vol){
        highBeat.play(vol);
    }

    public void playLowBeat(float vol){
        lowBeat.play(vol);
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Sound getHighBeat(){
        return highBeat;
    }

    public void setHighBeat(Sound highBeat){
        this.highBeat = highBeat;
    }

    public Sound getLowBeat(){
        return lowBeat;
    }

    public void setLowBeat(Sound lowBeat){
        this.lowBeat = lowBeat;
    }
}
