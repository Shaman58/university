package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.dao.jdbc.rowmapper.TeacherRowMapper;
import edu.shmonin.university.model.Course;
import edu.shmonin.university.model.Student;
import edu.shmonin.university.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTeacherDao implements TeacherDao {

    private static final String GET_COUNT_QUERY = " SELECT COUNT(*) FROM teachers";
    private static final String GET_QUERY = "SELECT * FROM teachers WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM teachers";
    private static final String CREATE_QUERY = "INSERT INTO teachers(first_name, last_name, email, country, gender, phone, address, birth_date, scientific_degree) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE teachers SET first_name=?,last_name=?,email=?,country=?,gender=?,phone=?,address=?,birth_date=?, scientific_degree=? WHERE id=?";
    private static final String DELETE_TEACHER_COURSE_QUERY = "DELETE FROM teacher_courses WHERE course_id=? AND teacher_id=?";
    private static final String DELETE_QUERY = "DELETE FROM teachers WHERE id=?";
    private static final String CREATE_TEACHER_COURSE_QUERY = "INSERT INTO teacher_courses(course_id, teacher_id) VALUES (?,?)";
    private static final String GET_BY_COURSE = "SELECT id, first_name, last_name, email, country, gender, phone, address, birth_date, scientific_degree FROM teachers INNER JOIN teacher_courses ON teachers.id = teacher_courses.teacher_id WHERE course_id=?";
    private static final String GET_PAGE_QUERY = "SELECT * FROM teachers order by last_name OFFSET ? LIMIT ?";

    private final JdbcTemplate jdbcTemplate;
    private final TeacherRowMapper teacherRowMapper;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherRowMapper teacherRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherRowMapper = teacherRowMapper;
    }

    @Override
    public Optional<Teacher> get(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_QUERY, teacherRowMapper, id));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, teacherRowMapper);
    }

    @Override
    public Page<Teacher> getAll(Pageable pageable) {
        int studentsQuantity = jdbcTemplate.queryForObject(GET_COUNT_QUERY, Integer.class);
        var students = jdbcTemplate.query(GET_PAGE_QUERY, teacherRowMapper,
                pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(students, pageable, studentsQuantity);
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
        teacher.setId((Integer) Objects.requireNonNull(keyHolder.getKeys().get("id")));
        if (teacher.getCourses() != null) {
            teacher.getCourses().forEach(course -> jdbcTemplate.update(CREATE_TEACHER_COURSE_QUERY, course.getId(), teacher.getId()));
        }
    }

    @Transactional
    @Override
    public void update(Teacher updatedTeacher) {
        var courses = getCoursesFromTheTeacher(updatedTeacher);
        jdbcTemplate.update(UPDATE_QUERY, updatedTeacher.getFirstName(), updatedTeacher.getLastName(), updatedTeacher.getEmail(),
                updatedTeacher.getCountry(), updatedTeacher.getGender().toString(), updatedTeacher.getPhone(), updatedTeacher.getAddress(),
                updatedTeacher.getBirthDate(), updatedTeacher.getScientificDegree().toString(), updatedTeacher.getId());
        courses.stream().filter(p -> !updatedTeacher.getCourses().contains(p))
                .forEach(course -> jdbcTemplate.update(DELETE_TEACHER_COURSE_QUERY, course.getId(), updatedTeacher.getId()));
        updatedTeacher.getCourses().stream().filter(p -> !courses.contains(p))
                .forEach(course -> jdbcTemplate.update(CREATE_TEACHER_COURSE_QUERY, course.getId(), updatedTeacher.getId()));
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public List<Teacher> getByCourseId(int courseId) {
        return jdbcTemplate.query(GET_BY_COURSE, teacherRowMapper, courseId);
    }

    private List<Course> getCoursesFromTheTeacher(Teacher updatedTeacher) {
        var teacher = get(updatedTeacher.getId());
        if (teacher.isPresent()) {
            return teacher.get().getCourses();
        } else {
            return new ArrayList<>();
        }
    }
}