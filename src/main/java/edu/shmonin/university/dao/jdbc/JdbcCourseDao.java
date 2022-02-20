package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.CourseDao;
import edu.shmonin.university.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcCourseDao implements CourseDao {

    private static final String GET_QUERY = "SELECT * FROM courses WHERE id=?";
    private static final String GET_COUNT_QUERY = " SELECT COUNT(*) FROM courses";
    private static final String GET_ALL_QUERY = "SELECT * FROM courses";
    private static final String CREATE_QUERY = "INSERT INTO courses(name) VALUES(?)";
    private static final String UPDATE_QUERY = "UPDATE courses SET name=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM courses WHERE id=?";
    private static final String GET_TEACHER_COURSES_QUERY =
            "SELECT id, name FROM courses INNER JOIN teacher_courses ON courses.id = teacher_courses.course_id WHERE teacher_id=?";
    private static final String GET_PAGE_QUERY = "SELECT * FROM courses order by name LIMIT ? OFFSET ?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Course> courseRowMapper;

    public JdbcCourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.courseRowMapper = BeanPropertyRowMapper.newInstance(Course.class);
    }

    @Override
    public Optional<Course> get(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_QUERY, courseRowMapper, id));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Course> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, courseRowMapper);
    }

    @Override
    public Page<Course> getAll(Pageable pageable) {
        int courseQuantity = jdbcTemplate.queryForObject(GET_COUNT_QUERY, Integer.class);
        var courses = jdbcTemplate.query(GET_PAGE_QUERY, courseRowMapper,
                pageable.getPageSize(), pageable.getOffset());
        return new PageImpl<>(courses, pageable, courseQuantity);
    }

    @Override
    public void create(Course course) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, course.getName());
            return preparedStatement;
        }, keyHolder);
        course.setId((Integer) Objects.requireNonNull(keyHolder.getKeys().get("id")));
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