package com.example.user_service.repositories;

import com.example.user_service.models.MusicStreamEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicStreamEventRepository extends JpaRepository<MusicStreamEvent, String> {
}
