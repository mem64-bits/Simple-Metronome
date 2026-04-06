package com.github.mem64bits.simple.metronome;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SoundRegistry{
    final private Map<String, MetronomeSound> sounds = new HashMap<>();

    public void registerSound(String name, MetronomeSound sound){
        sounds.put(name, sound);
    }

    public MetronomeSound getSound(String key){
        return sounds.getOrDefault(key.toLowerCase(), sounds.values().iterator().next());
    }

    public Set<String> getAvailiableNames(){
        return sounds.keySet();
    }

    public void disposeAll(){
        for(MetronomeSound sound : sounds.values()){
            sound.dispose();
        }
    }
}
