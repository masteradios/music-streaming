package com.example.user_service.services;

import com.example.user_service.models.MusicSearchEvent;
import com.example.user_service.models.MusicStreamEvent;
import com.example.user_service.repositories.MusicSearchEventRepository;
import com.example.user_service.repositories.MusicStreamEventRepository;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventWrite {

    private final MusicSearchEventRepository musicSearchEventRepository;
    private final MusicStreamEventRepository musicStreamEventRepository;


    public KafkaEventWrite(MusicSearchEventRepository musicSearchEventRepository, MusicStreamEventRepository musicStreamEventRepository) {
        this.musicSearchEventRepository = musicSearchEventRepository;
        this.musicStreamEventRepository = musicStreamEventRepository;
    }

    public void writeSearchEvent(MusicSearchEvent event){
        musicSearchEventRepository.save(event);
    }

    public void writeStreamEvent(MusicStreamEvent streamEvent){
        musicStreamEventRepository.save(streamEvent);
    }
}
