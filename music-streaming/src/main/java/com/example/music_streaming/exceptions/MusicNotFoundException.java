package com.example.music_streaming.exceptions;

public class MusicNotFoundException extends RuntimeException {
    public MusicNotFoundException(String message) {
        super(message);
    }
}
