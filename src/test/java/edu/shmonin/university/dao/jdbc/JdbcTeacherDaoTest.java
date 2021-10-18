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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcTeacherDaoTest {

    @Autowired
    private JdbcTeacherDao jdbcTeacherDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId_whenGet_thenReturnTeacher() {
        var expected = new Teacher();
        expected.setFirstName("name-1");
        expected.setLastName("surname-1");
        expected.setEmail("email-1");
        expected.setCountry("country-1");
        expected.setGender(Gender.MALE);
        expected.setPhone("phone-1");
        expected.setAddress("address-1");
        expected.setBirthDate(LocalDate.of(1980, 1, 1));
        expected.setScientificDegree(ScientificDegree.DOCTOR);
        expected.setCourses(List.of(new Course("course-1"), new Course("course-2")));
        expected.setVacations(List.of(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1))));

        var actual = jdbcTeacherDao.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllTeachers() {
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
        var teacher2 = new Teacher();
        teacher2.setFirstName("name-2");
        teacher2.setLastName("surname-2");
        teacher2.setEmail("email-2");
        teacher2.setCountry("country-2");
        teacher2.setGender(Gender.MALE);
        teacher2.setPhone("phone-2");
        teacher2.setAddress("address-2");
        teacher2.setBirthDate(LocalDate.of(1980, 1, 2));
        teacher2.setScientificDegree(ScientificDegree.DOCTOR);
        teacher2.setCourses(List.of(new Course("course-1"), new Course("course-3")));
        var teacher3 = new Teacher();
        teacher3.setFirstName("name-3");
        teacher3.setLastName("surname-3");
        teacher3.setEmail("email-3");
        teacher3.setCountry("country-3");
        teacher3.setGender(Gender.FEMALE);
        teacher3.setPhone("phone-3");
        teacher3.setAddress("address-3");
        teacher3.setBirthDate(LocalDate.of(1980, 1, 3));
        teacher3.setScientificDegree(ScientificDegree.BACHELOR);
        teacher3.setCourses(Collections.singletonList(new Course("course-3")));
        teacher1.setVacations(Collections.singletonList(new Vacation(LocalDate.of(2021, 5, 1), LocalDate.of(2021, 6, 1))));
        var expected = List.of(teacher1, teacher2, teacher3);

        var actual = jdbcTeacherDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacher_whenCreate_thenOneMoreRow() {
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers") + 1;
        var teacher = new Teacher();
        teacher.setFirstName("name-4");
        teacher.setLastName("surname-4");
        teacher.setEmail("email-4");
        teacher.setCountry("country-4");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-4");
        teacher.setAddress("address-4");
        teacher.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);
        var course1 = new Course("course-1");
        var course2 = new Course("course-2");
        course1.setId(1);
        course2.setId(2);
        teacher.setCourses(List.of(course1, course2));

        jdbcTeacherDao.create(teacher);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacher_whenUpdate_thenUpdateRaw() {
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setFirstName("name-4");
        teacher.setLastName("surname-4");
        teacher.setEmail("email-4");
        teacher.setCountry("country-4");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-4");
        teacher.setAddress("address-4");
        teacher.setBirthDate(LocalDate.of(1980, 1, 4));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);
        teacher.setCourses(new ArrayList<>());

        jdbcTeacherDao.update(teacher);

        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "teachers", "first_name='name-4' and last_name='surname-4' and email='email-4' and country='country-4' and gender='MALE' and phone='phone-4' and address='address-4' and birth_date='1980-01-04' and scientific_degree='DOCTOR'");
        var expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers") - 1;

        jdbcTeacherDao.delete(1);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");

        assertEquals(expected, actual);
    }
}