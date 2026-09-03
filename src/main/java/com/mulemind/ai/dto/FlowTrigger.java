package com.mulemind.ai.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowTrigger {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public FlowTrigger(String value) {
        int separator = value.indexOf(' ');
        if (separator < 0) {
            this.type = value;
            return;
        }

        this.type = value.substring(0, separator);
        this.path = value.substring(separator + 1);
    }

    private String type;
    private String path;
}