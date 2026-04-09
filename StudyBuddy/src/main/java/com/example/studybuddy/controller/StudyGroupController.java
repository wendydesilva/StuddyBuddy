package com.example.studybuddy.controller;

import com.example.studybuddy.entity.StudyGroup;
import com.example.studybuddy.service.CoachService;
import com.example.studybuddy.service.StudyGroupService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student/groups")
public class StudyGroupController {
    @Autowired
    private StudyGroupService studyGroupService;

    // views joined groups and available groups as well
    @GetMapping("/{studentId}")
    public String studyGroupPage(@PathVariable long studentId, Model model, HttpSession session) {
        List<StudyGroup> myGroup = studyGroupService.getGroupsByStudent(studentId);
        List<StudyGroup> allGroups = studyGroupService.getAllGroups();
        model.addAttribute("student", session.getAttribute("loggedUser"));
        model.addAttribute("myGroups", myGroup);
        model.addAttribute("allGroups", allGroups);
        model.addAttribute("studentId", studentId);
        return "student/study-groups";
    }

    //creating a new group
    @PostMapping("/{studentId}/create")
    public String createGroup(@PathVariable long studentId, @RequestParam String groupName){
        studyGroupService.createGroup(groupName, studentId);
        return "redirect:/student/groups/" + studentId;
    }

    //joining an existing group
    @PostMapping("/{studentId}/join")
    public String joinGroup(@PathVariable long studentId, @RequestParam long groupId){
        studyGroupService.joinGroup(groupId, studentId);
        return "redirect:/student/groups/" + studentId;
    }
}
