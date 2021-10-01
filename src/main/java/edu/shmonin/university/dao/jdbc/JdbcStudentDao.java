package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.StudentDao;
import edu.shmonin.university.dao.rowmapper.StudentRowMapper;
import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcStudentDao implements StudentDao {

    private static final String GET_QUERY = "SELECT * FROM students WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM students";
    private static final String CREATE_QUERY = "INSERT INTO students(first_name, last_name, email, country, gender, phone, address, birth_date) VALUES (?,?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE students SET first_name=?,last_name=?,email=?,country=?,gender=?,phone=?,address=?,birth_date=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM students WHERE id=?";
    private static final String ADD_STUDENT_TO_THE_GROUP_QUERY = "UPDATE students SET group_id=? WHERE id=?";
    private static final String GET_GROUP_STUDENTS = "SELECT * FROM students WHERE group_id=?";

    private final JdbcTemplate jdbcTemplate;
    private final StudentRowMapper studentRowMapper;

    public JdbcStudentDao(JdbcTemplate jdbcTemplate, StudentRowMapper studentRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentRowMapper = studentRowMapper;
    }

    @Override
    public Student get(int id) {
        return jdbcTemplate.queryForObject(GET_QUERY, studentRowMapper, id);
    }

    @Override
    public List<Student> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, studentRowMapper);
    }

    @Override
    public void create(Student student) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getCountry());
            preparedStatement.setString(5, student.getGender().toString());
            preparedStatement.setString(6, student.getPhone());
            preparedStatement.setString(7, student.getAddress());
            preparedStatement.setObject(8, student.getBirthDate());
            return preparedStatement;
        }, keyHolder);
        student.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void update(Student student) {
        jdbcTemplate.update(UPDATE_QUERY, student.getFirstName(), student.getLastName(), student.getEmail(),
                student.getCountry(), student.getGender().toString(), student.getPhone(), student.getAddress(),
                student.getBirthDate(), student.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public void addStudentToTheGroup(Student student, Group group) {
        jdbcTemplate.update(ADD_STUDENT_TO_THE_GROUP_QUERY, group.getId(), student.getId());
    }

    @Override
    public List<Student> getByGroupId(int groupId) {
        return jdbcTemplate.query(GET_GROUP_STUDENTS, studentRowMapper, groupId);
    }
}