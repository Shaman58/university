package edu.shmonin.university.dao.jdbc;

import config.TestConfig;
import edu.shmonin.university.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcLectureDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcLectureDao jdbcLectureDao;

    @Test
    void givenId_whenGet_thenReturnLecture() {
        var teacher1 = new Teacher();
        teacher1.setFirstName("name-1");
        teacher1.setLastName("surname-1");
        teacher1.setEmail("email-1");
        teacher1.setCountry("country-1");
        teacher1.setGender(Gender.MALE);
        teacher1.setPhone("phone-1");
        teacher1.setAddress("address-1");
        teacher1.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher1.setScientificDegree(ScientificDegree.DOCTOR);
        teacher1.setCourses(List.of(new Course("course-1"), new Course("course-2")));
        teacher1.setVacations(List.of(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1))));
        var expected = Optional.of(new Lecture(LocalDate.of(2021, 1, 1),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1));

        var actual = jdbcLectureDao.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllLectures() {
        var teacher1 = new Teacher();
        teacher1.setFirstName("name-1");
        teacher1.setLastName("surname-1");
        teacher1.setEmail("email-1");
        teacher1.setCountry("country-1");
        teacher1.setGender(Gender.MALE);
        teacher1.setPhone("phone-1");
        teacher1.setAddress("address-1");
        teacher1.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher1.setScientificDegree(ScientificDegree.DOCTOR);
        teacher1.setCourses(List.of(new Course("course-1"), new Course("course-2")));
        teacher1.setVacations(List.of(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1))));
        var lecture1 = new Lecture(LocalDate.of(2021, 1, 1),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var lecture2 = new Lecture(LocalDate.of(2021, 1, 2),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2"), new Group("group-3")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var lecture3 = new Lecture(LocalDate.of(2021, 1, 1),
                new Course("course-1"),
                List.of(new Group("group-3")),
                new Audience(2, 20),
                new Duration(LocalTime.of(11, 0, 0), LocalTime.of(12, 0, 0)),
                teacher1);
        var expected = List.of(lecture1, lecture2, lecture3);

        var actual = jdbcLectureDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenLecture_whenCreate_thenOneMoreRow() {
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures") + 1;
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setFirstName("name-1");
        teacher.setLastName("surname-1");
        teacher.setEmail("email-1");
        teacher.setCountry("country-1");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-1");
        teacher.setAddress("address-1");
        teacher.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);
        var course1 = new Course("course-1");
        course1.setId(1);
        var course2 = new Course("course-2");
        course2.setId(2);
        teacher.setCourses(List.of(course1, course2));
        var vacation1 = new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1));
        vacation1.setId(1);
        var vacation2 = new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1));
        vacation2.setId(2);
        teacher.setVacations(List.of(vacation1, vacation2));
        var group1 = new Group("group-1");
        group1.setId(1);
        var group2 = new Group("group-2");
        group2.setId(2);
        var audience = new Audience(1, 10);
        audience.setId(1);
        var duration = new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0));
        duration.setId(1);
        var lecture = new Lecture(LocalDate.of(2021, 1, 1),
                course1, List.of(group1, group2), audience, duration, teacher);

        jdbcLectureDao.create(lecture);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");

        assertEquals(expected, actual);
    }

    @Test
    void givenLecture_whenUpdate_thenUpdateRaw() {
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setFirstName("name-1");
        teacher.setLastName("surname-1");
        teacher.setEmail("email-1");
        teacher.setCountry("country-1");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-1");
        teacher.setAddress("address-1");
        teacher.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);
        var course = new Course("course-1");
        course.setId(1);
        teacher.setCourses(Collections.singletonList(course));
        var vacation1 = new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1));
        vacation1.setId(1);
        var vacation2 = new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1));
        vacation2.setId(2);
        teacher.setVacations(List.of(vacation1, vacation2));
        var group1 = new Group("group-1");
        group1.setId(1);
        var group2 = new Group("group-2");
        group2.setId(2);
        var audience = new Audience(1, 10);
        audience.setId(1);
        var duration = new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0));
        duration.setId(1);
        var lecture = new Lecture(LocalDate.of(2021, 1, 1),
                course, List.of(group1, group2), audience, duration, teacher);
        lecture.setId(1);

        jdbcLectureDao.update(lecture);

        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lectures", "date='2021-01-01' and course_id=1 and audience_id=1 and duration_id=1 and teacher_id=1");
        var expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures") - 1;

        jdbcLectureDao.delete(1);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lectures");

        assertEquals(expected, actual);
    }

    @Test
    void givenAudienceId_whenGetByAudienceId_thenReturnLecturesWithCurrentAudience() {
        var teacher1 = new Teacher();
        teacher1.setFirstName("name-1");
        teacher1.setLastName("surname-1");
        teacher1.setEmail("email-1");
        teacher1.setCountry("country-1");
        teacher1.setGender(Gender.MALE);
        teacher1.setPhone("phone-1");
        teacher1.setAddress("address-1");
        teacher1.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher1.setScientificDegree(ScientificDegree.DOCTOR);
        teacher1.setCourses(List.of(new Course("course-1"), new Course("course-2")));
        teacher1.setVacations(List.of(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1))));
        var lecture1 = new Lecture(LocalDate.of(2021, 1, 1),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var lecture2 = new Lecture(LocalDate.of(2021, 1, 2),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2"), new Group("group-3")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var expected = List.of(lecture1, lecture2);

        var actual = jdbcLectureDao.getByAudienceId(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenCourseId_whenGetByCourseId_thenReturnLecturesWithCurrentCourse() {
        var teacher1 = new Teacher();
        teacher1.setFirstName("name-1");
        teacher1.setLastName("surname-1");
        teacher1.setEmail("email-1");
        teacher1.setCountry("country-1");
        teacher1.setGender(Gender.MALE);
        teacher1.setPhone("phone-1");
        teacher1.setAddress("address-1");
        teacher1.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher1.setScientificDegree(ScientificDegree.DOCTOR);
        teacher1.setCourses(List.of(new Course("course-1"), new Course("course-2")));
        teacher1.setVacations(List.of(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1))));
        var lecture1 = new Lecture(LocalDate.of(2021, 1, 1),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var lecture2 = new Lecture(LocalDate.of(2021, 1, 2),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2"), new Group("group-3")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var lecture3 = new Lecture(LocalDate.of(2021, 1, 1),
                new Course("course-1"),
                List.of(new Group("group-3")),
                new Audience(2, 20),
                new Duration(LocalTime.of(11, 0, 0), LocalTime.of(12, 0, 0)),
                teacher1);
        var expected = List.of(lecture1, lecture2, lecture3);

        var actual = jdbcLectureDao.getByCourseId(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenDurationId_whenGetByGetByDurationId_thenReturnLecturesWithCurrentDuration() {
        var teacher1 = new Teacher();
        teacher1.setFirstName("name-1");
        teacher1.setLastName("surname-1");
        teacher1.setEmail("email-1");
        teacher1.setCountry("country-1");
        teacher1.setGender(Gender.MALE);
        teacher1.setPhone("phone-1");
        teacher1.setAddress("address-1");
        teacher1.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher1.setScientificDegree(ScientificDegree.DOCTOR);
        teacher1.setCourses(List.of(new Course("course-1"), new Course("course-2")));
        teacher1.setVacations(List.of(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1))));
        var lecture1 = new Lecture(LocalDate.of(2021, 1, 1),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var lecture2 = new Lecture(LocalDate.of(2021, 1, 2),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2"), new Group("group-3")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var expected = List.of(lecture1, lecture2);

        var actual = jdbcLectureDao.getByDurationId(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenGroupId_whenGetByGroupId_thenReturnLecturesWithCurrentGroup() {
        var teacher1 = new Teacher();
        teacher1.setFirstName("name-1");
        teacher1.setLastName("surname-1");
        teacher1.setEmail("email-1");
        teacher1.setCountry("country-1");
        teacher1.setGender(Gender.MALE);
        teacher1.setPhone("phone-1");
        teacher1.setAddress("address-1");
        teacher1.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher1.setScientificDegree(ScientificDegree.DOCTOR);
        teacher1.setCourses(List.of(new Course("course-1"), new Course("course-2")));
        teacher1.setVacations(List.of(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1))));
        var lecture1 = new Lecture(LocalDate.of(2021, 1, 1),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var lecture2 = new Lecture(LocalDate.of(2021, 1, 2),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2"), new Group("group-3")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var expected = List.of(lecture1, lecture2);

        var actual = jdbcLectureDao.getByGroupId(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherId_whenGetByTeacherId_thenReturnLecturesWithCurrentTeacher() {
        var teacher1 = new Teacher();
        teacher1.setFirstName("name-1");
        teacher1.setLastName("surname-1");
        teacher1.setEmail("email-1");
        teacher1.setCountry("country-1");
        teacher1.setGender(Gender.MALE);
        teacher1.setPhone("phone-1");
        teacher1.setAddress("address-1");
        teacher1.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher1.setScientificDegree(ScientificDegree.DOCTOR);
        teacher1.setCourses(List.of(new Course("course-1"), new Course("course-2")));
        teacher1.setVacations(List.of(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1))));
        var lecture1 = new Lecture(LocalDate.of(2021, 1, 1),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var lecture2 = new Lecture(LocalDate.of(2021, 1, 2),
                new Course("course-1"),
                List.of(new Group("group-1"), new Group("group-2"), new Group("group-3")),
                new Audience(1, 10),
                new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0)),
                teacher1);
        var lecture3 = new Lecture(LocalDate.of(2021, 1, 1),
                new Course("course-1"),
                List.of(new Group("group-3")),
                new Audience(2, 20),
                new Duration(LocalTime.of(11, 0, 0), LocalTime.of(12, 0, 0)),
                teacher1);
        var expected = List.of(lecture1, lecture2, lecture3);

        var actual = jdbcLectureDao.getByTeacherId(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenGroupIdAndDateAndDurationId_whenGetByGroupDateDuration_thenReturnOptionalLectureWithCurrentGroupDateDuration() {
        var teacher1 = new Teacher();
        teacher1.setFirstName("name-1");
        teacher1.setLastName("surname-1");
        teacher1.setEmail("email-1");
        teacher1.setCountry("country-1");
        teacher1.setGender(Gender.MALE);
        teacher1.setPhone("phone-1");
        teacher1.setAddress("address-1");
        teacher1.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher1.setScientificDegree(ScientificDegree.DOCTOR);
        teacher1.setCourses(List.of(new Course("course-1"), new Course("course-2")));
        teacher1.setVacations(List.of(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1))));
        var group = new Group("group-1");
        var date = LocalDate.of(2021, 1, 2);
        var duration = new Duration(LocalTime.of(9, 0, 0), LocalTime.of(10, 0, 0));
        var lecture2 = new Lecture(date, new Course("course-1"),
                List.of(group, new Group("group-2"), new Group("group-3")),
                new Audience(1, 10),
                duration,
                teacher1);
        var expected = Optional.of(lecture2);

        var actual = jdbcLectureDao.getByGroupDateDuration(1, date, 1);

        assertEquals(expected, actual);
    }
}