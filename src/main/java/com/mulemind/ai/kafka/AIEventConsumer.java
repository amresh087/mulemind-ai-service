package com.mulemind.ai.kafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.mulemind.ai.dto.ProjectScanResultEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AIEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(AIEventConsumer.class);
    private final AIKafkaProducer aiKafkaProducer;
   // private final JobServiceClient jobServiceClient;

    @Value("${app.kafka.topic.mulemind-scan-event}")
    private String topic;


    @KafkaListener(topics = "${app.kafka.topic.mulemind-scan-event}", groupId = "${spring.kafka.consumer.group-id}")
    public void onProjectUploaded(ProjectScanResultEvent scanEvent) {
        handleEvent(scanEvent, topic);
    }

    private void handleEvent(ProjectScanResultEvent scanEvent, String topic) {
        if (scanEvent == null) {
            log.warn("Received null Kafka event from topic {}", topic);
            return;
        }

      
    }
}
