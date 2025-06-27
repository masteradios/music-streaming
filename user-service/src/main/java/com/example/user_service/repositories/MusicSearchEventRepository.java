package com.example.user_service.repositories;

import com.example.user_service.models.MusicSearchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicSearchEventRepository extends JpaRepository<MusicSearchEvent,String> {
}
