package com.mulemind.ai.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mulemind.ai.dto.EmbeddingRequest;
import com.mulemind.ai.dto.EmbeddingResponse;
import com.mulemind.ai.dto.ProjectScanResultEvent;
import com.mulemind.ai.utilty.DocumentationType;
import com.mulemind.ai.utilty.PromptHelper;

import io.qdrant.client.QdrantClient;
import tools.jackson.databind.ObjectMapper;

@Service
public class OllamaService {

    private final RestTemplate restTemplate;

    private final QdrantClient qdrantClient;

    @Value("${ollama.url}")
    private String ollamaUrl;

    @Value("${ollama.model}")
    private String model;

    @Value("${ollama.embeddings-url}")
    private String embeddingsUrl;

    @Value("${ollama.emd-model}")
    private String emdModel;

    public OllamaService(RestTemplate restTemplate, QdrantClient qdrantClient) {
        this.restTemplate = restTemplate;
        this.qdrantClient = qdrantClient;
    }

   

    public String generateApplicationDocumentation(ProjectScanResultEvent scanEvent,DocumentationType docType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String metadataJson = mapper.writeValueAsString(scanEvent);
            String prompt = null;

            if (docType == DocumentationType.FUNCTIONAL_DOC) {
                prompt = PromptHelper.getFunctionalDocPrompt(metadataJson);
            } 
            /* 
            else if (docType == DocumentationType.FLOW_DOC) {
                prompt = PromptHelper.getFlowDocPrompt(metadataJson);
            } else if (docType == DocumentationType.SEQUENCE_DOC) {
                prompt = PromptHelper.getSequenceDocPrompt(metadataJson);
            }else if(docType == DocumentationType.TECHNICAL_DOC){
                 prompt = PromptHelper.getTechnicalDocPrompt(metadataJson);
            }
             */
            System.out.println("=============== Generated Prompt for Ollama: " + prompt);
            
            if (prompt == null) {
                throw new IllegalArgumentException("Unsupported documentation type: " + docType);
            }
            
            String result= executeOllamaCall(prompt);
            System.out.println("=============== Generated Documentation from Ollama: " + result);
            return result;
        } catch (Exception exception) {
            throw new RuntimeException("Failed to generate application documentation", exception);
        }
    }




/**
 * Creates an embedding for the given prompt using the Ollama API.  
 * @param request
 * @return
 */

    public EmbeddingResponse createEmbedding(EmbeddingRequest request) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", emdModel);
        payload.put("prompt", request.getPrompt());

        String endpoint = embeddingsUrl;
        Map<?, ?> response = restTemplate.postForObject(endpoint, payload, Map.class);
        if (response != null && response.get("embedding") instanceof List) {
            @SuppressWarnings("unchecked")
            List<Double> embedding = (List<Double>) response.get("embedding");
            return EmbeddingResponse.builder().embedding(embedding).build();
        }
        return EmbeddingResponse.builder().embedding(List.of()).build();
    }

    /**
     * Helper method to execute the Ollama API call with the given prompt and return
     * the response as a string.
     * 
     * @param prompt The prompt to send to the Ollama API.
     * @return The response from the Ollama API as a string.
     */
    private String executeOllamaCall(String prompt) {
        Map<String, Object> request = new HashMap<>();
        request.put("model", model);
        request.put("prompt", prompt);
        request.put("stream", false);

        Map<String, Object> options = new HashMap<>();
        options.put("temperature", 0);
        request.put("options", options);

        Map<?, ?> response = restTemplate.postForObject(ollamaUrl, request, Map.class);
        if (response != null && response.get("response") != null) {
            return response.get("response").toString();
        }
        return "{}";
    }

 
}