package com.example.auth_server.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Entity(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @NotEmpty(message = "Email can't be blank ")
    @Email(message = "Enter valid Email")
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty(message = "User Name can't be empty !")
    private String userName;


    public User(String id,String email, String password,String userName) {

        this.id=id;
        this.userName=userName;
        this.email = email;
        this.password = password;
    }

    public User() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
