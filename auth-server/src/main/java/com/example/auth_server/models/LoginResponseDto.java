package com.example.auth_server.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class LoginResponseDto {


    @NotEmpty(message = "Email can't be blank !")
    @Email(message = "Enter valid Email !")
    private String email;
    @NotEmpty(message = "User Name can't be empty !")
    private String userName;

    private String jwtToken;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String email, String userName, String jwtToken) {
        this.email = email;
        this.userName = userName;
        this.jwtToken = jwtToken;
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

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
