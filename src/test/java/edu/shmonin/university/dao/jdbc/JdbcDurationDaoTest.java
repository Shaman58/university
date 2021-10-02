package edu.shmonin.university.dao.jdbc;

import config.TestConfig;
import edu.shmonin.university.model.Audience;
import edu.shmonin.university.model.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestConfig.class)
@Sql({"classpath:Schema.sql", "classpath:test-data.sql"})
class JdbcDurationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcDurationDao jdbcDurationDao;

    @Test
    void givenId_whenGet_thenReturnDuration() {
        var expected = new Duration(LocalTime.of(9, 0), LocalTime.of(10, 0));
        var actual = jdbcDurationDao.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllDurations() {
        var expected = new ArrayList<Duration>();
        expected.add(new Duration(LocalTime.of(9, 0), LocalTime.of(10, 0)));
        expected.add(new Duration(LocalTime.of(11, 0), LocalTime.of(12, 0)));
        expected.add(new Duration(LocalTime.of(13, 0), LocalTime.of(14, 0)));
        var actual = jdbcDurationDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenDuration_whenCreate_thenOneMoreRow() {
        var duration = new Duration(LocalTime.of(15, 0), LocalTime.of(16, 0));
        jdbcDurationDao.create(duration);
        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "durations");
        var expected = 4;

        assertEquals(expected, actual);
    }

    @Test
    void givenDuration_whenUpdate_thenUpdateRaw() {
        var duration = new Duration(LocalTime.of(15, 0), LocalTime.of(16, 0));
        duration.setId(1);
        jdbcDurationDao.update(duration);
        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "durations", "start_time='15:00:00' and end_time='16:00:00'");
        var expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        jdbcDurationDao.delete(1);
        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "durations");
        var expected = 2;

        assertEquals(expected, actual);
    }
}