package com.example.studybuddy.service;

import com.example.studybuddy.entity.Coach;
import com.example.studybuddy.entity.Event;
import com.example.studybuddy.repository.CoachRepository;
import com.example.studybuddy.repository.CoachSessionRepository;
import com.example.studybuddy.repository.EventRepository;
import com.example.studybuddy.repository.StudentRepository;
import com.example.studybuddy.session.CoachSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachService {
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private CoachSessionRepository coachSessionRepository;
    @Autowired
    private EventRepository eventRepository;

    //getting coach profile
    public Coach getCoach(long id){
        return coachRepository.findById(id).orElseThrow(() -> new RuntimeException("Coach isn't found"));
    }

    //getting all sessions for weekly schedule
    public List<CoachSession> getSessionsByCoach(long coachId){
        return coachSessionRepository.findByCoachId(coachId);
    }

    //total number of classes
    public long getTotalClasses(long coachId){
        return coachSessionRepository.countByCoachId(coachId);
    }

    //total number of students
    public long getTotalStudents(long coachId){
        return coachSessionRepository.countByCoachIdAndStudentIsNotNull(coachId);
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
