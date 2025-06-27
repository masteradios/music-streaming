package com.example.music_streaming.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Document(indexName = "music")
public class Music {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String musicName;

    @Field(type = FieldType.Text)
    private String album;

    @Field(type = FieldType.Text)
    private String artistName;

    @Field(type = FieldType.Date)
    private LocalDate  createdDate;

    @Field(type = FieldType.Text)
    private String musicUrl;

    @Field(type = FieldType.Text)
    private String musicThumbnailUrl;


    public static LocalDate convertStringToDateTime(String rawDate){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(rawDate, inputFormatter);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getMusicThumbnailUrl() {
        return musicThumbnailUrl;
    }

    public void setMusicThumbnailUrl(String musicThumbnailUrl) {
        this.musicThumbnailUrl = musicThumbnailUrl;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
