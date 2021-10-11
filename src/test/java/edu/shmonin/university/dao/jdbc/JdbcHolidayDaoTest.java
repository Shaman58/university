package edu.shmonin.university.dao.jdbc;

import config.TestConfig;
import edu.shmonin.university.model.Holiday;
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
class JdbcHolidayDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcHolidayDao jdbcHolidayDao;

    @Test
    void givenId_whenGet_thenReturnHoliday() {
        var expected = new Holiday("holiday-1", LocalDate.of(2021, 1, 1));

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
}