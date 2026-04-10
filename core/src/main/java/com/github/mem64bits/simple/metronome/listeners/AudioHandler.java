package com.github.mem64bits.simple.metronome.listeners;

/*The AudioHandler is a concrete observer building upon
the abstract MetronomeListener interface; in this case
this listener is specifically focused on audio-related events*/

import com.github.mem64bits.simple.metronome.sound.MetronomeSound;
import com.github.mem64bits.simple.metronome.sound.SoundRegistry;


public class AudioHandler implements MetronomeListener{
    /*Registry Data Structure to easily store and
    arrange multiple metronome sounds*/
    private final SoundRegistry registry;

    private String currentSound;

    public AudioHandler(SoundRegistry registry, String initialSound){
        this.registry = registry;
        this.currentSound = initialSound;
    }

    public void setSound(String name){
        this.currentSound = name;
    }

    public String getCurrentSound(){
        return this.currentSound;
    }


    // Is called everytime the beat of the metronome changes
    @Override
    public void onMetronomeTick(TickEvent event){
        MetronomeSound soundStrategy = registry.getSound(currentSound);

        if(event.isAccent()){
            soundStrategy.playHighBeat();
        } else if(event.isSub()){
            soundStrategy.playLowBeat(0.4f);
        }
        else{
            soundStrategy.playLowBeat();
        }
    }
}
