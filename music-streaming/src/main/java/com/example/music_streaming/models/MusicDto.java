package com.example.music_streaming.models;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

public class MusicDto {

    private String id;
    private String musicName;
    private String artistName;
    private LocalDate createdDate;
    private String musicUrl;
    private String album;
    private String musicThumbnailUrl;

    public String getMusicThumbnailUrl() {
        return musicThumbnailUrl;
    }

    public void setMusicThumbnailUrl(String musicThumbnailUrl) {
        this.musicThumbnailUrl = musicThumbnailUrl;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
