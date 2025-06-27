package com.example.music_streaming.models;



import java.time.LocalDateTime;


public class MusicStreamEvent {
    
    private String sessionId;
    private String userEmail;
    private String musicId;
    private LocalDateTime startTime;


    private LocalDateTime endTime;

    public MusicStreamEvent(String sessionId, String userEmail, String musicId, LocalDateTime startTime) {
        this.sessionId = sessionId;
       
        this.userEmail = userEmail;
        this.musicId = musicId;
        this.startTime = startTime;
    }

    public MusicStreamEvent() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
