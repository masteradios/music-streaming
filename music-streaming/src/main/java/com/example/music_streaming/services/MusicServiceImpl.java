package com.example.music_streaming.services;

import co.elastic.clients.util.DateTime;
import com.example.music_streaming.exceptions.MusicNotFoundException;
import com.example.music_streaming.exceptions.MusicStreamException;
import com.example.music_streaming.exceptions.SessionNotFoundException;
import com.example.music_streaming.kafka.KafkaProducer;
import com.example.music_streaming.models.Music;
import com.example.music_streaming.models.MusicDto;
import com.example.music_streaming.models.MusicSearchEventDto;
import com.example.music_streaming.models.MusicStreamEvent;
import com.example.music_streaming.repositories.MusicRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MusicServiceImpl implements MusicService{

  Map<String, MusicStreamEvent> sessionMap=new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(MusicServiceImpl.class);
    private final MusicRepository musicRepository;

    ModelMapper modelMapper=new ModelMapper();

    private final KafkaProducer kafkaProducer;


    public MusicServiceImpl(MusicRepository musicRepository,KafkaProducer kafkaProducer) {
        this.musicRepository = musicRepository;
        this.kafkaProducer=kafkaProducer;
    }

    @Override
    public List<MusicDto> getAllSongs() {
        List<MusicDto> musicDtos=new ArrayList<>();

        for (Music music : musicRepository.findAll()) {
           musicDtos.add(modelMapper.map(music, MusicDto.class));
        }
        return musicDtos;
    }

    @Override
    public void streamMusic(String sessionId,String userEmail,String musicId, String rangeHeader, HttpServletResponse response) {

        final int BUFFER_SIZE = 1024 * 16;
        MusicStreamEvent streamEvent=new MusicStreamEvent(sessionId,userEmail,musicId, LocalDateTime.now());
        if(!sessionMap.containsKey(sessionId)){
            sessionMap.put(sessionId,streamEvent);
        }


        MusicDto musicDto = getSongDetails(musicId);
        System.out.printf("Got request and streaming music : %s%n",musicDto.getMusicName());
        File audioFile = new File("sg/"+musicDto.getMusicUrl());
        long fileLength = audioFile.length();

        try (RandomAccessFile inputFile = new RandomAccessFile(audioFile, "r")) {
            long start = 0, end = fileLength - 1;

            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String[] ranges = rangeHeader.substring(6).split("-");
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1 && !ranges[1].isEmpty()) {
                    end = Long.parseLong(ranges[1]);
                }
            }

            long contentLength = end - start + 1;

            response.reset();
            response.setBufferSize(BUFFER_SIZE);
            response.setHeader("Content-Type", "audio/mpeg");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", String.valueOf(contentLength));
            response.setHeader("Content-Range", String.format("bytes %d-%d/%d", start, end, fileLength));
            response.setStatus(rangeHeader == null ? HttpServletResponse.SC_OK : HttpServletResponse.SC_PARTIAL_CONTENT);

            inputFile.seek(start);
            byte[] buffer = new byte[BUFFER_SIZE];
            long bytesToRead = contentLength;
            OutputStream output = response.getOutputStream();

            while (bytesToRead > 0) {
                int read = inputFile.read(buffer, 0, (int) Math.min(buffer.length, bytesToRead));
                if (read == -1) break;
                output.write(buffer, 0, read);
                bytesToRead -= read;
            }

            output.flush();
            //endStream(sessionId,userEmail,musicId);

        } catch (IOException e) {

            throw new MusicStreamException("Error Streaming Music!!Try Later");

        }
    }

    public String bulkAdd(MultipartFile file) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> rawList = mapper.readValue(file.getInputStream(), new TypeReference<>() {});
            List<Music> musicList = new ArrayList<>();

            for (Map<String, Object> raw : rawList) {
                Music music = new Music();
                music.setId((String) raw.get("id"));
                music.setArtistName((String) raw.get("artistName"));
                music.setMusicName((String) raw.get("musicName"));
                music.setMusicUrl((String) raw.get("musicUrl"));
                music.setAlbum((String) raw.get("album"));
                music.setMusicThumbnailUrl((String) raw.get("musicThumbnailUrl"));
                music.setCreatedDate(Music.convertStringToDateTime((String) raw.get("createdDate")));

                musicList.add(music);
            }

            musicRepository.saveAll(musicList);
        return "Success!!";
    }catch (IOException e){
            log.error("Error : ",e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void endStream(String sessionId, String userEmail) {
        if(!sessionMap.containsKey(sessionId)){
            throw new SessionNotFoundException("Session with Id "+sessionId+" Not Found !");
        }
        MusicStreamEvent streamEvent = sessionMap.get(sessionId);
        streamEvent.setEndTime(LocalDateTime.now());
        sessionMap.replace(sessionId,streamEvent);

        //TODO:WRITE INTO KAFKA TOPIC STREAM
        kafkaProducer.sendEvent("stream",streamEvent);

        sessionMap.remove(sessionId);
    }


    @Override
    public List<MusicDto> searchByKeyword(String userEmail,String keyword) {
        MusicSearchEventDto searchEvent=new MusicSearchEventDto(userEmail,keyword, LocalDateTime.now());
        System.out.println("Got Keyword : "+keyword);
        kafkaProducer.sendEvent("search",searchEvent);

        return musicRepository.searchByKeyword(keyword)
                .stream()
                .map((music)->modelMapper.map(music,MusicDto.class))
                .toList();
    }

    @Override
    public List<MusicDto> searchByArtist(String userEmail,String keyword) {

        MusicSearchEventDto searchEvent=new MusicSearchEventDto(userEmail,keyword, LocalDateTime.now());
        System.out.println("Got Artist Keyword : "+keyword);
        kafkaProducer.sendEvent("search",searchEvent);

        return musicRepository.findAllByArtistNamePartial(keyword)
                .stream()
                .map((music)->modelMapper.map(music,MusicDto.class))
                .toList();
    }

    @Override
    public List<MusicDto> searchByAlbum(String userEmail,String keyword) {

        MusicSearchEventDto searchEvent=new MusicSearchEventDto(userEmail,keyword, LocalDateTime.now());
        System.out.println("Got Album Keyword : "+keyword);
        kafkaProducer.sendEvent("search",searchEvent);

        return musicRepository.findAllByAlbumPartial(keyword)
                .stream()
                .map((music)->modelMapper.map(music,MusicDto.class))
                .toList();
    }

    @Override
    public List<MusicDto> searchByMusicName(String userEmail,String keyword) {

        MusicSearchEventDto searchEvent=new MusicSearchEventDto(userEmail,keyword, LocalDateTime.now());
        kafkaProducer.sendEvent("search",searchEvent);


        return musicRepository.findAllByMusicNamePartial(keyword)
                .stream()
                .map((music)->modelMapper.map(music,MusicDto.class))
                .toList();
    }

    @Override
    public MusicDto getSongDetails(String musicId) {
        Music music = musicRepository.findById(musicId).orElseThrow(()-> new MusicNotFoundException("Song not found!!"));
        return modelMapper.map(music,MusicDto.class);
    }

}
