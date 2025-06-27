package com.example.auth_server;

import com.example.auth_server.exceptions.InvalidLoginCredentials;
import com.example.auth_server.exceptions.UserAlreadyExistsException;
import com.example.auth_server.models.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException e){

        ErrorResponseDto errorResponseDto=new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);

    }


    @ExceptionHandler(InvalidLoginCredentials.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidLoginCredException(InvalidLoginCredentials e){

        ErrorResponseDto errorResponseDto=new ErrorResponseDto(e.getMessage(), HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDto);

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgNotValidException(MethodArgumentNotValidException e){

        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));




        ErrorResponseDto errorResponseDto=new ErrorResponseDto(errors, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);

    }




}
