package com.mulemind.ai.kafka;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.mulemind.ai.client.JobServiceClient;
import com.mulemind.ai.dto.MetadataGeneratedEvent;
import com.mulemind.ai.dto.ProjectScanResultEvent;
import com.mulemind.ai.service.OllamaService;
import com.mulemind.ai.utilty.DocumentationType;
import com.mulemind.ai.utilty.TransformationStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AIEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(AIEventConsumer.class);
    private final AIKafkaProducer aiKafkaProducer;
    private final JobServiceClient jobServiceClient;
    private final OllamaService ollamaService;

    @Value("${app.kafka.topic.mulemind-scan-event}")
    private String topic;


    @KafkaListener(topics = "${app.kafka.topic.mulemind-scan-event}", groupId = "${spring.kafka.consumer.group-id}")
    public void onProjectUploaded(ProjectScanResultEvent scanEvent) {
        log.info("Received project scan event for document {} from topic {}", scanEvent != null ? scanEvent.getDocumentId() : null, topic);
        handleEvent(scanEvent, topic);
    }

    private void handleEvent(ProjectScanResultEvent scanEvent, String topic) {
        if (scanEvent == null) {
            log.warn("Received null Kafka event from topic {}", topic);
            return;
        }

   
         updateJobStatus(scanEvent, TransformationStatus.AI_ANALYZING);    
        try {
            for (DocumentationType documentationType : DocumentationType.values()) 
                {
                       
                    System.out.println("******************************* Processing documentation type: " + documentationType.name());   
                    
                    String documentation = ollamaService.generateApplicationDocumentation(scanEvent, documentationType);
                        MetadataGeneratedEvent generatedEvent = MetadataGeneratedEvent.builder()
                                .eventVersion(scanEvent.getEventVersion())
                                .documentId(scanEvent.getDocumentId())
                                .documentName(scanEvent.getDocumentName())
                                .documentationType(documentationType.name())
                                .tenant(scanEvent.getTenant())
                                .documentation(documentation)
                                .build();
                    // updateJobStatus(scanEvent, TransformationStatus.METADATA_PROCESSING);
                        aiKafkaProducer.send(generatedEvent, scanEvent.getDocumentId().toString());
                    // updateJobStatus(scanEvent, TransformationStatus.DOCUMENT_GENERATING);
                        Thread.sleep(3000); // Sleep for 3 seconds between sending events
            }
            updateJobStatus(scanEvent, TransformationStatus.METADATA_PROCESSING);
            Thread.sleep(1000); // Sleep for 1 seconds between sending events
            updateJobStatus(scanEvent, TransformationStatus.DOCUMENT_GENERATING);
            Thread.sleep(1000); // Sleep for 1 seconds between sending events
            updateJobStatus(scanEvent, TransformationStatus.COMPLETED);
            Thread.sleep(1000); // Sleep for 1 seconds between sending events
             updateJobStatus(scanEvent, TransformationStatus.DONE);
           
        } catch (RuntimeException exception) {
            log.error("Failed to process scan event for document {}", scanEvent.getDocumentId(), exception);
            updateJobStatus(scanEvent, TransformationStatus.FAILED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Project scan event received: documentId={}, status={}, eventType={}",
                scanEvent.getDocumentId(), scanEvent.getStatus(), scanEvent.getEventType());
    }

    private void updateJobStatus(ProjectScanResultEvent scanEvent, TransformationStatus status) {
        Map<String, String> payload = new HashMap<>();
        payload.put("status", status.name());
        payload.put("description", status.getDescription());
        jobServiceClient.updateJobStatus(scanEvent.getDocumentId(), payload);
    }
}
