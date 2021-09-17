package edu.shmonin.university.dao;

import edu.shmonin.university.model.Course;
import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TeacherDao implements Dao<Teacher> {

    private static final String GET_QUERY = "SELECT * FROM teachers WHERE teacher_id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM teachers";
    private static final String CREATE_QUERY = "INSERT INTO teachers(first_name, last_name, email, country, gender, phone, address, birth_date, scientific_degree) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE teachers SET first_name=?,last_name=?,email=?,country=?,gender=?,phone=?,address=?,birth_date=?, scientific_degree=? WHERE teacher_id=?";
    private static final String DELETE_QUERY = "DELETE FROM teachers WHERE teacher_id=?";
    private static final String CREATE_COURSE_TEACHER_QUERY = "INSERT INTO courses_teachers(course_id, teacher_id) VALUES (?,?)";
    private static final String GET_LECTURE_TEACHER_QUERY =
            "SELECT teacher_id, first_name, last_name, email, country, gender, phone, address, birth_date, scientific_degree FROM teachers NATURAL JOIN lectures WHERE lecture_id=?";

    private JdbcTemplate jdbcTemplate;
    private CourseDao courseDao;
    private VacationDao vacationDao;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Autowired
    public void setVacationDao(VacationDao vacationDao) {
        this.vacationDao = vacationDao;
    }

    @Override
    public Teacher get(int id) {
        return jdbcTemplate.query(GET_QUERY, new BeanPropertyRowMapper<>(Teacher.class), id).
                stream().peek(p -> p.setCourses(courseDao.getTeacherCourses(p))).
                peek(p -> p.setVacations(vacationDao.getTeacherVacations(p))).
                findAny().orElse(null);
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Teacher.class)).
                stream().peek(p -> p.setCourses(courseDao.getTeacherCourses(p))).
                peek(p -> p.setVacations(vacationDao.getTeacherVacations(p))).
                collect(Collectors.toList());
    }

    @Override
    public void create(Teacher entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                entity.getCountry(), entity.getGender().toString(), entity.getPhone(), entity.getAddress(),
                entity.getBirthDate(), entity.getScientificDegree().toString());
    }

    @Override
    public void update(Teacher entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getFirstName(), entity.getLastName(), entity.getEmail(),
                entity.getCountry(), entity.getGender().toString(), entity.getPhone(), entity.getAddress(),
                entity.getBirthDate(), entity.getScientificDegree().toString(), entity.getTeacherId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    public void addCourseTeacher(Course course, Teacher teacher) {
        jdbcTemplate.update(CREATE_COURSE_TEACHER_QUERY, course.getCourseId(), teacher.getTeacherId());
    }

    public Teacher getLectureTeacher(Lecture lecture) {
        return jdbcTemplate.query(GET_LECTURE_TEACHER_QUERY, new BeanPropertyRowMapper<>(Teacher.class), lecture.getLectureId()).
                stream().findAny().orElse(null);
    }
}