package com.example.studybuddy.controller;

import com.example.studybuddy.model.Notification;
import com.example.studybuddy.model.Prize;
import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.NotificationRepository;
import com.example.studybuddy.repository.PrizeRepository;
import com.example.studybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrizeControllerTest {

    @Mock
    PrizeRepository repo;
    @Mock
    NotificationRepository notifRepo;
    @Mock
    UserRepository userRepo;
    @Mock
    HttpSession session;
    @Mock
    Model model;
    @Mock
    RedirectAttributes ra;

    @InjectMocks
    PrizeController controller;

    @Test
    void requestPrize_fail() {

        // create student with low points
        User u = new User();
        u.setRole(Role.STUDENT);
        u.setTotalPoints(10);

        // mock session and used points
        when(session.getAttribute("loggedUser")).thenReturn(u);
        when(repo.getTotalApprovedPointsUsed(u)).thenReturn(0);

        // call method
        String view = controller.requestPrize("A", "B", 50, session, ra);

        // check redirect
        assertEquals("redirect:/student/prizes", view);

        // verify error message
        verify(ra).addFlashAttribute(eq("errorMessage"), any());
    }

    @Test
    void approvePrize_success() {

        // create admin user
        User admin = new User();
        admin.setRole(Role.ADMIN);

        // create prize
        Prize p = new Prize();
        p.setStudent(new User());

        // mock session and repo
        when(session.getAttribute("loggedUser")).thenReturn(admin);
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        // call method
        String view = controller.approvePrize(1L, session, ra);

        // check redirect
        assertEquals("redirect:/admin/prizes", view);

        // verify status updated
        assertEquals(Prize.PrizeStatus.APPROVED, p.getStatus());
    }
}