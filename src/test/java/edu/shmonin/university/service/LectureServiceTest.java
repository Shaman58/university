package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureDao lectureDao;
    @Mock
    private HolidayDao holidayDao;

    @InjectMocks
    private LectureService lectureService;

    @BeforeEach
    void setUp() {
        setField(lectureService, "maxGroups", 3);
    }

    @Test
    void givenId_whenGet_thenReturnedLecture() {
        var expected = new Lecture();
        when(lectureDao.get(1)).thenReturn(expected);

        var actual = lectureService.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllLectures() {
        var expected = List.of(new Lecture());
        when(lectureDao.getAll()).thenReturn(expected);

        var actual = lectureService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenValidLecture_whenCreate_thenStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var course = new Course("math");
        lecture.setCourse(course);
        lecture.setGroups(List.of(new Group()));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setCourses(List.of(course));
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(new ArrayList<>());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());

        lectureService.create(lecture);

        verify(lectureDao).create(lecture);
    }

    @Test
    void givenOutOfDateLecture_whenCreate_thenNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().minus(1, ChronoUnit.DAYS));

        lectureService.create(lecture);

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureAndAudienceIsBusyness_whenCreate_thenNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));

        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(List.of(lecture));

        lectureService.create(lecture);

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureOnHoliday_whenCreate_thenNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(List.of(new Holiday("holiday", LocalDate.now().plus(1, ChronoUnit.DAYS))));

        lectureService.create(lecture);

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureOnTeacherVacation_whenCreate_thenNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(List.of(new Vacation(LocalDate.now(), LocalDate.now().plusDays(2))));
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(new ArrayList<>());

        lectureService.create(lecture);

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureAndTeacherIsBusyness_whenCreate_thenNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(new ArrayList<>());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(List.of(lecture));

        lectureService.create(lecture);

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureAndTeacherHasNotNeededCourse_whenCreate_thenNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        teacher.setCourses(new ArrayList<>());
        var course = new Course("math");
        lecture.setCourse(course);
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(new ArrayList<>());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());

        lectureService.create(lecture);

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureGroupsAreMoreThenAllowed_whenCreate_thenNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        teacher.setCourses(new ArrayList<>());
        var course = new Course("math");
        lecture.setCourse(course);
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(new ArrayList<>());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        lecture.setGroups(List.of(new Group(), new Group(), new Group(), new Group()));

        lectureService.create(lecture);

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLecture_whenUpdate_thenStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var course = new Course("math");
        lecture.setCourse(course);
        lecture.setGroups(List.of(new Group()));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setCourses(List.of(course));
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(new ArrayList<>());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());

        lectureService.update(lecture);

        verify(lectureDao).update(lecture);
    }

    @Test
    void givenOutOfDateLecture_whenUpdate_thenNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().minus(1, ChronoUnit.DAYS));

        lectureService.update(lecture);

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureAndAudienceIsBusyness_whenUpdate_thenNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));

        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(List.of(lecture));

        lectureService.update(lecture);

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureOnHoliday_whenUpdate_thenNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(List.of(new Holiday("holiday", LocalDate.now().plus(1, ChronoUnit.DAYS))));

        lectureService.update(lecture);

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureOnTeacherVacation_whenUpdate_thenNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(List.of(new Vacation(LocalDate.now(), LocalDate.now().plusDays(2))));
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(new ArrayList<>());

        lectureService.update(lecture);

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureAndTeacherIsBusyness_whenUpdate_thenNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(new ArrayList<>());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(List.of(lecture));

        lectureService.update(lecture);

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureAndTeacherHasNotNeededCourse_whenUpdate_thenNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        teacher.setCourses(new ArrayList<>());
        var course = new Course("math");
        lecture.setCourse(course);
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(new ArrayList<>());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());

        lectureService.update(lecture);

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureGroupsAreMoreThenAllowed_whenUpdate_thenNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        teacher.setCourses(new ArrayList<>());
        var course = new Course("math");
        lecture.setCourse(course);
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getAll()).thenReturn(new ArrayList<>());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        lecture.setGroups(List.of(new Group(), new Group(), new Group(), new Group()));

        lectureService.update(lecture);

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void whenDelete_thenStartedLectureUpdateDelete() {
        lectureService.delete(1);

        verify(lectureDao).delete(1);
    }
}