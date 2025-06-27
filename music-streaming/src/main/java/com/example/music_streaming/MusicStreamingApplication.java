package com.example.music_streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MusicStreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicStreamingApplication.class, args);
	}

}
