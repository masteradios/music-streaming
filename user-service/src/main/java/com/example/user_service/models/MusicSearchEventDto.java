package com.example.user_service.models;

import java.time.LocalDateTime;

public class MusicSearchEventDto {

    private String userEmail;
    private String searchQuery;
    private LocalDateTime searchedAt;

    public MusicSearchEventDto() {
    }

    public MusicSearchEventDto(String userEmail, String searchQuery, LocalDateTime searchedAt) {
        this.userEmail = userEmail;
        this.searchQuery = searchQuery;
        this.searchedAt = searchedAt;
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
