DROP TABLE IF EXISTS groups_lectures;
DROP TABLE IF EXISTS courses_teachers;
DROP TABLE IF EXISTS lectures;
DROP TABLE IF EXISTS vacations;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS durations;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS holidays;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS audiences;

CREATE TABLE audiences
(
    id          SERIAL PRIMARY KEY NOT NULL,
    room_number INT,
    capacity    INT
);
CREATE TABLE courses
(
    id   SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255)
);
CREATE TABLE holidays
(
    id   SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255),
    date DATE
);
CREATE TABLE groups
(
    id   SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255)
);
CREATE TABLE durations
(
    id         SERIAL PRIMARY KEY NOT NULL,
    start_time time,
    end_time   time
);
CREATE TABLE students
(
    id         SERIAL PRIMARY KEY NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255),
    country    VARCHAR(255),
    gender     VARCHAR(255),
    phone      VARCHAR(255),
    address    VARCHAR(255),
    birth_date DATE,
    group_id   INT,
    FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE teachers
(
    id                SERIAL PRIMARY KEY NOT NULL,
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
    id         SERIAL PRIMARY KEY NOT NULL,
    start_date DATE,
    end_date   DATE,
    teacher_id INT,
    FOREIGN KEY (teacher_id) REFERENCES teachers (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE lectures
(
    id          SERIAL PRIMARY KEY NOT NULL,
    date        DATE,
    course_id   INT,
    audience_id INT,
    duration_id INT,
    teacher_id  INT,
    FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (audience_id) REFERENCES audiences (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (duration_id) REFERENCES durations (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE courses_teachers
(
    course_id  INT,
    teacher_id INT,
    FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (course_id, teacher_id)
);
CREATE TABLE groups_lectures
(
    group_id   INT,
    lecture_id INT,
    FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (lecture_id) REFERENCES lectures (id) ON DELETE CASCADE ON UPDATE CASCADE
);
