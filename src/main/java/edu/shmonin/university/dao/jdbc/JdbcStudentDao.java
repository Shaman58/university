package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.StudentDao;
import edu.shmonin.university.dao.jdbc.rowmapper.StudentRowMapper;
import edu.shmonin.university.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcStudentDao implements StudentDao {

    private static final String GET_QUERY = "SELECT * FROM students WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM students";
    private static final String CREATE_QUERY = "INSERT INTO students(first_name, last_name, email, country, gender, phone, address, birth_date, group_id) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE students SET first_name=?,last_name=?,email=?,country=?,gender=?,phone=?,address=?,birth_date=?, group_id=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM students WHERE id=?";
    private static final String GET_GROUP_STUDENTS = "SELECT * FROM students WHERE group_id=?";

    private final JdbcTemplate jdbcTemplate;
    private StudentRowMapper studentRowMapper;
    private final BeanPropertyRowMapper<Student> rowMapper;

    public JdbcStudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = BeanPropertyRowMapper.newInstance(Student.class);
    }

    @Autowired
    public void setStudentRowMapper(StudentRowMapper studentRowMapper) {
        this.studentRowMapper = studentRowMapper;
    }

    @Override
    public Optional<Student> get(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GET_QUERY, studentRowMapper, id));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
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
            preparedStatement.setObject(9, student.getGroup() == null ? null : student.getGroup().getId());
            return preparedStatement;
        }, keyHolder);
        student.setId((Integer) Objects.requireNonNull(keyHolder.getKeys().get("id")));
    }

    @Override
    public void update(Student student) {
        jdbcTemplate.update(UPDATE_QUERY, student.getFirstName(), student.getLastName(), student.getEmail(),
                student.getCountry(), student.getGender().toString(), student.getPhone(), student.getAddress(),
                student.getBirthDate(), student.getGroup() == null ? null : student.getGroup().getId(), student.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public List<Student> getByGroupId(int groupId) {
        return jdbcTemplate.query(GET_GROUP_STUDENTS, rowMapper, groupId);
    }
}