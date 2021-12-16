package edu.shmonin.university.service;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ForeignReferenceException;
import edu.shmonin.university.exception.TeacherNotAvailableException;
import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService implements EntityService<Teacher> {

    private static final Logger log = LoggerFactory.getLogger(TeacherService.class);

    @Value("${university.teacher.min.age}")
    private int minAge;

    @Value("${university.human.max.age}")
    private int maxAge;

    private final TeacherDao teacherDao;
    private final LectureDao lectureDao;
    private final VacationDao vacationDao;

    public TeacherService(TeacherDao teacherDao, LectureDao lectureDao, VacationDao vacationDao) {
        this.teacherDao = teacherDao;
        this.lectureDao = lectureDao;
        this.vacationDao = vacationDao;
    }

    @Override
    public Teacher get(int teacherId) {
        log.debug("Get teacher with id={}", teacherId);
        return teacherDao.get(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find teacher by id=" + teacherId));
    }

    @Override
    public List<Teacher> getAll() {
        log.debug("Get all teachers");
        return teacherDao.getAll();
    }

    @Override
    public void create(Teacher teacher) {
        log.debug("Create teacher {}", teacher);
        validateAgeOfTeacher(teacher);
        teacherDao.create(teacher);
    }

    @Override
    public void update(Teacher teacher) {
        log.debug("Update teacher {}", teacher);
        validateTeachersCourses(teacher);
        validateAgeOfTeacher(teacher);
        teacherDao.update(teacher);
    }

    @Override
    public void delete(int teacherId) {
        log.debug("Delete teacher by id={}", teacherId);
        this.get(teacherId);
        if (!lectureDao.getByTeacherId(teacherId).isEmpty()) {
            throw new ForeignReferenceException("There are lectures with this teacher");
        }
        vacationDao.getByTeacherId(teacherId).forEach(p -> vacationDao.delete(p.getId()));
        teacherDao.delete(teacherId);

    }

    private void validateTeachersCourses(Teacher teacher) {
        var courses = lectureDao.getByTeacherId(teacher.getId())
                .stream().map(Lecture::getCourse).collect(Collectors.toSet());
        if (!teacher.getCourses().containsAll(courses)) {
            throw new TeacherNotAvailableException("The teacher has not all needed courses");
        }
    }

    private void validateAgeOfTeacher(Teacher teacher) {
        var age = Period.between(teacher.getBirthDate(), LocalDate.now()).getYears();
        if (age < minAge || age >= maxAge) {
            throw new TeacherNotAvailableException("The teacher's age is not in the acceptable range");
        }
    }
}