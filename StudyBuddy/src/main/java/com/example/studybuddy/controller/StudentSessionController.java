package com.example.studybuddy.controller;

import com.example.studybuddy.entity.Student;
import com.example.studybuddy.model.Payment;
import com.example.studybuddy.model.Session;
import com.example.studybuddy.model.SessionPlan;
import com.example.studybuddy.model.Student_SE;
import com.example.studybuddy.repository.PaymentRepository;
import com.example.studybuddy.repository.SessionPlanRepository;
import com.example.studybuddy.repository.SessionRepository;
import com.example.studybuddy.repository.StudentRepository;
import com.example.studybuddy.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/student/sessions")
@SessionAttributes("selectedPlan")
public class StudentSessionController {

    @Autowired
    private SessionPlanRepository planRepo;

    @Autowired
    private SessionRepository sessionRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private SessionService sessionService;


    @GetMapping

    public String viewSessions(Model model){

        List<Student> students = studentRepo.findAll();
        if (students.isEmpty()){
            model.addAttribute("error", "No students found");
            return "student-sessions";
        }
        Student student = students.get(0);
        List<SessionPlan> plans = planRepo.findAll();
        Session currentSession = sessionRepo.findByStudentId(student.getId())
                .stream()
                .reduce((first, second) -> second)
                .orElse(new Session());

        List<Session> allSessions = sessionRepo.findByStudentId(student.getId());

        model.addAttribute("student", student);
        model.addAttribute("plans", plans);
        model.addAttribute("currentSession", currentSession);
        model.addAttribute("allSessions", allSessions);
        return "student-sessions";
    }


    @PostMapping("/book")
    public String bookSession(@RequestParam Long sessionId) {
        Session session = sessionRepo.findById(sessionId).orElse(null);
        if (session != null) {
            session.setStatus("PENDING");
            sessionRepo.save(session);
        }
        return "redirect:/student/sessions";
    }

    @PostMapping("/cancel/{id}")
    public String cancelSession(@PathVariable Long id) {
        Session session = sessionRepo.findById(id).orElse(null);
        if (session != null) {

            if ("PENDING".equals(session.getStatus())) {
                session.setStatus("NEW");
            } else {
                session.setStatus("CANCELLED");
            }
            sessionRepo.save(session);
        }
        return "redirect:/student/sessions";
    }


    @PostMapping("/selectPlan")
    public String selectPlan(@RequestParam Long planId, Model model){

        SessionPlan selectedPlan = planRepo.findById(planId).orElse(null);

        if(selectedPlan != null){
            model.addAttribute("selectedPlan", selectedPlan);
        }

        return "redirect:/student/sessions";
    }


    @GetMapping("/payment")
    public String paymentPage(Model model) {

        List<Student> students = studentRepo.findAll();
        if (students.isEmpty()){
            model.addAttribute("error", "No students found");
            return "student-sessions";
        }
        Student student = students.get(0);
        model.addAttribute("error", student);
        return "student-payment";
    }


    @Autowired
    private PaymentRepository paymentRepo;

    @PostMapping("/processPayment")
    public String processPayment(@RequestParam Long planId,
                                 RedirectAttributes redirectAttributes) {

        Student_SE student = studentRepo.findAll().get(0);
        SessionPlan plan = planRepo.findById(planId).orElse(null);

        if (plan != null) {

            Payment payment = new Payment();
            payment.setStudent(student);
            payment.setPlan(plan);
            payment.setAmount(plan.getPrice());
            payment.setPaymentDate(LocalDateTime.now());

            paymentRepo.save(payment);

            redirectAttributes.addFlashAttribute("receiptPayment", payment);

            return "redirect:/student/sessions/receipt";
        }

        return "redirect:/student/sessions";
    }

    @GetMapping("/receipt")
    public String receiptPage(Model model) {
        return "receipt";
    }
}
