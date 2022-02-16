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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcVacationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcVacationDao jdbcVacationDao;

    @Test
    void givenId_whenGet_thenReturnVacation() {
        var expected = Optional.of(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)));

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
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "vacations") + 1;
        var vacation = new Vacation(LocalDate.of(2021, 7, 1), LocalDate.of(2021, 8, 1));

        jdbcVacationDao.create(vacation);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "vacations");

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
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "vacations") - 1;

        jdbcVacationDao.delete(1);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "vacations");

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherId_whenGetByTeacherId_ThenReturnVacationsOfTheTeacher() {
        var expected = new ArrayList<Vacation>();
        expected.add(new Vacation(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)));
        expected.add(new Vacation(LocalDate.of(2021, 3, 1), LocalDate.of(2021, 4, 1)));

        var actual = jdbcVacationDao.getByTeacherId(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherIdAndDate_whenGetByTeacherIdAndDate_ThenReturnVacationsOfTheTeacher() {
        var date = LocalDate.of(2021, 2, 1);
        var expected = Optional.of(new Vacation(LocalDate.of(2021, 1, 1), date));

        var actual = jdbcVacationDao.getByTeacherIdAndDate(1, date);

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherId_whenGetByTeacherIdAndDateBetween_ThenReturnVacationsOfTheTeacherBetweenDate() {
        var startDate = LocalDate.of(2021, 2, 20);
        var endDate = LocalDate.of(2021, 5, 10);
        var expected = List.of(new Vacation(LocalDate.of(2021, 3, 1),
                LocalDate.of(2021, 4, 1)));

        var actual = jdbcVacationDao.getByTeacherIdAndDateBetween(1, startDate, endDate);

        assertEquals(expected, actual);
    }
}