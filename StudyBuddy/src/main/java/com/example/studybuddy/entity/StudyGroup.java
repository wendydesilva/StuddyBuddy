package com.example.studybuddy.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //written close to the unique key which is the id
    private long id;
    private String groupName;
    private LocalDate createdDate;

    //many students can join many groups
    @ManyToMany
    @JoinTable(
            name = "student_study_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> members;

    //Getters
    public long getId() {
        return id;
    }
    public String getGroupName() {
        return groupName;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    public List<Student> getMembers() {
        return members;
    }

    //Setters
    public void setId(long id) {
        this.id = id;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    public void setMembers(List<Student> members) {
        this.members = members;
    }

    //helper (returns member count)
    public int getMemberCount(){
        return members != null ? members.size() : 0;
    }
}
