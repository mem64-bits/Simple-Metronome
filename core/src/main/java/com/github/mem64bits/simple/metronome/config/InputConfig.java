package com.github.mem64bits.simple.metronome.config;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Optional;



public record InputConfig(
    Map<String, String> keyboard,
    Optional<Map<String, String>> mouse
){
    @JsonCreator
    public InputConfig(
        @JsonProperty("keyboard")Map<String, String> keyboard,
        @JsonProperty("mouse")Optional<Map<String, String>> mouse
    )
    {
        this.keyboard = keyboard;
        this.mouse = mouse != null ? mouse : Optional.empty();
    }
}
