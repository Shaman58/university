package edu.shmonin.university.dao.jdbc;

import config.TestConfig;
import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcStudentDaoTest {

    @Autowired
    JdbcStudentDao jdbcStudentDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void givenId_whenGet_thenReturnStudent() {
        var student = new Student();
        student.setFirstName("name-1");
        student.setLastName("surname-1");
        student.setEmail("email-1");
        student.setCountry("country-1");
        student.setGender(Gender.MALE);
        student.setPhone("phone-1");
        student.setAddress("address-1");
        student.setBirthDate(LocalDate.of(2000, 1, 1));
        var expected = Optional.of(student);

        var actual = jdbcStudentDao.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllStudents() {
        var student1 = new Student();
        student1.setFirstName("name-1");
        student1.setLastName("surname-1");
        student1.setEmail("email-1");
        student1.setCountry("country-1");
        student1.setGender(Gender.MALE);
        student1.setPhone("phone-1");
        student1.setAddress("address-1");
        student1.setBirthDate(LocalDate.of(2000, 1, 1));
        var student2 = new Student();
        student2.setFirstName("name-2");
        student2.setLastName("surname-2");
        student2.setEmail("email-2");
        student2.setCountry("country-2");
        student2.setGender(Gender.MALE);
        student2.setPhone("phone-2");
        student2.setAddress("address-2");
        student2.setBirthDate(LocalDate.of(2000, 1, 2));
        var student3 = new Student();
        student3.setFirstName("name-3");
        student3.setLastName("surname-3");
        student3.setEmail("email-3");
        student3.setCountry("country-3");
        student3.setGender(Gender.FEMALE);
        student3.setPhone("phone-3");
        student3.setAddress("address-3");
        student3.setBirthDate(LocalDate.of(2000, 1, 3));
        var expected = List.of(student1, student2, student3);

        var actual = jdbcStudentDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenPageRequest_whenGetAll_thenReturnPageOfStudents() {
        var pageRequest = PageRequest.of(0, 20);
        var student1 = new Student();
        student1.setFirstName("name-1");
        student1.setLastName("surname-1");
        student1.setEmail("email-1");
        student1.setCountry("country-1");
        student1.setGender(Gender.MALE);
        student1.setPhone("phone-1");
        student1.setAddress("address-1");
        student1.setBirthDate(LocalDate.of(2000, 1, 1));
        var student2 = new Student();
        student2.setFirstName("name-2");
        student2.setLastName("surname-2");
        student2.setEmail("email-2");
        student2.setCountry("country-2");
        student2.setGender(Gender.MALE);
        student2.setPhone("phone-2");
        student2.setAddress("address-2");
        student2.setBirthDate(LocalDate.of(2000, 1, 2));
        var student3 = new Student();
        student3.setFirstName("name-3");
        student3.setLastName("surname-3");
        student3.setEmail("email-3");
        student3.setCountry("country-3");
        student3.setGender(Gender.FEMALE);
        student3.setPhone("phone-3");
        student3.setAddress("address-3");
        student3.setBirthDate(LocalDate.of(2000, 1, 3));
        var students = List.of(student1, student2, student3);
        var expected = new PageImpl<>(students, pageRequest, 1);

        var actual = jdbcStudentDao.getAll(pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void givenStudent_whenCreate_thenOneMoreRow() {
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students") + 1;
        var student = new Student();
        student.setFirstName("name-1");
        student.setLastName("surname-1");
        student.setEmail("email-1");
        student.setCountry("country-1");
        student.setGender(Gender.MALE);
        student.setPhone("phone-1");
        student.setAddress("address-1");
        student.setBirthDate(LocalDate.of(1980, 1, 1));

        jdbcStudentDao.create(student);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");

        assertEquals(expected, actual);
    }

    @Test
    void givenStudent_whenUpdate_thenUpdateRaw() {
        var student = new Student();
        student.setId(1);
        student.setFirstName("name-4");
        student.setLastName("surname-4");
        student.setEmail("email-4");
        student.setCountry("country-4");
        student.setGender(Gender.MALE);
        student.setPhone("phone-4");
        student.setAddress("address-4");
        student.setBirthDate(LocalDate.of(2000, 1, 4));

        jdbcStudentDao.update(student);

        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,
                "students", "first_name='name-4' and last_name='surname-4' and email='email-4' and country='country-4' and gender='MALE' and phone='phone-4' and address='address-4' and birth_date='2000-01-04'");
        var expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students") - 1;

        jdbcStudentDao.delete(1);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");

        assertEquals(expected, actual);
    }
}