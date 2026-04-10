package com.github.mem64bits.simple.metronome.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*Groups up and sorts multiple soundPacks easily, using
* hashmaps as a registry. Allows easily switching between multiple
* named sounds */

public class SoundRegistry{
    final private Map<String, MetronomeSound> sounds = new HashMap<>();

    public void registerSound(String name, MetronomeSound sound){
        sounds.put(name, sound); // adds MetronomeSound and gives it name, to retrieve it by
    }

    public MetronomeSound getSound(String key){
        return sounds.getOrDefault(key.toLowerCase(), sounds.values().iterator().next());
    }

    public Set<String> getAvailiableNames(){
        return sounds.keySet(); // Gets every sound in the registry
    }

    public void disposeAll(){
        for(MetronomeSound sound : sounds.values()){
            sound.dispose();
        }
    }
}
