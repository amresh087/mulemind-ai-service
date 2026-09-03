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
public class ApiEndpoint {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public ApiEndpoint(String path) {
        this.path = path;
    }

    private String type;
    private String method;
    private boolean methodRestricted;
    private String path;
    private String listenerConfig;
    private String host;
    private Integer port;
    private String flow;
}