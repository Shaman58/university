package edu.shmonin.university.service;

import edu.shmonin.university.dao.StudentDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ValidationException;
import edu.shmonin.university.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class StudentService implements EntityService<Student> {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    @Value("${university.student.min.age}")
    private int minAge;

    @Value("${university.group.capacity.max}")
    private int maxCapacity;

    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Student get(int studentId) {
        var student = studentDao.get(studentId);
        if (student.isEmpty()) {
            throw new EntityNotFoundException("Can not find the student. There is no student with id=" + studentId);
        }
        log.debug("Get student with id={}", studentId);
        return student.get();
    }

    @Override
    public List<Student> getAll() {
        log.debug("Get all students");
        return studentDao.getAll();
    }

    @Override
    public void create(Student student) {
        validateStudent(student);
        log.debug("Create student {}", student);
        studentDao.create(student);
    }

    @Override
    public void update(Student student) {
        validateStudent(student);
        log.debug("Update student {}", student);
        studentDao.update(student);
    }

    @Override
    public void delete(int studentId) {
        if (studentDao.get(studentId).isEmpty()) {
            throw new EntityNotFoundException("Can not find the student. There is no student with id=" + studentId);
        }
        log.debug("Delete student by id={}", studentId);
        studentDao.delete(studentId);
    }

    public List<Student> getByGroupId(int groupId) {
        return studentDao.getByGroupId(groupId);
    }

    private void validateStudent(Student student) {
        if (Period.between(student.getBirthDate(), LocalDate.now()).getYears() < minAge) {
            throw new ValidationException("The student " + student + " did not pass the validity check. Student can not be younger " + minAge);
        }
        if (studentDao.getByGroupId(student.getGroup().getId()).size() >= maxCapacity) {
            throw new ValidationException("The student " + student + " did not pass the validity check. Group capacity can not be greater than " + maxCapacity);
        }
    }
}