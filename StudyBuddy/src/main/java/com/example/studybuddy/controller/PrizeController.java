package com.example.studybuddy.controller;

import com.example.studybuddy.model.Notification;
import com.example.studybuddy.model.Prize;
import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.NotificationRepository;
import com.example.studybuddy.repository.PrizeRepository;
import com.example.studybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class PrizeController {

    private final PrizeRepository prizeRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // inject repositories
    public PrizeController(PrizeRepository prizeRepository,
                           NotificationRepository notificationRepository,
                           UserRepository userRepository) {
        this.prizeRepository = prizeRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/student/prizes")
    public String studentPrizes(HttpSession session, Model model) {

        // get logged-in student
        User student = (User) session.getAttribute("loggedUser");

        // allow only student
        if (student == null || student.getRole() != Role.STUDENT) return "redirect:/login";

        // calculate available points
        Integer usedPoints = prizeRepository.getTotalApprovedPointsUsed(student);
        int availablePoints = student.getTotalPoints() - (usedPoints == null ? 0 : usedPoints);

        // send data to view
        model.addAttribute("prizes", prizeRepository.findByStudent(student));
        model.addAttribute("totalEarned", student.getTotalPoints());
        model.addAttribute("availablePoints", availablePoints);
        model.addAttribute("prizeTiers", getPrizeTiers());

        return "student/prizes";
    }

    @PostMapping("/student/prizes/request")
    public String requestPrize(@RequestParam String prizeName,
                               @RequestParam String description,
                               @RequestParam Integer pointsRequired,
                               HttpSession session,
                               RedirectAttributes ra) {

        // get logged-in student
        User student = (User) session.getAttribute("loggedUser");

        // allow only student
        if (student == null || student.getRole() != Role.STUDENT) return "redirect:/login";

        // check available points
        Integer usedPoints = prizeRepository.getTotalApprovedPointsUsed(student);
        int availablePoints = student.getTotalPoints() - (usedPoints == null ? 0 : usedPoints);

        // not enough points
        if (availablePoints < pointsRequired) {
            ra.addFlashAttribute("errorMessage", "Not enough points for this prize.");
            return "redirect:/student/prizes";
        }

        // create new prize request
        Prize prize = new Prize();
        prize.setPrizeName(prizeName);
        prize.setDescription(description);
        prize.setPointsRequired(pointsRequired);
        prize.setRequestedDate(LocalDate.now());
        prize.setStatus(Prize.PrizeStatus.PENDING);
        prize.setStudent(student);

        prizeRepository.save(prize);

        // notify admins
        List<User> admins = userRepository.findByRole(Role.ADMIN);
        for (User admin : admins) {
            Notification adminNotification = new Notification();
            adminNotification.setUser(admin);
            adminNotification.setMessage("New prize request from " + student.getName() + ": " + prize.getPrizeName());
            notificationRepository.save(adminNotification);
        }

        ra.addFlashAttribute("successMessage", "Prize request submitted.");
        return "redirect:/student/prizes";
    }

    @GetMapping("/admin/prizes")
    public String adminPrizes(HttpSession session, Model model) {

        // get logged-in admin
        User admin = (User) session.getAttribute("loggedUser");

        // allow only admin
        if (admin == null || admin.getRole() != Role.ADMIN) return "redirect:/login";

        // send prize data
        model.addAttribute("pendingPrizes", prizeRepository.findByStatus(Prize.PrizeStatus.PENDING));
        model.addAttribute("allPrizes", prizeRepository.findAll());

        return "admin/prizes";
    }

    @PostMapping("/admin/prizes/approve")
    @Transactional
    public String approvePrize(@RequestParam Long prizeId,
                               HttpSession session,
                               RedirectAttributes ra) {

        // get logged-in admin
        User admin = (User) session.getAttribute("loggedUser");

        // allow only admin
        if (admin == null || admin.getRole() != Role.ADMIN) return "redirect:/login";

        // find prize
        Prize prize = prizeRepository.findById(prizeId).orElse(null);

        if (prize != null) {
            // approve prize
            prize.setStatus(Prize.PrizeStatus.APPROVED);
            prize.setAwardedDate(LocalDate.now());
            prizeRepository.save(prize);

            // notify student
            Notification studentNotification = new Notification();
            studentNotification.setUser(prize.getStudent());
            studentNotification.setMessage("Your prize request was approved: " + prize.getPrizeName());
            notificationRepository.save(studentNotification);
        }

        ra.addFlashAttribute("successMessage", "Prize approved successfully.");
        return "redirect:/admin/prizes";
    }

    @PostMapping("/admin/prizes/reject")
    @Transactional
    public String rejectPrize(@RequestParam Long prizeId,
                              @RequestParam(required = false) String adminNote,
                              HttpSession session,
                              RedirectAttributes ra) {

        // get logged-in admin
        User admin = (User) session.getAttribute("loggedUser");

        // allow only admin
        if (admin == null || admin.getRole() != Role.ADMIN) return "redirect:/login";

        // find prize
        Prize prize = prizeRepository.findById(prizeId).orElse(null);

        if (prize != null) {
            // reject prize
            prize.setStatus(Prize.PrizeStatus.REJECTED);
            prize.setAdminNote(adminNote == null || adminNote.isBlank() ? "Request rejected." : adminNote);
            prizeRepository.save(prize);

            // notify student
            Notification studentNotification = new Notification();
            studentNotification.setUser(prize.getStudent());
            studentNotification.setMessage("Your prize request was rejected: " + prize.getPrizeName());
            notificationRepository.save(studentNotification);
        }

        ra.addFlashAttribute("errorMessage", "Prize request rejected.");
        return "redirect:/admin/prizes";
    }

    // prize options
    private List<PrizeTier> getPrizeTiers() {
        return List.of(
                new PrizeTier("Movie Ticket", "Free movie ticket at any cinema", 50),
                new PrizeTier("Gift Card $10", "$10 Amazon Gift Card", 100),
                new PrizeTier("Gift Card $25", "$25 Amazon Gift Card", 200),
                new PrizeTier("Gift Card $50", "$50 Amazon Gift Card", 400),
                new PrizeTier("Laptop Bag", "Premium laptop bag", 600),
                new PrizeTier("Scholarship", "$500 tuition scholarship credit", 1000)
        );
    }

    public record PrizeTier(String name, String description, int pointsRequired) {}
}