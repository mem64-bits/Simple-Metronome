package com.github.mem64bits.simple.metronome.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

public record SoundPackConfig(
    String id,
    String high_beat_path,
    String low_beat_path,

    Optional<Integer> base_bpm // Optional for fields that might be missing
) {
    // Jackson uses this to map JSON keys to Record components
    @JsonCreator
    public SoundPackConfig(
        @JsonProperty("id") String id,
        @JsonProperty("high_beat_path") String high_beat_path,
        @JsonProperty("low_beat_path") String low_beat_path,
        @JsonProperty("base_bpm") Optional<Integer> base_bpm) {
        this.id = id;
        this.high_beat_path = high_beat_path;
        this.low_beat_path = low_beat_path;
        this.base_bpm = base_bpm != null ? base_bpm : Optional.empty(); // Ensure it's not null if JSON omits it
    }
}
