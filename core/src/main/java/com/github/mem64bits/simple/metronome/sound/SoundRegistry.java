package com.github.mem64bits.simple.metronome.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper; // Jackson import
import com.fasterxml.jackson.databind.type.CollectionType; // For parsing arrays
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.mem64bits.simple.metronome.config.SoundPackConfig; // DTO import

import java.io.IOException;
import java.util.*;
import java.nio.file.Path;

public class SoundRegistry {
    private final Map<String, MetronomeSound> sounds = new HashMap<>();
    private MetronomeSound defaultFallbackSound = null;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson instance

    public SoundRegistry(){
        objectMapper.registerModule(new Jdk8Module());
    }

    /**
     * Loads sound packs from a JSON configuration file.
     * This method acts as a Factory for MetronomeSound objects.
     */
    public void loadFromJson(Path configPath) throws IOException {
        FileHandle configFileHandle = Gdx.files.internal(configPath.toString());
        if (!configFileHandle.exists()) {
            throw new IOException("Sound config file not found: " + configPath);
        }
        String jsonContent = configFileHandle.readString();

        // Jackson: Parse JSON Array into List of DTOs
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, SoundPackConfig.class);
        List<SoundPackConfig> configs = objectMapper.readValue(jsonContent, listType); // FIX: Use objectMapper.readValue

        // Use DTOs to create and register MetronomeSound objects
        for (SoundPackConfig config : configs) {
            try {
                int baseBpm = config.base_bpm().orElse(0);
                MetronomeSound pack = MetronomeSound.load(
                    config.id(),
                    config.high_beat_path(),
                    config.low_beat_path(),
                    Optional.of(baseBpm)
                );
                // Register using the ID, which is automatically lowercased
                registerSound(pack.getSoundID(), pack);
                System.out.println("[SoundRegistry] Loaded: " + pack.getSoundID());
            } catch (IllegalArgumentException e) {
                System.err.println("[SoundRegistry] Error loading pack " + config.id() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Registers a MetronomeSound, normalizing its ID to lowercase.
     */
    public void registerSound(String name, MetronomeSound sound) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Sound ID cannot be null or empty.");
        }
        String lowerCaseName = name.toLowerCase();
        sounds.put(lowerCaseName, sound);
        // If this is the first sound registered, make it the default fallback
        if (defaultFallbackSound == null) {
            defaultFallbackSound = sound;
        }
    }

    /**
     * Retrieves a MetronomeSound by its ID, normalizing the lookup key to lowercase.
     * Provides a safe fallback if the sound is not found.
     */
    public MetronomeSound getSound(String key) {
        if (key == null || key.isBlank()) {
            System.err.println("[SoundRegistry] Warning: Attempted to get sound with null/empty key. Returning default.");
            return defaultFallbackSound;
        }
        return sounds.getOrDefault(key.toLowerCase(), defaultFallbackSound);
    }

    /**
     * Gets a Set of all available sound IDs in lowercase.
     */
    public Set<String> getAvailableNames() {
        return sounds.keySet();
    }

    /**
     * Disposes all registered MetronomeSound resources.
     */
    public void disposeAll() {
        for (MetronomeSound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
        defaultFallbackSound = null;
    }
}
