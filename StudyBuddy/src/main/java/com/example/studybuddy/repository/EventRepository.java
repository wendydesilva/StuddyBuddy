package com.example.studybuddy.repository;

import com.example.studybuddy.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCoachId(long coachId);
}
