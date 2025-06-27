package com.example.music_streaming;

import com.example.music_streaming.exceptions.MusicNotFoundException;
import com.example.music_streaming.exceptions.MusicStreamException;
import com.example.music_streaming.exceptions.SessionNotFoundException;
import com.example.music_streaming.models.ErrorResponseApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MusicStreamException.class)
    public ResponseEntity<ErrorResponseApi> handleMusicStreamException(MusicStreamException e){
        ErrorResponseApi errorResponseApi=new ErrorResponseApi(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseApi);
    }

    @ExceptionHandler(MusicNotFoundException.class)
    public ResponseEntity<ErrorResponseApi> handleMusicNotFoundException(MusicNotFoundException e){
        ErrorResponseApi errorResponseApi=new ErrorResponseApi(HttpStatus.NOT_FOUND.value(),e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseApi);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ErrorResponseApi> handleSessionNotFoundException(SessionNotFoundException e){
        ErrorResponseApi errorResponseApi=new ErrorResponseApi(HttpStatus.NOT_FOUND.value(),e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseApi);
    }
}
