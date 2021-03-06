package edu.shmonin.university.service;

import edu.shmonin.university.dao.CourseDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ForeignReferenceException;
import edu.shmonin.university.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseDao courseDao;
    private final LectureDao lectureDao;
    private final TeacherDao teacherDao;

    public CourseService(CourseDao courseDao, LectureDao lectureDao, TeacherDao teacherDao) {
        this.courseDao = courseDao;
        this.lectureDao = lectureDao;
        this.teacherDao = teacherDao;
    }

    public Course get(int courseId) {
        log.debug("Get course with id={}", courseId);
        return courseDao.get(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find course by id=" + courseId));
    }

    public List<Course> getAll() {
        log.debug("Get all courses");
        return courseDao.getAll();
    }

    public Page<Course> getAll(Pageable pageable) {
        log.debug("Get all sorted courses");
        return courseDao.getAll(pageable);
    }

    public void create(Course course) {
        log.debug("Create course {}", course);
        courseDao.create(course);
    }

    public void update(Course course) {
        log.debug("Update course {}", course);
        courseDao.update(course);
    }

    public void delete(int courseId) {
        log.debug("Delete course by id={}", courseId);
        this.get(courseId);
        if (!lectureDao.getByCourseId(courseId).isEmpty()) {
            throw new ForeignReferenceException("There are lectures with this course");
        }
        if (!teacherDao.getByCourseId(courseId).isEmpty()) {
            throw new ForeignReferenceException("There are teachers with this course");
        }
        courseDao.delete(courseId);
    }

    public List<Course> getByTeacherId(int teacherId) {
        log.debug("Get all courses by teacher id");
        return courseDao.getByTeacherId(teacherId);
    }
}