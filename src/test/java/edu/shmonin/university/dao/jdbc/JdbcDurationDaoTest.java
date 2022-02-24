package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.config.TestConfig;
import edu.shmonin.university.model.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcDurationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcDurationDao jdbcDurationDao;

    @Test
    void givenId_whenGet_thenReturnDuration() {
        var expected = Optional.of(new Duration(LocalTime.of(9, 0), LocalTime.of(10, 0)));

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
    void givenPageRequest_whenGetAll_thenReturnAllDurations_thenReturnPageOfDurations() {
        var pageRequest = PageRequest.of(0, 20);
        var durations = new ArrayList<Duration>();
        durations.add(new Duration(LocalTime.of(9, 0), LocalTime.of(10, 0)));
        durations.add(new Duration(LocalTime.of(11, 0), LocalTime.of(12, 0)));
        durations.add(new Duration(LocalTime.of(13, 0), LocalTime.of(14, 0)));
        var expected = new PageImpl<>(durations, pageRequest, 1);

        var actual = jdbcDurationDao.getAll(pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void givenDuration_whenCreate_thenOneMoreRow() {
        var duration = new Duration(LocalTime.of(15, 0), LocalTime.of(16, 0));
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "durations") + 1;

        jdbcDurationDao.create(duration);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "durations");

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
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "durations") - 1;

        jdbcDurationDao.delete(1);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "durations");

        assertEquals(expected, actual);
    }
}