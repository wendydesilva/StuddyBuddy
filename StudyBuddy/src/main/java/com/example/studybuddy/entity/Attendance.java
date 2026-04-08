package com.example.studybuddy.entity;

import jakarta.persistence.*;

import com.example.studybuddy.entity.Student;
import java.time.LocalDate;

@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //written close to the unique key which is the id
    private long id;

    //link to student and subject
    @ManyToOne //many attendance record to one student
    @JoinColumn(name = "student_id") //JPA name for the foreign key column in the db
    private Student student;

    @ManyToOne //many attendance record to one subject
    @JoinColumn(name = "subject_id") //JPA name for the foreign key column in the db
    private Subject subject;

    private LocalDate attendanceDate;
    private boolean present;

    //Getters
    public long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Subject getSubject() {
        return subject;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public boolean isPresent() {
        return present;
    }

    //Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
