INSERT INTO audiences(room_number, capacity)
VALUES (1, 10);
INSERT INTO audiences(room_number, capacity)
VALUES (2, 20);
INSERT INTO audiences(room_number, capacity)
VALUES (3, 30);
INSERT INTO courses(name)
VALUES ('course-1');
INSERT INTO courses(name)
VALUES ('course-2');
INSERT INTO courses(name)
VALUES ('course-3');
INSERT INTO durations(start_time, end_time)
VALUES ('9:00', '10:00');
INSERT INTO durations(start_time, end_time)
VALUES ('11:00', '12:00');
INSERT INTO durations(start_time, end_time)
VALUES ('13:00', '14:00');
INSERT INTO groups(name)
VALUES ('group-1');
INSERT INTO groups(name)
VALUES ('group-2');
INSERT INTO groups(name)
VALUES ('group-3');
INSERT INTO holidays(name, date)
VALUES ('holiday-1', '2021-01-01');
INSERT INTO holidays(name, date)
VALUES ('holiday-2', '2021-01-02');
INSERT INTO holidays(name, date)
VALUES ('holiday-3', '2021-01-03');
INSERT INTO teachers(first_name, last_name, email, country, gender, phone, address, birth_date, scientific_degree)
VALUES ('name-1', 'surname-1', 'email-1', 'country-1', 'MALE', 'phone-1', 'address-1', '1980-01-01', 'DOCTOR');
INSERT INTO teachers(first_name, last_name, email, country, gender, phone, address, birth_date, scientific_degree)
VALUES ('name-2', 'surname-2', 'email-2', 'country-2', 'MALE', 'phone-2', 'address-2', '1980-01-02', 'DOCTOR');
INSERT INTO teachers(first_name, last_name, email, country, gender, phone, address, birth_date, scientific_degree)
VALUES ('name-3', 'surname-3', 'email-3', 'country-3', 'FEMALE', 'phone-3', 'address-3', '1980-01-03', 'BACHELOR');
INSERT INTO vacations(start_date, end_date, teacher_id)
VALUES ('2021-01-01', '2021-02-01', 1);
INSERT INTO vacations(start_date, end_date, teacher_id)
VALUES ('2021-03-01', '2021-04-01', 1);
INSERT INTO vacations(start_date, end_date, teacher_id)
VALUES ('2021-05-01', '2021-06-01', 3);
INSERT INTO teacher_courses(course_id, teacher_id)
VALUES (1, 1);
INSERT INTO teacher_courses(course_id, teacher_id)
VALUES (2, 1);
INSERT INTO teacher_courses(course_id, teacher_id)
VALUES (1, 2);
INSERT INTO teacher_courses(course_id, teacher_id)
VALUES (3, 2);
INSERT INTO teacher_courses(course_id, teacher_id)
VALUES (3, 3);
INSERT INTO students(first_name, last_name, email, country, gender, phone, address, birth_date, group_id)
VALUES ('name-1', 'surname-1', 'email-1', 'country-1', 'MALE', 'phone-1', 'address-1', '2000-01-01', 1);
INSERT INTO students(first_name, last_name, email, country, gender, phone, address, birth_date, group_id)
VALUES ('name-2', 'surname-2', 'email-2', 'country-2', 'MALE', 'phone-2', 'address-2', '2000-01-02', 1);
INSERT INTO students(first_name, last_name, email, country, gender, phone, address, birth_date, group_id)
VALUES ('name-3', 'surname-3', 'email-3', 'country-3', 'FEMALE', 'phone-3', 'address-3', '2000-01-03', 2);
INSERT INTO lectures(date, course_id, audience_id, duration_id, teacher_id)
VALUES ('2021-01-01', 1, 1, 1, 1);
INSERT INTO lectures(date, course_id, audience_id, duration_id, teacher_id)
VALUES ('2021-01-02', 1, 1, 1, 1);
INSERT INTO lectures(date, course_id, audience_id, duration_id, teacher_id)
VALUES ('2021-01-03', 1, 2, 2, 1);
INSERT INTO lecture_groups(group_id, lecture_id)
VALUES (1, 1);
INSERT INTO lecture_groups(group_id, lecture_id)
VALUES (2, 1);
INSERT INTO lecture_groups(group_id, lecture_id)
VALUES (1, 2);
INSERT INTO lecture_groups(group_id, lecture_id)
VALUES (2, 2);
INSERT INTO lecture_groups(group_id, lecture_id)
VALUES (3, 2);
INSERT INTO lecture_groups(group_id, lecture_id)
VALUES (3, 3);