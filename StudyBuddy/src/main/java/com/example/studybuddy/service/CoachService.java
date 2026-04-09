package com.example.studybuddy.service;

import com.example.studybuddy.entity.Coach;
import com.example.studybuddy.entity.Event;
import com.example.studybuddy.model.Session;
import com.example.studybuddy.repository.*;
import com.example.studybuddy.session.CoachSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachService {
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private EventRepository eventRepository;

    //getting coach profile
    public Coach getCoach(long id){
        return coachRepository.findById(id).orElseThrow(() -> new RuntimeException("Coach isn't found"));
    }

    //getting all sessions for weekly schedule
    public List<Session> getSessionsByCoach(long coachId){
        return sessionRepository.findAll();
    }

    //total number of classes
    public long getTotalClasses(long coachId){
        return sessionRepository.count();
    }

    //total number of students
    public long getTotalStudents(long coachId){
        return sessionRepository.count();
    }

    //generating pay = total sessions confirmed * hourly rate
    public double getGeneratedPay(long coachId){
        Coach coach = getCoach(coachId);
        long totalClasses = getTotalClasses(coachId);
        return totalClasses * coach.getHourlyRate();
    }

    //total hours worked
    public long getTotalHours(long coachId){
        return getTotalClasses(coachId); // the notion being 1 session = 1 hour
    }

    //upcoming events
    public List<Event> getUpcomingEvent(long coachId){
        return eventRepository.findByCoachId(coachId);
    }
}
