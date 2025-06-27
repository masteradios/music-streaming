package com.example.music_streaming.services;

import com.example.music_streaming.models.MusicDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MusicService {
    List<MusicDto> getAllSongs();
    void streamMusic(String sessionId,String userEmail,String musicId, String rangeHeader, HttpServletResponse response);

    MusicDto getSongDetails(String songId);
    String bulkAdd(MultipartFile file);
    void endStream(String sessionId,String userEmail);
    List<MusicDto> searchByKeyword(String userEmail,String keyword);
    List<MusicDto> searchByArtist(String userEmail,String keyword);
    List<MusicDto> searchByAlbum(String userEmail ,String keyword);
    List<MusicDto> searchByMusicName(String userEmail,String keyword);

}
