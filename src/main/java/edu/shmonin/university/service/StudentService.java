package edu.shmonin.university.service;

import edu.shmonin.university.dao.StudentDao;
import edu.shmonin.university.model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class StudentService implements EntityService<Student> {

    @Value("${university.student.min.age}")
    private int minAge;

    private final StudentDao jdbcStudentDao;

    public StudentService(StudentDao jdbcStudentDao) {
        this.jdbcStudentDao = jdbcStudentDao;
    }

    @Override
    public Student get(int studentId) {
        return jdbcStudentDao.get(studentId);
    }

    @Override
    public List<Student> getAll() {
        return jdbcStudentDao.getAll();
    }

    @Override
    public void create(Student student) {
        if (validateStudent(student)) {
            jdbcStudentDao.create(student);
        }
    }

    @Override
    public void update(Student student) {
        if (validateStudent(student)) {
            jdbcStudentDao.update(student);
        }
    }

    @Override
    public void delete(int studentId) {
        jdbcStudentDao.delete(studentId);
    }

    public List<Student> getByGroupId(int groupId) {
        return jdbcStudentDao.getByGroupId(groupId);
    }

    private boolean validateStudent(Student student) {
        return Period.between(LocalDate.now(), student.getBirthDate()).getYears() >= minAge;
    }
}