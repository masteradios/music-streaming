package com.example.auth_server.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class SignUpRequestDto {

    @NotEmpty(message = "Email can't be blank ")
    @Email(message = "Enter valid Email")
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty(message = "User Name can't be empty !")
    private String userName;

    public SignUpRequestDto(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
    }

    public SignUpRequestDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
