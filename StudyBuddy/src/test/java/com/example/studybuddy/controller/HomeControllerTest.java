package com.example.studybuddy.controller;

import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    HomeController controller = new HomeController();

    @Test
    void studentHome_success() {
        HttpSession s = mock(HttpSession.class);
        Model m = mock(Model.class);

        User u = new User(); u.setRole(Role.STUDENT);
        when(s.getAttribute("loggedUser")).thenReturn(u);

        assertEquals("student/home", controller.studentHome(s,m));
    }

    @Test
    void studentHome_redirect() {
        HttpSession s = mock(HttpSession.class);
        Model m = mock(Model.class);

        when(s.getAttribute("loggedUser")).thenReturn(null);

        assertEquals("redirect:/login", controller.studentHome(s,m));
    }
}