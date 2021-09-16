CREATE TABLE audiences
(
    audience_id SERIAL PRIMARY KEY NOT NULL,
    room_number INT,
    capacity    INT
);
CREATE TABLE courses
(
    course_id SERIAL PRIMARY KEY NOT NULL,
    name      VARCHAR(255)
);
CREATE TABLE holidays
(
    holiday_id SERIAL PRIMARY KEY NOT NULL,
    name       VARCHAR(255),
    date       DATE
);
CREATE TABLE groups
(
    group_id SERIAL PRIMARY KEY NOT NULL,
    name     VARCHAR(255)
);
CREATE TABLE durations
(
    duration_id SERIAL PRIMARY KEY NOT NULL,
    start_time  time,
    end_time    time
);
CREATE TABLE students
(
    student_id SERIAL PRIMARY KEY NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255),
    country    VARCHAR(255),
    gender     VARCHAR(255),
    phone      VARCHAR(255),
    address    VARCHAR(255),
    birth_date DATE,
    group_id   INT,
    FOREIGN KEY (group_id) REFERENCES groups (group_id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE teachers
(
    teacher_id        SERIAL PRIMARY KEY NOT NULL,
    first_name        VARCHAR(255),
    last_name         VARCHAR(255),
    email             VARCHAR(255),
    country           VARCHAR(255),
    gender            VARCHAR(255),
    phone             VARCHAR(255),
    address           VARCHAR(255),
    birth_date        DATE,
    scientific_degree VARCHAR(255)
);
CREATE TABLE vacations
(
    vacation_id SERIAL PRIMARY KEY NOT NULL,
    start_date  DATE,
    end_date    DATE,
    teacher_id  INT,
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE lectures
(
    lecture_id  SERIAL PRIMARY KEY NOT NULL,
    date        DATE,
    course_id   INT,
    audience_id INT,
    duration_id INT,
    teacher_id  INT,
    FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (audience_id) REFERENCES audiences (audience_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (duration_id) REFERENCES durations (duration_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE courses_teachers
(
    course_id  INT,
    teacher_id INT,
    FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (course_id, teacher_id)
);
CREATE TABLE groups_lectures
(
    group_id   INT,
    lecture_id INT,
    FOREIGN KEY (group_id) REFERENCES groups (group_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (lecture_id) REFERENCES lectures (lecture_id) ON DELETE CASCADE ON UPDATE CASCADE
);
