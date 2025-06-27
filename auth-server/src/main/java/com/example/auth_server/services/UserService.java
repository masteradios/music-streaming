package com.example.auth_server.services;

import com.example.auth_server.models.LoginRequestDto;
import com.example.auth_server.models.LoginResponseDto;
import com.example.auth_server.models.SignUpRequestDto;
import com.example.auth_server.models.UserDto;

public interface UserService {

    UserDto signUpUser(SignUpRequestDto requestDto);
    LoginResponseDto loginUser(LoginRequestDto loginRequestDto);
    boolean validateToken(String authHeader);

}
