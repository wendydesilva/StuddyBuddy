package com.example.studybuddy.service;

import com.example.studybuddy.entity.Student;
import com.example.studybuddy.entity.StudyGroup;
import com.example.studybuddy.repository.StudentRepository;
import com.example.studybuddy.repository.StudyGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudyGroupService {
    @Autowired
    private StudyGroupRepository studyGroupRepository;
    @Autowired
    private StudentRepository studentRepository;

    //getting all groups a student has joined
    public List<StudyGroup> getGroupsByStudent(long studentId){
        return studyGroupRepository.findByMembersId(studentId);
    }

    //getting all groups (to/for joining)
    public List<StudyGroup> getAllGroups(){
        return studyGroupRepository.findAll();
    }

    //Creating a new group
    public StudyGroup createGroup(String groupName, long studentId){
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        StudyGroup group = new StudyGroup();
        group.setGroupName(groupName);
        group.setCreatedDate(LocalDate.now());
        group.setMembers(List.of(student)); // creator is set as the first member
        return studyGroupRepository.save(group);
    }

    //joining an already created group
    public void joinGroup(long groupId, long studentId){
        StudyGroup group = studyGroupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        if(!group.getMembers().contains(student)){
            group.getMembers().add(student);
            studyGroupRepository.save(group);
        }
    }
}
