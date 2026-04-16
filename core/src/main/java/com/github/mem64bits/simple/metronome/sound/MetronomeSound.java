package com.github.mem64bits.simple.metronome.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound; // LibGDX wrapper on OpenAL used to play sound clips
import com.badlogic.gdx.files.FileHandle;
import java.util.Objects;
import java.util.Optional;


/* Class to group together accent and lowBeat of a Metronome, into one soundPack*/
public class MetronomeSound{
    private final String soundID;
    private final Sound highBeat; // Accent Beat
    private final Sound lowBeat; // Metronome sound for the rest of the measure
    private final int baseBpm; // Holds bpm sound was recorded at

    // a private constructor forces a user to use a validated factory object creator
    private MetronomeSound(String id, Sound high, Sound low, int baseBpm){
        this.soundID = id;
        this.highBeat = high;
        this.lowBeat = low;
        this.baseBpm = baseBpm;
    }

    public static MetronomeSound load(String name, String highPath, String lowPath, Optional<Integer> baseBpm) {
        Objects.requireNonNull(name, "SoundPack name cannot be Null");
        FileHandle highFile = Gdx.files.internal(highPath);
        FileHandle lowFile = Gdx.files.internal(lowPath);

        if (!highFile.exists()) {
            throw new IllegalArgumentException("High beat file not found at: " + highPath);
        }
        if (!lowFile.exists()) {
            throw new IllegalArgumentException("Low beat file not found at: " + lowPath);
        }

        Sound high = Gdx.audio.newSound(highFile);
        Sound low = Gdx.audio.newSound(lowFile);
        int finalBaseBpm = baseBpm.orElse(0);

        return new MetronomeSound(name, high, low, finalBaseBpm);
    }


    // Clears up low-level OpenAL API memory
    public void dispose(){
        if(highBeat != null) highBeat.dispose();
        if(lowBeat != null) lowBeat.dispose();
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

    public String getSoundID(){
        return soundID;
    }

    public Sound getHighBeat(){
        return highBeat;
    }

    public Sound getLowBeat(){
        return lowBeat;
    }

    public int getBaseBpm(){ return baseBpm; }

    public float getPitchMultiplier(int currentBpm) {
        // If baseBpm was 0 (meaning not provided), default to 1.0f pitch
        if (baseBpm == 0) return 1.0f;
        return (float) currentBpm / (float) baseBpm;
    }
}
