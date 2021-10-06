package edu.shmonin.university.dao.jdbc;

import config.TestConfig;
import edu.shmonin.university.model.Teacher;
import edu.shmonin.university.model.Vacation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@Sql({"classpath:Schema.sql", "classpath:test-data.sql"})
class JdbcVacationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcVacationDao jdbcVacationDao;

    @Test
    void givenId_whenGet_thenReturnVacation() {
        var expected = new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1));
        var actual = jdbcVacationDao.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllVacations() {
        var expected = new ArrayList<Vacation>();
        expected.add(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)));
        expected.add(new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1)));
        expected.add(new Vacation(LocalDate.of(2021, 5, 1), LocalDate.of(2021, 6, 1)));
        var actual = jdbcVacationDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenVacation_whenCreate_thenOneMoreRow() {
        var vacation = new Vacation(LocalDate.of(2021, 7, 1), LocalDate.of(2021, 8, 1));
        jdbcVacationDao.create(vacation);
        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "vacations");
        var expected = 4;

        assertEquals(expected, actual);
    }

    @Test
    void givenVacation_whenUpdate_thenUpdateRaw() {
        var vacation = new Vacation(LocalDate.of(2021, 7, 1), LocalDate.of(2021, 8, 1));
        vacation.setId(1);
        jdbcVacationDao.update(vacation);
        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "vacations", "start_date='2021-07-01' and end_date='2021-08-01'");
        var expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        jdbcVacationDao.delete(1);
        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "vacations");
        var expected = 2;

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherId_whenGetTeacherVacations_ThenReturnVacationsOfTheTeacher() {
        var actual = jdbcVacationDao.getTeacherVacations(1);
        var expected = new ArrayList<Vacation>();
        expected.add(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)));
        expected.add(new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1)));

        assertEquals(expected, actual);
    }

    @Test
    void givenVacationAndTeacher_whenSetTeacherVacation_thenUpdateRow() {
        var teacher = new Teacher();
        teacher.setId(1);
        var vacation = new Vacation();
        vacation.setId(3);
        jdbcVacationDao.setTeacherVacation(vacation, teacher);
        var expected = 3;
        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "vacations", "teacher_id=1");

        assertEquals(expected, actual);
    }
}