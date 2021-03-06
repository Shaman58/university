package edu.shmonin.university.service;

import edu.shmonin.university.dao.CourseDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ForeignReferenceException;
import edu.shmonin.university.model.Course;
import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseDao courseDao;
    @Mock
    private LectureDao lectureDao;
    @Mock
    private TeacherDao teacherDao;

    @InjectMocks
    private CourseService courseService;

    @Test
    void givenId_whenGet_thenReturnCourse() {
        var expected = new Course("math");
        when(courseDao.get(1)).thenReturn(Optional.of(expected));

        var actual = courseService.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllCourses() {
        var expected = new ArrayList<Course>();
        expected.add(new Course("math"));
        expected.add(new Course("hist"));
        when(courseDao.getAll()).thenReturn(expected);

        var actual = courseService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenPageRequest_whenGetAll_thenReturnPageOfCourses() {
        var pageRequest = PageRequest.of(0, 20);
        var courses = new ArrayList<Course>();
        courses.add(new Course("math"));
        courses.add(new Course("hist"));
        var expected = new PageImpl<>(courses, pageRequest, 1);
        when(courseDao.getAll(pageRequest)).thenReturn(expected);

        var actual = courseService.getAll(pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void givenCourse_whenCreate_thenStartedDaoCreate() {
        var course = new Course("math");

        courseService.create(course);

        verify(courseDao).create(course);
    }

    @Test
    void givenCourse_whenUpdate_thenStartedDaoUpdate() {
        var course = new Course("math");

        courseService.update(course);

        verify(courseDao).update(course);
    }

    @Test
    void givenIdAndEmptyListsOfCoursesInLectureDaoAndTeacherDao_whenDelete_thenStartedCourseDaoDelete() {
        when(lectureDao.getByCourseId(1)).thenReturn(new ArrayList<>());
        when(teacherDao.getByCourseId(1)).thenReturn(new ArrayList<>());
        when(courseDao.get(1)).thenReturn(Optional.of(new Course()));
        courseService.delete(1);

        verify(courseDao).delete(1);
    }

    @Test
    void givenIdAndNotEmptyListOfCoursesInLectureDao_whenDelete_thenThrowForeignReferenceExceptionAndNotStartedCourseDaoDelete() {
        var lectures = new ArrayList<Lecture>();
        lectures.add(new Lecture());
        when(lectureDao.getByCourseId(1)).thenReturn(lectures);
        when(courseDao.get(1)).thenReturn(Optional.of(new Course()));

        var exception = assertThrows(ForeignReferenceException.class, () -> courseService.delete(1));

        verify(courseDao, never()).delete(1);
        assertEquals("There are lectures with this course", exception.getMessage());
    }

    @Test
    void givenIdAndNotEmptyListOfCoursesInTeacherDao_whenDelete_thenThrownForeignReferenceExceptionAndNotStartedCourseDaoDelete() {
        var teachers = new ArrayList<Teacher>();
        teachers.add(new Teacher());
        when(teacherDao.getByCourseId(1)).thenReturn(teachers);
        when(courseDao.get(1)).thenReturn(Optional.of(new Course()));

        var exception = assertThrows(ForeignReferenceException.class, () -> courseService.delete(1));

        verify(courseDao, never()).delete(1);
        assertEquals("There are teachers with this course", exception.getMessage());
    }

    @Test
    void givenIdAndEmptyOptionalInGet_whenDelete_thenThrownEntityNotFoundExceptionAndNotStartedCourseDaoDelete() {
        when(courseDao.get(1)).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class, () -> courseService.delete(1));

        verify(courseDao, never()).delete(1);
        assertEquals("Can not find course by id=1", exception.getMessage());
    }

    @Test
    void givenId_whenGetByTeacherId_thenReturnedCourses() {
        var expected = List.of(new Course("math"), new Course("hist"));
        when(courseDao.getByTeacherId(1)).thenReturn(expected);

        var actual = courseService.getByTeacherId(1);

        assertEquals(expected, actual);
    }
}