package com.example.studybuddy.controller;

import com.example.studybuddy.model.Reward;
import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.RewardRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardControllerTest {

    @Mock
    RewardRepository repo;
    @Mock
    HttpSession session;
    @Mock
    Model model;
    @InjectMocks
    RewardController controller;

    @Test
    void rewards_success() {

        // create student with points
        User u = new User();
        u.setRole(Role.STUDENT);
        u.setTotalPoints(50);

        // mock session and rewards
        when(session.getAttribute("loggedUser")).thenReturn(u);
        when(repo.findByStudent(u)).thenReturn(List.of(new Reward()));

        // call method
        String view = controller.studentRewards(session, model);

        // check correct view
        assertEquals("student/rewards", view);

        // verify points sent to view
        verify(model).addAttribute("totalPoints", 50);
    }

    @Test
    void rewards_redirect() {

        // mock no user logged in
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // check redirect to login
        assertEquals("redirect:/login", controller.studentRewards(session, model));
    }
}