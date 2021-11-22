package edu.shmonin.university.service;

import edu.shmonin.university.dao.CourseDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.LinkedEntityException;
import edu.shmonin.university.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements EntityService<Course> {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseDao jdbcCourseDao;
    private final LectureDao jdbcLectureDao;
    private final TeacherDao jdbcTeacherDao;

    public CourseService(CourseDao jdbcCourseDao, LectureDao jdbcLectureDao, TeacherDao jdbcTeacherDao) {
        this.jdbcCourseDao = jdbcCourseDao;
        this.jdbcLectureDao = jdbcLectureDao;
        this.jdbcTeacherDao = jdbcTeacherDao;
    }

    @Override
    public Course get(int courseId) {
        try {
            log.debug("Get course with id={}", courseId);
            return jdbcCourseDao.get(courseId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the course. There is no course with id=" + courseId);
        }
    }

    @Override
    public List<Course> getAll() {
        log.debug("Get all courses");
        return jdbcCourseDao.getAll();
    }

    @Override
    public void create(Course course) {
        log.debug("Create course {}", course);
        jdbcCourseDao.create(course);
    }

    @Override
    public void update(Course course) {
        log.debug("Update course {}", course);
        jdbcCourseDao.update(course);
    }

    @Override
    public void delete(int courseId) {
        try {
            jdbcCourseDao.get(courseId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not delete the course. There is no course with id=" + courseId);
        }
        if (!jdbcLectureDao.getByCourseId(courseId).isEmpty()) {
            throw new LinkedEntityException("Can not delete course with id=" + courseId + ", there are lectures with this audience in database");
        }
        if (!jdbcTeacherDao.getByCourseId(courseId).isEmpty()) {
            throw new LinkedEntityException("Can not delete course with id=" + courseId + ", there are teachers with this audience in database");
        }
        jdbcCourseDao.delete(courseId);
    }

    public List<Course> getByTeacherId(int teacherId) {
        return jdbcCourseDao.getByTeacherId(teacherId);
    }
}