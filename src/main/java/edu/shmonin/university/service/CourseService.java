package edu.shmonin.university.service;

import edu.shmonin.university.dao.CourseDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements EntityService<Course> {

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
        return jdbcCourseDao.get(courseId);
    }

    @Override
    public List<Course> getAll() {
        return jdbcCourseDao.getAll();
    }

    @Override
    public void create(Course course) {
        jdbcCourseDao.create(course);
    }

    @Override
    public void update(Course course) {
        jdbcCourseDao.update(course);
    }

    @Override
    public void delete(int courseId) {
        if (jdbcLectureDao.getByCourseId(courseId).isEmpty() && jdbcTeacherDao.getByCourseId(courseId).isEmpty()) {
            jdbcCourseDao.delete(courseId);
        }
    }

    public List<Course> getByTeacherId(int teacherId) {
        return jdbcCourseDao.getByTeacherId(teacherId);
    }
}