package com.example.user_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class MusicSearchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String userEmail;
    private String searchQuery;
    private LocalDateTime searchedAt;

    public MusicSearchEvent(String userEmail, String searchQuery, LocalDateTime searchedAt) {


        this.userEmail = userEmail;
        this.searchQuery = searchQuery;
        this.searchedAt = searchedAt;
    }

    public MusicSearchEvent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public LocalDateTime getSearchedAt() {
        return searchedAt;
    }

    public void setSearchedAt(LocalDateTime searchedAt) {
        this.searchedAt = searchedAt;
    }
}
