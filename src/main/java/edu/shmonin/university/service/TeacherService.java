package edu.shmonin.university.service;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.model.Teacher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService implements EntityService<Teacher> {

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
        return jdbcTeacherDao.get(teacherId);
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTeacherDao.getAll();
    }

    @Override
    public void create(Teacher teacher) {
        if (validateAgeOfTeacher(teacher)) {
            jdbcTeacherDao.create(teacher);
        }
    }

    @Override
    public void update(Teacher teacher) {
        var courses = jdbcLectureDao.getByTeacherId(teacher.getId())
                .stream().map(Lecture::getCourse).collect(Collectors.toSet());
        if (teacher.getCourses().containsAll(courses) && validateAgeOfTeacher(teacher)) {
            jdbcTeacherDao.update(teacher);
        }
    }

    @Override
    public void delete(int teacherId) {
        if (jdbcLectureDao.getByTeacherId(teacherId).isEmpty()) {
            jdbcTeacherDao.get(teacherId).getVacations().forEach(p -> jdbcVacationDao.delete(p.getId()));
            jdbcTeacherDao.delete(teacherId);
        }
    }

    private boolean validateAgeOfTeacher(Teacher teacher) {
        var age = Period.between(teacher.getBirthDate(), LocalDate.now()).getYears();
        return age >= minAge && age < maxAge;
    }
}