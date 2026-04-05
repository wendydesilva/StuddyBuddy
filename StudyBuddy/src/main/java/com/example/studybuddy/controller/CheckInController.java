package com.example.studybuddy.controller;

import com.example.studybuddy.model.CheckIn;
import com.example.studybuddy.repository.CheckInRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/checkins")
public class CheckInController {

    private final CheckInRepository checkInRepository;

    public CheckInController(CheckInRepository checkInRepository) {
        this.checkInRepository = checkInRepository;
    }

    @GetMapping
    public String viewCheckIns(Model model) {
        model.addAttribute("checkIns", checkInRepository.findAll());
        return "checkins";
    }


    @PostMapping
    public String addCheckIn(@RequestParam String subject,
                             @RequestParam String professor,
                             @RequestParam String notes,
                             @RequestParam("file") MultipartFile file) {

        byte[] fileData = null;
        String fileName = null;

        if (!file.isEmpty()) {
            try {
                fileData = file.getBytes();
                fileName = file.getOriginalFilename();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        CheckIn checkIn = new CheckIn(subject, professor, notes, fileName, fileData);
        checkInRepository.save(checkIn);

        return "redirect:/checkins";
    }


    @PostMapping("/delete/{id}")
    public String deleteCheckIn(@PathVariable Long id) {
        checkInRepository.deleteById(id);
        return "redirect:/checkins";
    }


    @GetMapping("/edit/{id}")
    public String editCheckIn(@PathVariable Long id, Model model) {
        CheckIn checkIn = checkInRepository.findById(id).orElse(null);
        if (checkIn == null) return "error";
        model.addAttribute("checkIn", checkIn);
        return "edit-checkin";
    }


    @PostMapping("/update/{id}")
    public String updateCheckIn(@PathVariable Long id,
                                @RequestParam String subject,
                                @RequestParam String professor,
                                @RequestParam String notes,
                                @RequestParam(value = "file", required = false) MultipartFile file) {

        CheckIn checkIn = checkInRepository.findById(id).orElse(null);
        if (checkIn == null) return "error";

        checkIn.setSubject(subject);
        checkIn.setProfessor(professor);
        checkIn.setNotes(notes);

        if (file != null && !file.isEmpty()) {
            try {
                checkIn.setFileData(file.getBytes());
                checkIn.setFileName(file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        checkInRepository.save(checkIn);
        return "redirect:/checkins";
    }


    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        CheckIn checkIn = checkInRepository.findById(id).orElse(null);
        if (checkIn == null || checkIn.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + checkIn.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(checkIn.getFileData());
    }
}