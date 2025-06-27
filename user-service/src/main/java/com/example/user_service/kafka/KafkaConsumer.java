package com.example.user_service.kafka;
import com.example.user_service.models.MusicSearchEvent;
import com.example.user_service.models.MusicSearchEventDto;
import com.example.user_service.models.MusicStreamEvent;
import com.example.user_service.services.KafkaEventWrite;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaEventWrite kafkaEventWrite;
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    ModelMapper modelMapper=new ModelMapper();


    @KafkaListener(topics = "search",groupId = "music-search-consumer-group")
    public void listenToSearchEvent(String json) {
        try {
            MusicSearchEventDto event = objectMapper.readValue(json, MusicSearchEventDto.class);
            MusicSearchEvent searchEvent = modelMapper.map(event, MusicSearchEvent.class);
            System.out.println("KAFKA CONSUMED : "+searchEvent.getSearchQuery());
            kafkaEventWrite.writeSearchEvent(searchEvent);
        } catch (Exception e) {
            log.error("Failed :{}",json);
        }
    }

    @KafkaListener(topics = "stream",groupId = "music-stream-consumer-group")
    public void listenToStreamEvent(String json) {
        try {
            MusicStreamEvent streamEvent = objectMapper.readValue(json, MusicStreamEvent.class);
            System.out.println("KAFKA CONSUMED : "+streamEvent.getMusicId());
            kafkaEventWrite.writeStreamEvent(streamEvent);
        } catch (Exception e) {
            log.error("Failed :{}",json);
        }
    }


}

