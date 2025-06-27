package com.example.music_streaming.controllers;

import com.example.music_streaming.models.MusicDto;
import com.example.music_streaming.services.MusicService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/music")
public class MusicStreamController {

        @Autowired
        private MusicService musicService;



        @GetMapping("/stream/{musicId}")
        public void streamAudio(

                @PathVariable String musicId,
                                @RequestHeader(value = "Range", required = false) String rangeHeader,
                                @RequestHeader(value = "X-SESSION-ID") String sessionId,
                                @RequestHeader(value = "X-USER-EMAIL") String userEmail,

                                HttpServletResponse response)  {
            musicService.streamMusic(sessionId,userEmail,musicId, rangeHeader,response);

        }


        @PostMapping("/stream/end")
        public ResponseEntity<?> endStream(
                @RequestHeader(value = "X-SESSION-ID") String sessionId,
                @RequestHeader(value = "X-USER-EMAIL") String userEmail
        ){
            musicService.endStream(sessionId,userEmail);
            return ResponseEntity.ok().build();
        }

        @GetMapping("/image/{musicId}")
        public ResponseEntity<?> getThumbnail(@PathVariable  String musicId){

            MusicDto musicDto = musicService.getSongDetails(musicId);
            String thumbnailUrl="sg/thumbnails/"+musicDto.getMusicThumbnailUrl();
            Path thumbnailPath = Paths.get(thumbnailUrl);
            Resource resource = new FileSystemResource(thumbnailPath);
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
        }

        @GetMapping("/getSongs")
        public ResponseEntity<?> getAllMusic(){
            System.out.println("Got request");
            List<MusicDto> musicDtos = musicService.getAllSongs();
            return ResponseEntity.ok(musicDtos);
        }


        @PostMapping("/add")
    public ResponseEntity<String> addMusic(@RequestParam MultipartFile file){
            String response = musicService.bulkAdd(file);
            return ResponseEntity.ok(response);
        }

       @GetMapping("/search/{keyword}")
    public ResponseEntity<List<MusicDto>> searchByKeyword(@PathVariable String keyword,
                                                          @RequestHeader("X-USER-EMAIL") String userEmail){

           return ResponseEntity.ok(musicService.searchByKeyword(userEmail,keyword));
       }

    @GetMapping("/search/artist/{keyword}")
    public ResponseEntity<List<MusicDto>> searchByArtist(@PathVariable String keyword,
                                                         @RequestHeader("X-USER-EMAIL") String userEmail){

        return ResponseEntity.ok(musicService.searchByArtist(userEmail,keyword));
    }
    @GetMapping("/search/musicName/{keyword}")
    public ResponseEntity<List<MusicDto>> searchByMusicName(@PathVariable String keyword,
                                                            @RequestHeader("X-USER-EMAIL") String userEmail){

        return ResponseEntity.ok(musicService.searchByMusicName(userEmail,keyword));
    }
    @GetMapping("/search/album/{keyword}")
    public ResponseEntity<List<MusicDto>> searchByAlbum(@PathVariable String keyword,
                                                        @RequestHeader("X-USER-EMAIL") String userEmail){

        return ResponseEntity.ok(musicService.searchByAlbum(userEmail,keyword));
    }

}
