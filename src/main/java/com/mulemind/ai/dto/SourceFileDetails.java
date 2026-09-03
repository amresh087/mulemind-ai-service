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
public class SourceFileDetails {
    private String name;
    private boolean parsed;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public SourceFileDetails(String name) {
        this.name = name;
    }
}