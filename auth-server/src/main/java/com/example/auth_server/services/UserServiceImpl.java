package com.example.auth_server.services;

import com.example.auth_server.exceptions.UserAlreadyExistsException;
import com.example.auth_server.exceptions.InvalidLoginCredentials;
import com.example.auth_server.jwt.JwtUtil;
import com.example.auth_server.models.*;
import com.example.auth_server.repositories.UserRepository;
import io.jsonwebtoken.JwtException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;
    ModelMapper mapper=new ModelMapper();
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil=jwtUtil;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public UserDto signUpUser(SignUpRequestDto requestDto) {
        Optional<User> existingUser = userRepository.findByEmail(requestDto.getEmail());

        if(existingUser.isPresent()){

            throw new UserAlreadyExistsException("User with this Email already Exists !");
        }

        User user = mapper.map(requestDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);

        return mapper.map(newUser, UserDto.class);
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).filter((u)-> passwordEncoder.matches(loginRequestDto.getPassword(), u.getPassword())).orElseThrow(()->new InvalidLoginCredentials("Invalid Credentials !"));
        String token=jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDto(user.getEmail(), user.getUserName(), token);
    }

    @Override
    public boolean validateToken(String authHeader) {

        return jwtUtil.validateToken(authHeader.substring(7));

    }


}
