package edu.shmonin.university.service;

import edu.shmonin.university.dao.CourseDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ChainedEntityException;
import edu.shmonin.university.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements EntityService<Course> {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseDao courseDao;
    private final LectureDao lectureDao;
    private final TeacherDao teacherDao;

    public CourseService(CourseDao courseDao, LectureDao lectureDao, TeacherDao teacherDao) {
        this.courseDao = courseDao;
        this.lectureDao = lectureDao;
        this.teacherDao = teacherDao;
    }

    @Override
    public Course get(int courseId) {
        var course = courseDao.get(courseId);
        if (course.isEmpty()) {
            throw new EntityNotFoundException("Can not find the course. There is no course with id=" + courseId);
        }
        log.debug("Get course with id={}", courseId);
        return course.get();
    }

    @Override
    public List<Course> getAll() {
        log.debug("Get all courses");
        return courseDao.getAll();
    }

    @Override
    public void create(Course course) {
        log.debug("Create course {}", course);
        courseDao.create(course);
    }

    @Override
    public void update(Course course) {
        log.debug("Update course {}", course);
        courseDao.update(course);
    }

    @Override
    public void delete(int courseId) {
        if (courseDao.get(courseId).isEmpty()) {
            throw new EntityNotFoundException("Can not delete the course. There is no course with id=" + courseId);
        }
        if (!lectureDao.getByCourseId(courseId).isEmpty()) {
            throw new ChainedEntityException("Can not delete course with id=" + courseId + ", there are lectures with this audience in database");
        }
        if (!teacherDao.getByCourseId(courseId).isEmpty()) {
            throw new ChainedEntityException("Can not delete course with id=" + courseId + ", there are teachers with this audience in database");
        }
        courseDao.delete(courseId);
    }

    public List<Course> getByTeacherId(int teacherId) {
        return courseDao.getByTeacherId(teacherId);
    }
}