package edu.shmonin.university.dao;

import edu.shmonin.university.model.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@Sql({"classpath:Schema.sql", "classpath:test-data.sql"})
class AudienceDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AudienceDao audienceDao;

    @Test
    void get() {
        audienceDao.setJdbcTemplate(jdbcTemplate);
        var expected = new Audience(1,10);
        var actual = audienceDao.get(1);

        assertEquals(expected,actual);
    }
}