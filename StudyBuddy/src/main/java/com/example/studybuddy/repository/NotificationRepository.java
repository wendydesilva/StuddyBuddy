package com.example.studybuddy.repository;

import com.example.studybuddy.model.Notification;
import com.example.studybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
}