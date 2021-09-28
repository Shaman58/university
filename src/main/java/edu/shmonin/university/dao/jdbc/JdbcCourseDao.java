package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.CourseDao;
import edu.shmonin.university.model.Course;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcCourseDao implements CourseDao {

    private static final String GET_QUERY = "SELECT * FROM courses WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM courses";
    private static final String CREATE_QUERY = "INSERT INTO courses(name) VALUES(?) RETURNING id";
    private static final String UPDATE_QUERY = "UPDATE courses SET name=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM courses WHERE id=?";
    private static final String GET_TEACHER_COURSES_QUERY =
            "SELECT id, name FROM courses INNER JOIN courses_teachers ON courses.id = courses_teachers.course_id WHERE teacher_id=?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Course> courseRowMapper;

    public JdbcCourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.courseRowMapper = BeanPropertyRowMapper.newInstance(Course.class);
    }

    @Override
    public Course get(int id) {
        return jdbcTemplate.queryForObject(GET_QUERY, courseRowMapper, id);
    }

    @Override
    public List<Course> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, courseRowMapper);
    }

    @Override
    public Course create(Course course) {
        var id = jdbcTemplate.queryForObject(CREATE_QUERY, Integer.class, course.getName());
        course.setId(id);
        return course;
    }

    @Override
    public void update(Course course) {
        jdbcTemplate.update(UPDATE_QUERY, course.getName(), course.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public List<Course> getByTeacherId(int teacherId) {
        return jdbcTemplate.query(GET_TEACHER_COURSES_QUERY, courseRowMapper, teacherId);
    }
}