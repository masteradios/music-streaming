package com.example.auth_server.models;

import jakarta.validation.constraints.NotEmpty;

public class UserDto {

    private String email;
    private String userName;

    public UserDto(String email, String userName) {
        this.email = email;
        this.userName = userName;
    }

    public UserDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
