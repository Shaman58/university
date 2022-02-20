package edu.shmonin.university.dao.jdbc;

import config.TestConfig;
import edu.shmonin.university.model.Audience;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcAudienceDaoTest {

    @Autowired
    private JdbcAudienceDao jdbcAudienceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void givenId_whenGet_thenReturnAudience() {
        var expected = Optional.of(new Audience(1, 10));

        var actual = jdbcAudienceDao.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllAudiences() {
        var expected = new ArrayList<Audience>();
        expected.add(new Audience(1, 10));
        expected.add(new Audience(2, 20));
        expected.add(new Audience(3, 30));

        var actual = jdbcAudienceDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenPageRequest_whenGetAll_thenReturnPageOfAudiences() {
        var pageRequest = PageRequest.of(0, 20);
        var audiences = new ArrayList<Audience>();
        audiences.add(new Audience(1, 10));
        audiences.add(new Audience(2, 20));
        audiences.add(new Audience(3, 30));
        var expected = new PageImpl<>(audiences, pageRequest, 1);

        var actual = jdbcAudienceDao.getAll(pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void givenAudience_whenCreate_thenOneMoreRow() {
        var audience = new Audience(4, 40);
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences") + 1;

        jdbcAudienceDao.create(audience);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");

        assertEquals(expected, actual);
    }

    @Test
    void givenAudience_whenUpdate_thenUpdateRaw() {
        var audience = new Audience(1, 11);
        audience.setId(1);

        jdbcAudienceDao.update(audience);

        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "audiences", "room_number=1 and capacity=11");
        var expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences") - 1;

        jdbcAudienceDao.delete(1);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "audiences");

        assertEquals(expected, actual);
    }
}