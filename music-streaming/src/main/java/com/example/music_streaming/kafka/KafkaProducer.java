package com.example.music_streaming.kafka;


import com.example.music_streaming.models.MusicSearchEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String topic,Object searchEvent){
        try{
            String json = objectMapper.writeValueAsString(searchEvent);

            kafkaTemplate.send(topic, json);
        } catch (Exception e) {
            log.error("Error sending to search :{}",searchEvent);
            throw new RuntimeException(e);
        }
    }
}

