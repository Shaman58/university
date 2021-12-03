package edu.shmonin.university.service;

import edu.shmonin.university.dao.StudentDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        setField(studentService, "minAge", 17);
        setField(studentService, "maxCapacity", 2);
    }

    @Test
    void givenId_whenGet_thenReturnedStudent() {
        var expected = new Student();
        when(studentDao.get(1)).thenReturn(Optional.of(expected));

        var actual = studentService.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnedStudents() {
        var expected = List.of(new Student());
        when(studentDao.getAll()).thenReturn(expected);

        var actual = studentService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenValidStudentAndGroupListOfStudentsIsNotFull_whenCreate_thenStartedStudentDaoCreate() {
        var student = new Student();
        student.setFirstName("name-1");
        student.setLastName("surname-1");
        student.setEmail("email-1");
        student.setCountry("country-1");
        student.setGender(Gender.MALE);
        student.setPhone("phone-1");
        student.setAddress("address-1");
        student.setBirthDate(LocalDate.of(1980, 1, 1));
        var group = new Group("group");
        group.setId(1);
        student.setGroup(group);
        var students = new ArrayList<Student>();
        when(studentDao.getByGroupId(1)).thenReturn(students);

        studentService.create(student);

        verify(studentDao).create(student);
    }

    @Test
    void givenValidStudentAndGroupListOfStudentsIsFull_whenCreate_thenThrowRuntimeExceptionAndNotStartedStudentDaoCreate() {
        var student = new Student();
        student.setFirstName("name-1");
        student.setLastName("surname-1");
        student.setEmail("email-1");
        student.setCountry("country-1");
        student.setGender(Gender.MALE);
        student.setPhone("phone-1");
        student.setAddress("address-1");
        student.setBirthDate(LocalDate.of(1980, 1, 1));
        var group = new Group("group");
        group.setId(1);
        student.setGroup(group);
        var students = List.of(new Student(), new Student());
        when(studentDao.getByGroupId(1)).thenReturn(students);

        assertThrows(RuntimeException.class, () -> studentService.create(student));

        verify(studentDao, never()).create(student);
    }

    @Test
    void givenInvalidStudentAndGroupListOfStudentsIsFull_whenCreate_thenThrowRuntimeExceptionAndNotStartedStudentDaoCreate() {
        var student = new Student();
        student.setFirstName("name-1");
        student.setLastName("surname-1");
        student.setEmail("email-1");
        student.setCountry("country-1");
        student.setGender(Gender.MALE);
        student.setPhone("phone-1");
        student.setAddress("address-1");
        student.setBirthDate(LocalDate.now().minusYears(16));

        assertThrows(RuntimeException.class, () -> studentService.create(student));

        verify(studentDao, never()).create(student);
    }

    @Test
    void givenValidStudentAndGroupListOfStudentsIsNotFull_whenUpdate_thenStartedStudentDaoUpdate() {
        var student = new Student();
        student.setFirstName("name-1");
        student.setLastName("surname-1");
        student.setEmail("email-1");
        student.setCountry("country-1");
        student.setGender(Gender.MALE);
        student.setPhone("phone-1");
        student.setAddress("address-1");
        student.setBirthDate(LocalDate.of(1980, 1, 1));
        var group = new Group("group");
        group.setId(1);
        student.setGroup(group);
        var students = new ArrayList<Student>();
        when(studentDao.getByGroupId(1)).thenReturn(students);

        studentService.update(student);

        verify(studentDao).update(student);
    }

    @Test
    void givenValidStudentAndGroupListOfStudentsIsFull_whenUpdate_thenThrowRuntimeExceptionAndNotStartedStudentDaoUpdate() {
        var student = new Student();
        student.setFirstName("name-1");
        student.setLastName("surname-1");
        student.setEmail("email-1");
        student.setCountry("country-1");
        student.setGender(Gender.MALE);
        student.setPhone("phone-1");
        student.setAddress("address-1");
        student.setBirthDate(LocalDate.of(1980, 1, 1));
        var group = new Group("group");
        group.setId(1);
        student.setGroup(group);
        var students = List.of(new Student(), new Student());
        when(studentDao.getByGroupId(1)).thenReturn(students);

        assertThrows(RuntimeException.class, () -> studentService.create(student));

        verify(studentDao, never()).update(student);
    }

    @Test
    void givenInvalidStudentAndGroupListOfStudentsIsFull_whenUpdate_thenThrowRuntimeExceptionAndNotStartedStudentDaoUpdate() {
        var student = new Student();
        student.setFirstName("name-1");
        student.setLastName("surname-1");
        student.setEmail("email-1");
        student.setCountry("country-1");
        student.setGender(Gender.MALE);
        student.setPhone("phone-1");
        student.setAddress("address-1");
        student.setBirthDate(LocalDate.now().minusYears(16));

        assertThrows(RuntimeException.class, () -> studentService.create(student));

        verify(studentDao, never()).update(student);
    }

    @Test
    void givenIdAndStudentDaoReturnNotEmptyOptional_whenDelete_thenStartedStudentDaoDelete() {
        when(studentDao.get(1)).thenReturn(Optional.of(new Student()));

        studentService.delete(1);

        verify(studentDao).delete(1);
    }

    @Test
    void givenIdAndStudentDaoReturnEmptyOptional_whenDelete_thenThrowEntityNotFoundExceptionNotStartedStudentDaoDelete() {
        when(studentDao.get(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> studentService.delete(1));

        verify(studentDao, never()).delete(1);
    }

    @Test
    void givenId_whenGetByGroupId_thenReturnedStudents() {
        var expected = List.of(new Student());
        when(studentDao.getByGroupId(1)).thenReturn(expected);

        var actual = studentService.getByGroupId(1);

        assertEquals(expected, actual);
    }
}