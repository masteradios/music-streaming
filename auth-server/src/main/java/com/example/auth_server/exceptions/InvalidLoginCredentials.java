package com.example.auth_server.exceptions;

public class InvalidLoginCredentials extends RuntimeException {
    public InvalidLoginCredentials(String message) {
        super(message);
    }
}
