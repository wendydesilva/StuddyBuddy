package com.example.studybuddy.repository;

import com.example.studybuddy.model.Prize;
import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DataJpaTest
class PrizeRepositoryTest {

    @Autowired
    private PrizeRepository prizeRepository; // test prize queries

    @Autowired
    private UserRepository userRepository; // test user data

    @Test
    void testTotalApprovedPointsUsed() {

        // create student
        User student = new User();
        student.setName("Test");
        student.setEmail("test@test.com");
        student.setPassword("123");
        student.setRole(Role.STUDENT);
        student.setTotalPoints(0);
        userRepository.save(student);

        // first approved prize
        Prize p1 = new Prize();
        p1.setPrizeName("Gift Card");
        p1.setDescription("Test");
        p1.setPointsRequired(50);
        p1.setStatus(Prize.PrizeStatus.APPROVED);
        p1.setStudent(student);
        prizeRepository.save(p1);

        // second approved prize
        Prize p2 = new Prize();
        p2.setPrizeName("Headphones");
        p2.setDescription("Test");
        p2.setPointsRequired(30);
        p2.setStatus(Prize.PrizeStatus.APPROVED);
        p2.setStudent(student);
        prizeRepository.save(p2);

        // calculate total points used
        Integer total = prizeRepository.getTotalApprovedPointsUsed(student);

        // verify sum
        assertEquals(80, total);
    }
}