package com.example.studybuddy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String professor;
    private String notes;

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;

    private String fileName;

    private LocalDateTime createdAt;


    public CheckIn() {
        this.createdAt = LocalDateTime.now();
    }

    public CheckIn(String subject, String professor, String notes, String fileName, byte[] fileData) {
        this.subject = subject;
        this.professor = professor;
        this.notes = notes;
        this.fileName = fileName;
        this.fileData = fileData;
        this.createdAt = LocalDateTime.now();
    }


    public Long getId() { return id; }
    public String getSubject() { return subject; }
    public String getProfessor() { return professor; }
    public String getNotes() { return notes; }
    public byte[] getFileData() { return fileData; }
    public String getFileName() { return fileName; }
    public LocalDateTime getCreatedAt() { return createdAt; }


    public void setId(Long id) { this.id = id; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setProfessor(String professor) { this.professor = professor; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}