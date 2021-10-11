package edu.shmonin.university.dao.jdbc;

import config.TestConfig;
import edu.shmonin.university.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@Sql({"classpath:Schema.sql", "classpath:test-data.sql"})
class JdbcCourseDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcCourseDao jdbcCourseDao;

    @Test
    void givenId_whenGet_thenReturnCourse() {
        var expected = new Course("course-1");

        var actual = jdbcCourseDao.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllCourses() {
        var expected = new ArrayList<Course>();
        expected.add(new Course("course-1"));
        expected.add(new Course("course-2"));
        expected.add(new Course("course-3"));

        var actual = jdbcCourseDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenCourse_whenCreate_thenOneMoreRow() {
        var course = new Course("course-4");
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses") + 1;

        jdbcCourseDao.create(course);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses");

        assertEquals(expected, actual);
    }

    @Test
    void givenCourse_whenUpdate_thenUpdateRaw() {
        var course = new Course("course-11");
        course.setId(1);

        jdbcCourseDao.update(course);

        var actual = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses", "name='course-11'");
        var expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDelete_thenDeleteRaw() {
        var expected = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses") - 1;

        jdbcCourseDao.delete(1);

        var actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses");

        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenGetByTeacherId_thenReturnCoursesOfTheTeacher() {
        var expected = 2;

        var actual = jdbcCourseDao.getByTeacherId(1).size();

        assertEquals(expected, actual);
    }
}