package com.example.studybuddy.service;

import com.example.studybuddy.model.Session;
import com.example.studybuddy.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepo;

    public Session bookSession(Session session){
        session.setStatus("PENDING");
        return sessionRepo.save(session);
    }

    public void cancelSession(Long id){
        sessionRepo.findById(id).ifPresent(session -> {
            session.setStatus("CANCELLED");
            sessionRepo.save(session);
        });
    }
}