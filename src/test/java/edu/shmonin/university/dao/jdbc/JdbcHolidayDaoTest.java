package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.config.TestConfig;
import edu.shmonin.university.model.Holiday;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcHolidayDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcHolidayDao jdbcHolidayDao;

    @Test
    void givenId_whenGet_thenReturnHoliday() {
        var expected = Optional.of(new Holiday("holiday-1", LocalDate.of(2021, 1, 1)));

        var actual = jdbcHolidayDao.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllHolidays() {
        var expected = new ArrayList<Holiday>();
        expected.add(new Holiday("holiday-1", LocalDate.of(2021, 1, 1)));
        expected.add(new Holiday("holiday-2", LocalDate.of(2021, 1, 2)));
        expected.add(new Holiday("holiday-3", LocalDate.of(2021, 1, 3)));

        var actual = jdbcHolidayDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenPageRequest_whenGetAll_thenReturnPageOfHolidays() {
        var pageRequest = PageRequest.of(0, 20);
        var holidays = new ArrayList<Holiday>();
        holidays.add(new Holiday("holiday-1", LocalDate.of(2021, 1, 1)));
        holidays.add(new Holiday("holiday-2", LocalDate.of(2021, 1, 2)));
        holidays.add(new Holiday("holiday-3", LocalDate.of(2021, 1, 3)));
        var expected = new PageImpl<>(holidays, pageRequest, 1);

        var actual = jdbcHolidayDao.getAll(pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void givenHoliday_whenCreate_thenOneMoreRow() {
        var holiday = new Holiday("holiday-4", LocalDate.of(2021, 1, 4));
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "holidays") + 1;

        jdbcHolidayDao.create(holiday);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "holidays");

        assertEquals(expected, actual);
    }

    @Test
    void givenHoliday_whenUpdate_thenUpdateRaw() {
        var holiday = new Holiday("holiday-4", LocalDate.of(2021, 1, 4));
        holiday.setId(1);

        jdbcHolidayDao.update(holiday);

        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "holidays", "name='holiday-4' and date='2021-01-04'");
        var expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "holidays") - 1;

        jdbcHolidayDao.delete(1);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "holidays");

        assertEquals(expected, actual);
    }

    @Test
    void givenDate_whenGetByDate_thenReturnHolidayByDate() {
        var date = LocalDate.of(2021, 1, 1);
        var expected = Optional.of(new Holiday("holiday-1", date));

        var actual = jdbcHolidayDao.getByDate(date);

        assertEquals(expected, actual);
    }
}