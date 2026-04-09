INSERT INTO users (name, email, password, role, total_points)
VALUES ('Admin', 'admin@test.com', 'pass123', 'ADMIN', 0);

-- CREATE TABLE STUDENT_SE (
--                          id INT PRIMARY KEY,
--                          name VARCHAR(255),
--                          email VARCHAR(255),
--                          course VARCHAR(255),
--                          dob VARCHAR(255)
-- );

-- CREATE TABLE SESSION_PLAN (
--                               id INT PRIMARY KEY,
--                               name VARCHAR(255),
--                               price DECIMAL(10,2),
--                               duration_minutes INT
-- );
--
-- CREATE TABLE SESSION (
--                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
--                          session_time TIMESTAMP NOT NULL,
--                          status VARCHAR(20) NOT NULL,      -- PENDING, CONFIRMED, CANCELLED
--                          student_id BIGINT NOT NULL,
--                          subject VARCHAR(20) NOT NULL,
--                          instructor VARCHAR(20) NOT NULL,
-- --                          plan_id BIGINT NOT NULL,
--                          CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES STUDENT(id)
-- --                          CONSTRAINT fk_plan FOREIGN KEY (plan_id) REFERENCES SESSION_PLAN(id)
-- );
INSERT INTO STUDENT_SE (dtype, id, name, email, course,dob)
VALUES ('Student',1, 'Wendy', 'wendy@example.com', 'BSc Computer Science','2026-03-18T15:30');
INSERT INTO SESSION_PLAN (id, name, price, duration_minutes)
VALUES (1, 'Basic', 0, 30),
       (2, 'Standard', 20, 60),
       (3, 'Premium', 30, 90);


INSERT INTO SESSION (session_time, status, student_id, subject,instructor)
VALUES
    ('2026-03-18 15:30:00', 'NEW', 1, 'Java','Jane'),
    ('2026-03-19 10:00:00', 'NEW', 1, 'C#','Shane'),
    ('2026-03-20 11:00:00', 'NEW', 1, 'Data Structures','George');