package edu.shmonin.university.dao.jdbc;

import config.TestConfig;
import edu.shmonin.university.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@Sql({"classpath:Schema.sql", "classpath:test-data.sql"})
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
        expected.setCourses(Arrays.asList(new Course("course-1"), new Course("course-2")));
        expected.setVacations(Arrays.asList(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
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
        teacher1.setCourses(Arrays.asList(new Course("course-1"), new Course("course-2")));
        teacher1.setVacations(Arrays.asList(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
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
        teacher2.setCourses(Arrays.asList(new Course("course-1"), new Course("course-3")));
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
        var expected = Arrays.asList(teacher1, teacher2, teacher3);
        var actual = jdbcTeacherDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacher_whenCreate_thenOneMoreRow() {
        var teacher = new Teacher();
        teacher.setFirstName("name-1");
        teacher.setLastName("surname-1");
        teacher.setEmail("email-1");
        teacher.setCountry("country-1");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-1");
        teacher.setAddress("address-1");
        teacher.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);
        teacher.setCourses(Arrays.asList(new Course("course-1"), new Course("course-2")));
        teacher.setVacations(Arrays.asList(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1))));
        jdbcTeacherDao.create(teacher);
        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
        var expected = 4;

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
        jdbcTeacherDao.update(teacher);
        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "teachers", "first_name='name-4' and last_name='surname-4' and email='email-4' and country='country-4' and gender='MALE' and phone='phone-4' and address='address-4' and birth_date='1980-01-04' and scientific_degree='DOCTOR'");
        var expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        jdbcTeacherDao.delete(1);
        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers");
        var expected = 2;

        assertEquals(expected, actual);
    }
}