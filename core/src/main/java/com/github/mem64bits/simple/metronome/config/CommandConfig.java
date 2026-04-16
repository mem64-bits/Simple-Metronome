package com.github.mem64bits.simple.metronome.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mem64bits.simple.metronome.commands.*;

import java.util.Optional; // For optional fields

public record CommandConfig(
    String id,
    String type,

    Optional<Integer> amount, // Add all possible optional parameters here
    Optional<Boolean> next
) {
    @JsonCreator
    public CommandConfig(
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,

        @JsonProperty("amount") Optional<Integer> amount,
        @JsonProperty("next") Optional<Boolean> next) {
        this.id = id;
        this.type = type;
        this.amount = amount != null ? amount : Optional.empty();
        this.next = next != null ? next : Optional.empty();
    }

    public Command toCommand() {
        return switch (type.toUpperCase()) { // Ensure type is always uppercase for consistency
            case "TOGGLE_PAUSE_COMMAND" -> new PauseCommand();
            case "QUIT_APP_COMMAND" -> new QuitApp();

            case "BPM_UP_COMMAND" -> new IncreaseBpm(amount().orElseThrow(
                () -> new IllegalArgumentException("Amount Required for BpmIncrease Command")
            ));

            case "BPM_DOWN_COMMAND" -> new DecreaseBpm(amount().orElseThrow(
                () -> new IllegalArgumentException("Amount Required for BpmDecrease Command")
            ));

            default -> throw new IllegalArgumentException("Unknown command type: " + type + " for ID: " + id);
        };
    }
}
