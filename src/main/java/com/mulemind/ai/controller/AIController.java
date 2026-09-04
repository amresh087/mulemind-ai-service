package com.mulemind.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mulemind.ai.dto.AIRequest;
import com.mulemind.ai.dto.EmbeddingRequest;
import com.mulemind.ai.dto.EmbeddingResponse;
import com.mulemind.ai.service.OllamaService;

@RestController
@RequestMapping("/ai")
// @CrossOrigin("*")
public class AIController {

    private final OllamaService ollamaService;

    public AIController(
            OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping("/intent")
    public String getIntent(@RequestBody AIRequest request, @RequestParam(required = false) String sessionMode) {
        // We pass the sessionMode flag from the frontend directly to the service layer
        return null;
    }

    @PostMapping("/embeddings")
    public ResponseEntity<EmbeddingResponse> createEmbedding(@RequestBody EmbeddingRequest request) {
        EmbeddingResponse response = ollamaService.createEmbedding(request);
        return ResponseEntity.ok(response);
    }


     @PostMapping("/saveProductEmbedding")
    public ResponseEntity<EmbeddingResponse> saveProductEmbedding(@RequestBody EmbeddingRequest request) {
        EmbeddingResponse response = ollamaService.createEmbedding(request);
        return ResponseEntity.ok(response);
    }




}
