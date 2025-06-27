package com.example.user_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class MusicStreamEvent {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String sessionId;
    private String userEmail;
    private String musicId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public MusicStreamEvent(String id, String sessionId, String userEmail, String musicId, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.sessionId = sessionId;
        this.userEmail = userEmail;
        this.musicId = musicId;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
