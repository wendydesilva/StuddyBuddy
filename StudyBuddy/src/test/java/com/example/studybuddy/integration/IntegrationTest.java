package com.example.studybuddy.integration;

import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc; // simulate HTTP requests

    @Autowired
    private UserRepository userRepository; // access test database

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // clear users before each test
    }

    @Test
    void loginPageLoads() throws Exception {
        // check login page loads
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void studentHomeWithoutLoginRedirects() throws Exception {
        // check redirect when not logged in
        mockMvc.perform(get("/student/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void loginSuccessRedirectsToStudentHome() throws Exception {
        // create test student
        User user = new User();
        user.setName("Test");
        user.setEmail("test@test.com");
        user.setPassword("123");
        user.setRole(Role.STUDENT);
        user.setTotalPoints(0);
        userRepository.save(user);

        // perform login request
        mockMvc.perform(post("/login")
                        .param("email", "test@test.com")
                        .param("password", "123")
                        .param("role", "STUDENT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/home"));
    }

    @Test
    void studentHomeWithLoginLoads() throws Exception {
        // create test student
        User user = new User();
        user.setName("Student");
        user.setEmail("student@test.com");
        user.setPassword("123");
        user.setRole(Role.STUDENT);
        user.setTotalPoints(0);
        userRepository.save(user);

        // create logged-in session
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loggedUser", user);

        // check home page loads
        mockMvc.perform(get("/student/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("student/home"));
    }
}