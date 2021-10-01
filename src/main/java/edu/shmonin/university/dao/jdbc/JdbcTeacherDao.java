package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.dao.rowmapper.TeacherRowMapper;
import edu.shmonin.university.model.Course;
import edu.shmonin.university.model.Teacher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcTeacherDao implements TeacherDao {

    private static final String GET_QUERY = "SELECT * FROM teachers WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM teachers";
    private static final String CREATE_QUERY = "INSERT INTO teachers(first_name, last_name, email, country, gender, phone, address, birth_date, scientific_degree) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE teachers SET first_name=?,last_name=?,email=?,country=?,gender=?,phone=?,address=?,birth_date=?, scientific_degree=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM teachers WHERE id=?";
    private static final String CREATE_TEACHER_COURSE_QUERY = "INSERT INTO courses_teachers(course_id, teacher_id) VALUES (?,?)";

    private final JdbcTemplate jdbcTemplate;
    private final TeacherRowMapper teacherRowMapper;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherRowMapper teacherRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherRowMapper = teacherRowMapper;
    }

    @Override
    public Teacher get(int id) {
        return jdbcTemplate.queryForObject(GET_QUERY, teacherRowMapper, id);
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, teacherRowMapper);
    }

    @Override
    public void create(Teacher teacher) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, teacher.getFirstName());
            preparedStatement.setString(2, teacher.getLastName());
            preparedStatement.setString(3, teacher.getEmail());
            preparedStatement.setString(4, teacher.getCountry());
            preparedStatement.setString(5, teacher.getGender().toString());
            preparedStatement.setString(6, teacher.getPhone());
            preparedStatement.setString(7, teacher.getAddress());
            preparedStatement.setObject(8, teacher.getBirthDate());
            preparedStatement.setString(9, teacher.getScientificDegree().toString());
            return preparedStatement;
        }, keyHolder);
        teacher.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void update(Teacher teacher) {
        jdbcTemplate.update(UPDATE_QUERY, teacher.getFirstName(), teacher.getLastName(), teacher.getEmail(),
                teacher.getCountry(), teacher.getGender().toString(), teacher.getPhone(), teacher.getAddress(),
                teacher.getBirthDate(), teacher.getScientificDegree().toString(), teacher.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public void addTeacherCourse(Course course, Teacher teacher) {
        jdbcTemplate.update(CREATE_TEACHER_COURSE_QUERY, course.getId(), teacher.getId());
    }
}