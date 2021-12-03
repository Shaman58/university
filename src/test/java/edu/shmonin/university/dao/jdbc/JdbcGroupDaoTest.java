package edu.shmonin.university.dao.jdbc;

import config.TestConfig;
import edu.shmonin.university.model.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcGroupDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcGroupDao jdbcGroupDao;

    @Test
    void givenId_whenGet_thenReturnGroup() {
        var expected = Optional.of(new Group("group-1"));

        var actual = jdbcGroupDao.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllGroups() {
        var expected = new ArrayList<Group>();
        expected.add(new Group("group-1"));
        expected.add(new Group("group-2"));
        expected.add(new Group("group-3"));

        var actual = jdbcGroupDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenGroup_whenCreate_thenOneMoreRow() {
        var group = new Group("group-4");
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups") + 1;

        jdbcGroupDao.create(group);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

        assertEquals(expected, actual);
    }

    @Test
    void givenGroup_whenUpdate_thenUpdateRaw() {
        var group = new Group("group-4");
        group.setId(2);

        jdbcGroupDao.update(group);

        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", "name='group-4'");
        var expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups") - 1;

        jdbcGroupDao.delete(1);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

        assertEquals(expected, actual);
    }

    @Test
    void givenLectureId_whenGetByLectureId_thenReturnGroups() {
        var group1 = new Group("group-1");
        group1.setId(1);
        var group2 = new Group("group-2");
        group1.setId(2);
        var expected = List.of(group1, group2);

        var actual = jdbcGroupDao.getByLectureId(1);

        assertEquals(expected, actual);
    }
}