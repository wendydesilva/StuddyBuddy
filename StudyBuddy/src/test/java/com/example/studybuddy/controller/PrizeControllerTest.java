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

    @Mock PrizeRepository repo;
    @Mock NotificationRepository notifRepo;
    @Mock UserRepository userRepo;
    @Mock HttpSession session;
    @Mock Model model;
    @Mock RedirectAttributes ra;

    @InjectMocks PrizeController controller;

    @Test
    void requestPrize_fail() {
        User u = new User();
        u.setRole(Role.STUDENT);
        u.setTotalPoints(10);

        when(session.getAttribute("loggedUser")).thenReturn(u);
        when(repo.getTotalApprovedPointsUsed(u)).thenReturn(0);

        String view = controller.requestPrize("A","B",50,session,ra);

        assertEquals("redirect:/student/prizes", view);
        verify(ra).addFlashAttribute(eq("errorMessage"), any());
    }

    @Test
    void approvePrize_success() {
        User admin = new User();
        admin.setRole(Role.ADMIN);

        Prize p = new Prize();
        p.setStudent(new User());

        when(session.getAttribute("loggedUser")).thenReturn(admin);
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        String view = controller.approvePrize(1L, session, ra);

        assertEquals("redirect:/admin/prizes", view);
        assertEquals(Prize.PrizeStatus.APPROVED, p.getStatus());
    }
}