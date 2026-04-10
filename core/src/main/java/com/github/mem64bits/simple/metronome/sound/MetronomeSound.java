package com.github.mem64bits.simple.metronome.sound;
import com.badlogic.gdx.audio.Sound; // LibGDX wrapper on OpenAL used to play sound clips

/* Class to group together accent and lowBeat of a Metronome, into one soundPack*/
public class MetronomeSound{
    private String name;
    private Sound highBeat; // Accent Beat
    private Sound lowBeat; // Metronome sound for the rest of the measure

    public MetronomeSound(String name, Sound high, Sound low){
        this.name = name;
        this.highBeat = high;
        this.lowBeat = low;
    }

    // Clears up low-level OpenAL API memory
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

    public void playLowBeat(){
        lowBeat.play();
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
