package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.dao.jdbc.rowmapper.TeacherRowMapper;
import edu.shmonin.university.model.Teacher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcTeacherDao implements TeacherDao {

    private static final String GET_QUERY = "SELECT * FROM teachers WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM teachers";
    private static final String CREATE_QUERY = "INSERT INTO teachers(first_name, last_name, email, country, gender, phone, address, birth_date, scientific_degree) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE teachers SET first_name=?,last_name=?,email=?,country=?,gender=?,phone=?,address=?,birth_date=?, scientific_degree=? WHERE id=?";
    private static final String DELETE_TEACHER_COURSE_QUERY = "DELETE FROM teacher_courses WHERE course_id=? AND teacher_id=?";
    private static final String DELETE_QUERY = "DELETE FROM teachers WHERE id=?";
    private static final String CREATE_TEACHER_COURSE_QUERY = "INSERT INTO teacher_courses(course_id, teacher_id) VALUES (?,?)";

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

    @Transactional
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
        if (teacher.getCourses() != null) {
            teacher.getCourses().forEach(course -> jdbcTemplate.update(CREATE_TEACHER_COURSE_QUERY, course.getId(), teacher.getId()));
        }
    }

    @Transactional
    @Override
    public void update(Teacher updatedTeacher) {
        var teacher = get(updatedTeacher.getId());
        var coursesToDelete = new java.util.ArrayList<>(List.copyOf(teacher.getCourses()));
        coursesToDelete.removeAll(updatedTeacher.getCourses());
        var coursesToAdd = new ArrayList<>(List.copyOf(updatedTeacher.getCourses()));
        coursesToAdd.removeAll(teacher.getCourses());
        jdbcTemplate.update(UPDATE_QUERY, updatedTeacher.getFirstName(), updatedTeacher.getLastName(), updatedTeacher.getEmail(),
                updatedTeacher.getCountry(), updatedTeacher.getGender().toString(), updatedTeacher.getPhone(), updatedTeacher.getAddress(),
                updatedTeacher.getBirthDate(), updatedTeacher.getScientificDegree().toString(), updatedTeacher.getId());
        coursesToDelete.forEach(course -> jdbcTemplate.update(DELETE_TEACHER_COURSE_QUERY, course.getId(), updatedTeacher.getId()));
        coursesToAdd.forEach(course -> jdbcTemplate.update(CREATE_TEACHER_COURSE_QUERY, course.getId(), updatedTeacher.getId()));
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}