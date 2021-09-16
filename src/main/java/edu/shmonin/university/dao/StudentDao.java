package edu.shmonin.university.dao;

import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDao implements Dao<Student> {

    private static final String GET_QUERY = "SELECT * FROM students WHERE student_id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM students";
    private static final String CREATE_QUERY = "INSERT INTO students(first_name, last_name, email, country, gender, phone, address, birth_date) VALUES (?,?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE students SET first_name=?,last_name=?,email=?,country=?,gender=?,phone=?,address=?,birth_date=? WHERE student_id=?";
    private static final String DELETE_QUERY = "DELETE FROM students WHERE student_id=?";
    private static final String GET_STUDENTS_RELATED_TO_GROUP_QUERY = "SELECT * FROM students WHERE group_id=?";
    private static final String ADD_STUDENT_TO_THE_GROUP_QUERY = "UPDATE students SET group_id=? WHERE student_id=?";

    private JdbcTemplate jdbcTemplate;
    private StudentMapper studentMapper;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setStudentMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public Student get(int id) {
        return jdbcTemplate.query(GET_QUERY, studentMapper, id).
                stream().findAny().orElse(null);
    }

    @Override
    public List<Student> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, studentMapper);
    }

    @Override
    public void create(Student entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                entity.getCountry(), entity.getGender().toString(), entity.getPhone(), entity.getAddress(),
                entity.getBirthDate());
    }

    @Override
    public void update(Student entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                entity.getCountry(), entity.getGender().toString(), entity.getPhone(), entity.getAddress(),
                entity.getBirthDate(), entity.getStudentId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    public void addStudentToTheGroup(Student student, Group group) {
        jdbcTemplate.update(ADD_STUDENT_TO_THE_GROUP_QUERY, group.getGroupId(), student.getStudentId());
    }

    public List<Student> selectStudentsRelatedToTheGroup(Group group) {
        return jdbcTemplate.query(GET_STUDENTS_RELATED_TO_GROUP_QUERY, studentMapper, group.getGroupId());
    }
}