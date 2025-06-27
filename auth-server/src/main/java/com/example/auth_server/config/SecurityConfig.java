package com.example.auth_server.config;
import com.example.auth_server.jwt.AuthEntryPointJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/auth/signup").permitAll()
                                .requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/auth/validate").permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling((configurer)->configurer.authenticationEntryPoint(authEntryPointJwt))
                .csrf(AbstractHttpConfigurer::disable); // Optional: disable CSRF for testing if using POST

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
