package edu.shmonin.university.dao;

import edu.shmonin.university.model.Audience;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(SpringTestConfig.class)
@Sql({"classpath:Schema.sql", "classpath:test-data.sql"})
class AudienceDaoTest {

    @Autowired
    private AudienceDao audienceDao;

    @Test
    void givenId_whenGet_thenReturnAudience() {
        var expected = new Audience(1, 10);
        var actual = audienceDao.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllAudiences() {
        var expected = new ArrayList<Audience>();
        expected.add(new Audience(1, 10));
        expected.add(new Audience(2, 20));
        expected.add(new Audience(3, 30));
        var actual = audienceDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        audienceDao.delete(1);
        var actual = audienceDao.getAll().size();
        var expected = 2;

        assertEquals(expected, actual);
    }

    @Test
    void givenAudience_whenUpdate_thenUpdateRaw() {
        var expected = new Audience(1, 11);
        expected.setAudienceId(1);
        audienceDao.update(expected);
        var actual = audienceDao.get(1);

        assertEquals(expected, actual);
    }
}