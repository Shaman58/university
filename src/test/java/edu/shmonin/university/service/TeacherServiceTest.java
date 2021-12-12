package edu.shmonin.university.service;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.TeacherNotAvailableException;
import edu.shmonin.university.model.*;
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
class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;
    @Mock
    private VacationDao vacationDao;
    @Mock
    private LectureDao lectureDao;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        setField(teacherService, "minAge", 25);
        setField(teacherService, "maxAge", 100);
    }

    @Test
    void givenId_whenGet_thenReturnedTeacher() {
        var expected = new Teacher();
        when(teacherDao.get(1)).thenReturn(Optional.of(expected));

        var actual = teacherService.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnedTeachers() {
        var expected = List.of(new Teacher());
        when(teacherDao.getAll()).thenReturn(expected);

        var actual = teacherService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenValidTeacher_whenCreate_thenStartedTeacherDaoCreate() {
        var teacher = new Teacher();
        teacher.setFirstName("name-1");
        teacher.setLastName("surname-1");
        teacher.setEmail("email-1");
        teacher.setCountry("country-1");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-1");
        teacher.setAddress("address-1");
        teacher.setBirthDate(LocalDate.now().minusYears(25));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);

        teacherService.create(teacher);

        verify(teacherDao).create(teacher);
    }

    @Test
    void givenInvalidTeacherTooYang_whenCreate_thenThrowTeacherNotAvailableExceptionAndStartedNotTeacherDaoCreate() {
        var teacher = new Teacher();
        teacher.setFirstName("name-1");
        teacher.setLastName("surname-1");
        teacher.setEmail("email-1");
        teacher.setCountry("country-1");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-1");
        teacher.setAddress("address-1");
        teacher.setBirthDate(LocalDate.now().minusYears(24));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);

        var exception = assertThrows(TeacherNotAvailableException.class, () -> teacherService.create(teacher));

        verify(teacherDao, never()).create(teacher);
        assertEquals("The teacher's age is not in the acceptable range", exception.getMessage());
    }

    @Test
    void givenInvalidTeacherTooOld_whenCreate_thenThrowTeacherNotAvailableExceptionAndNotStartedTeacherDaoCreate() {
        var teacher = new Teacher();
        teacher.setFirstName("name-1");
        teacher.setLastName("surname-1");
        teacher.setEmail("email-1");
        teacher.setCountry("country-1");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-1");
        teacher.setAddress("address-1");
        teacher.setBirthDate(LocalDate.now().minusYears(100));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);

        var exception = assertThrows(TeacherNotAvailableException.class, () -> teacherService.create(teacher));

        verify(teacherDao, never()).create(teacher);
        assertEquals("The teacher's age is not in the acceptable range", exception.getMessage());
    }

    @Test
    void givenValidTeacherAndTeacherHasAllNeededCourses_whenUpdate_thenStartedTeacherDaoUpdate() {
        var teacher = new Teacher();
        teacher.setFirstName("name-1");
        teacher.setLastName("surname-1");
        teacher.setEmail("email-1");
        teacher.setCountry("country-1");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-1");
        teacher.setAddress("address-1");
        teacher.setBirthDate(LocalDate.now().minusYears(25));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);
        teacher.setCourses(List.of(new Course("course1"), new Course("course2")));
        teacher.setId(1);
        var lecture = new Lecture();
        lecture.setCourse(new Course("course1"));
        when(lectureDao.getByTeacherId(1)).thenReturn(List.of(lecture));

        teacherService.update(teacher);

        verify(teacherDao).update(teacher);
    }

    @Test
    void givenValidTeacherAndTeacherHasNotAllNeededCourses_whenUpdate_thenThrowTeacherNotAvailableExceptionAndNotStartedTeacherDaoUpdate() {
        var teacher = new Teacher();
        teacher.setFirstName("name-1");
        teacher.setLastName("surname-1");
        teacher.setEmail("email-1");
        teacher.setCountry("country-1");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-1");
        teacher.setAddress("address-1");
        teacher.setBirthDate(LocalDate.now().minusYears(25));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);
        teacher.setCourses(List.of(new Course("course3"), new Course("course4")));
        teacher.setId(1);
        var lecture = new Lecture();
        lecture.setCourse(new Course("course1"));
        when(lectureDao.getByTeacherId(1)).thenReturn(List.of(lecture));

        var exception = assertThrows(TeacherNotAvailableException.class, () -> teacherService.update(teacher));

        verify(teacherDao, never()).update(teacher);
        assertEquals("The teacher has not all needed courses", exception.getMessage());
    }

    @Test
    void givenInvalidTeacherAndTeacherHasAllNeededCourses_whenUpdate_thenThrowTeacherNotAvailableExceptionAndNotStartedTeacherDaoUpdate() {
        var teacher = new Teacher();
        teacher.setFirstName("name-1");
        teacher.setLastName("surname-1");
        teacher.setEmail("email-1");
        teacher.setCountry("country-1");
        teacher.setGender(Gender.MALE);
        teacher.setPhone("phone-1");
        teacher.setAddress("address-1");
        teacher.setBirthDate(LocalDate.now().minusYears(24));
        teacher.setScientificDegree(ScientificDegree.DOCTOR);
        teacher.setCourses(List.of(new Course("course1"), new Course("course2")));
        teacher.setId(1);
        var lecture = new Lecture();
        lecture.setCourse(new Course("course1"));
        when(lectureDao.getByTeacherId(1)).thenReturn(List.of(lecture));

        var exception = assertThrows(TeacherNotAvailableException.class, () -> teacherService.update(teacher));

        verify(teacherDao, never()).update(teacher);
        assertEquals("The teacher's age is not in the acceptable range", exception.getMessage());
    }

    @Test
    void givenIdAndTeacherDaoReturnNotEmptyOptional_whenDelete_thenStartedTeacherDaoDelete() {
        when(lectureDao.getByTeacherId(1)).thenReturn(new ArrayList<>());
        var teacher = new Teacher();
        when(teacherDao.get(1)).thenReturn(Optional.of(teacher));
        when(vacationDao.getByTeacherId(1)).thenReturn(List.of(new Vacation(), new Vacation()));

        teacherService.delete(1);

        verify(teacherDao).delete(1);
        verify(vacationDao, times(2)).delete(any(Integer.class));
    }

    @Test
    void givenIdAndTeacherDaoReturnEmptyOptional_whenDelete_thenThrowEntityNotFoundExceptionAndNotStartedTeacherDaoDelete() {
        when(teacherDao.get(1)).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class, () -> teacherService.delete(1));

        verify(teacherDao, never()).delete(1);
        verify(vacationDao, never()).delete(any(Integer.class));
        assertEquals("Can not find teacher by id=1", exception.getMessage());
    }
}