package edu.shmonin.university.service;

import edu.shmonin.university.dao.CourseDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.model.Course;
import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        when(courseDao.get(1)).thenReturn(expected);

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
        courseService.delete(1);

        verify(courseDao).delete(1);
    }

    @Test
    void givenIdAndNotEmptyListOfCoursesInLectureDao_whenDelete_thenNotStartedCourseDaoDelete() {
        var lectures = new ArrayList<Lecture>();
        lectures.add(new Lecture());
        when(lectureDao.getByCourseId(1)).thenReturn(lectures);

        courseService.delete(1);

        verify(courseDao, never()).delete(1);
    }

    @Test
    void givenIdAndNotEmptyListOfCoursesInTeacherDao_whenDelete_thenNotStartedCourseDaoDelete() {
        var teachers = new ArrayList<Teacher>();
        teachers.add(new Teacher());
        when(teacherDao.getByCourseId(1)).thenReturn(teachers);

        courseService.delete(1);

        verify(courseDao, never()).delete(1);
    }

    @Test
    void givenId_whenGetByTeacherId_thenReturnedCourses() {
        var expected = List.of(new Course("math"), new Course("hist"));
        when(courseDao.getByTeacherId(1)).thenReturn(expected);

        var actual = courseService.getByTeacherId(1);

        assertEquals(expected, actual);
    }
}