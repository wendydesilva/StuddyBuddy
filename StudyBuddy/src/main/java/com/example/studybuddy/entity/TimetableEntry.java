package com.example.studybuddy.entity;

import jakarta.persistence.*;

@Entity
public class TimetableEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //written close to the unique key which is the id
    private long id;

    //link to student and subject
    @ManyToOne //many attendance record to one student
    @JoinColumn(name = "student_id") //JPA name for the foreign key column in the db
    private Student student;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String subjectName;
    private String roomNumber;

    //Getters
    public long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    //Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
