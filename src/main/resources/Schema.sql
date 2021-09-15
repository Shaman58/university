CREATE TABLE audience
(
    id          SERIAL PRIMARY KEY NOT NULL,
    room_number INT,
    capacity    INT
);
CREATE TABLE groups
(
    id   SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255)
);
CREATE TABLE courses
(
    id   SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255)
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
CREATE TABLE holidays
(
    id   SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255),
    date DATE
);
CREATE TABLE durations
(
    id         SERIAL PRIMARY KEY NOT NULL,
    start_time time,
    end_time   time
);
CREATE TABLE teachers
(
    teacher_id                SERIAL PRIMARY KEY NOT NULL,
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
CREATE TABLE courses_teachers
(
    course_id  INT,
    teacher_id INT,
    FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (course_id, teacher_id)
);
CREATE TABLE vacations
(
    id         SERIAL PRIMARY KEY NOT NULL,
    start_date DATE,
    end_date   DATE,
    teacher_id INT,
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) ON DELETE CASCADE ON UPDATE CASCADE
);