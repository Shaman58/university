package edu.shmonin.university.service.implementation;

import edu.shmonin.university.dao.StudentDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.StudentNotAvailableException;
import edu.shmonin.university.exception.StudentsLimitReachedException;
import edu.shmonin.university.model.Student;
import edu.shmonin.university.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Value("${university.student.min.age}")
    private int minAge;

    @Value("${university.group.capacity.max}")
    private int maxCapacity;

    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Student get(int studentId) {
        log.debug("Get student with id={}", studentId);
        return studentDao.get(studentId).orElseThrow(() -> new EntityNotFoundException("Can not find student by id=" + studentId));
    }

    @Override
    public List<Student> getAll() {
        log.debug("Get all students");
        return studentDao.getAll();
    }

    @Override
    public Page<Student> getAll(Pageable pageable) {
        log.debug("Get all sorted students");
        return studentDao.getAll(pageable);
    }

    @Override
    public void create(Student student) {
        log.debug("Create student {}", student);
        validateStudent(student);
        studentDao.create(student);
    }

    @Override
    public void update(Student student) {
        log.debug("Update student {}", student);
        validateStudent(student);
        studentDao.update(student);
    }

    @Override
    public void delete(int studentId) {
        log.debug("Delete student by id={}", studentId);
        this.get(studentId);
        studentDao.delete(studentId);
    }

    @Override
    public List<Student> getByGroupId(int groupId) {
        log.debug("Get students with group id={}", groupId);
        return studentDao.getByGroupId(groupId);
    }

    private void validateStudent(Student student) {
        if (Period.between(student.getBirthDate(), LocalDate.now()).getYears() < minAge) {
            throw new StudentNotAvailableException("Student can not be younger " + minAge);
        }
        if (studentDao.getByGroupId(student.getGroup().getId()).size() >= maxCapacity) {
            throw new StudentsLimitReachedException("The number of students in the group cannot be more than " + maxCapacity);
        }
    }
}