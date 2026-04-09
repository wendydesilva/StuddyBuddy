package com.example.studybuddy.controller;

import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    HomeController controller = new HomeController(); // create controller

    @Test
    void studentHome_success() {

        // mock session and model
        HttpSession s = mock(HttpSession.class);
        Model m = mock(Model.class);

        // create student user
        User u = new User();
        u.setRole(Role.STUDENT);

        // mock logged-in user
        when(s.getAttribute("loggedUser")).thenReturn(u);

        // check correct view
        assertEquals("student/home", controller.studentHome(s,m));
    }

    @Test
    void studentHome_redirect() {

        // mock session and model
        HttpSession s = mock(HttpSession.class);
        Model m = mock(Model.class);

        // mock no user logged in
        when(s.getAttribute("loggedUser")).thenReturn(null);

        // check redirect to login
        assertEquals("redirect:/login", controller.studentHome(s,m));
    }
}