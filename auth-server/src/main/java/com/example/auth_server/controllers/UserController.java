package com.example.auth_server.controllers;


import com.example.auth_server.jwt.JwtUtil;
import com.example.auth_server.models.LoginRequestDto;
import com.example.auth_server.models.LoginResponseDto;
import com.example.auth_server.models.SignUpRequestDto;
import com.example.auth_server.models.UserDto;
import com.example.auth_server.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto){

        UserDto userDto = userService.signUpUser(signUpRequestDto);
        return ResponseEntity.status(201).body(userDto);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){

        LoginResponseDto responseDto = userService.loginUser(loginRequestDto);
        return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validate(@RequestHeader("Authorization") String authHeader){
        if(authHeader==null|| !authHeader.startsWith("Bearer")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(userService.validateToken(authHeader)){
            String username= jwtUtil.getUsernameFromJwt(authHeader.substring(7));
            return ResponseEntity.ok().header("X-USER-EMAIL",username).build();
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
