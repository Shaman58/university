package edu.shmonin.university.service;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.LinkedEntityException;
import edu.shmonin.university.exception.ValidationException;
import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
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

    private final TeacherDao jdbcTeacherDao;
    private final LectureDao jdbcLectureDao;
    private final VacationDao jdbcVacationDao;

    public TeacherService(TeacherDao jdbcTeacherDao, LectureDao jdbcLectureDao, VacationDao jdbcVacationDao) {
        this.jdbcTeacherDao = jdbcTeacherDao;
        this.jdbcLectureDao = jdbcLectureDao;
        this.jdbcVacationDao = jdbcVacationDao;
    }

    @Override
    public Teacher get(int teacherId) {
        try {
            log.debug("Get teacher with id={}", teacherId);
            return jdbcTeacherDao.get(teacherId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the teacher. There is no teacher with id=" + teacherId);
        }
    }

    @Override
    public List<Teacher> getAll() {
        log.debug("Get all teachers");
        return jdbcTeacherDao.getAll();
    }

    @Override
    public void create(Teacher teacher) {
        validateAgeOfTeacher(teacher);
        log.debug("Create teacher {}", teacher);
        jdbcTeacherDao.create(teacher);
    }

    @Override
    public void update(Teacher teacher) {
        validateTeachersCourses(teacher);
        validateAgeOfTeacher(teacher);
        log.debug("Update teacher {}", teacher);
        jdbcTeacherDao.update(teacher);
    }

    @Override
    public void delete(int teacherId) {
        try {
            jdbcTeacherDao.get(teacherId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the teacher. There is no teacher with id=" + teacherId);
        }
        if (!jdbcLectureDao.getByTeacherId(teacherId).isEmpty()) {
            throw new LinkedEntityException("Can not delete teacher with id=" + teacherId + ", there are lectures with this teacher in database");
        }
        log.debug("Delete teacher by id={}", teacherId);
        jdbcTeacherDao.get(teacherId).getVacations().forEach(p -> jdbcVacationDao.delete(p.getId()));
        jdbcTeacherDao.delete(teacherId);

    }

    private void validateTeachersCourses(Teacher teacher) {
        var courses = jdbcLectureDao.getByTeacherId(teacher.getId())
                .stream().map(Lecture::getCourse).collect(Collectors.toSet());
        if (!teacher.getCourses().containsAll(courses)) {
            throw new ValidationException("The teacher " + teacher + " did not pass the validity check. The teacher has not all needed courses");
        }
    }

    private void validateAgeOfTeacher(Teacher teacher) {
        var age = Period.between(teacher.getBirthDate(), LocalDate.now()).getYears();
        if (age < minAge || age >= maxAge) {
            throw new ValidationException("The teacher " + teacher + " did not pass the validity check. The teacher's age is not in the acceptable range");
        }
    }
}