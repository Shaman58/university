package edu.shmonin.university.dao;

import edu.shmonin.university.model.Audience;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = SpringTestConfig.class)
@Sql({"classpath:Schema.sql", "classpath:test-data.sql"})
class AudienceDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

//    @BeforeEach
//    void beforeAll(){
//        var d= jdbcTemplate.getDataSource();
//                d.ad
//    }

    @Test
    void get() {
        var audienceDao = new AudienceDao();
        audienceDao.setJdbcTemplate(jdbcTemplate);
        var expected = new Audience(1,10);
        var actual = audienceDao.get(1);

        assertEquals(expected,actual);
    }
}