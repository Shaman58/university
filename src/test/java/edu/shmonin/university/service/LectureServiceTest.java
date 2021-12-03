package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.StudentDao;
import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.exception.EntityNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureDao lectureDao;
    @Mock
    private HolidayDao holidayDao;
    @Mock
    private VacationDao vacationDao;
    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private LectureService lectureService;

    @BeforeEach
    void setUp() {
        setField(lectureService, "maxGroups", 3);
    }

    @Test
    void givenId_whenGet_thenReturnedLecture() {
        var expected = new Lecture();
        when(lectureDao.get(1)).thenReturn(Optional.of(expected));

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
        var group = new Group("group");
        group.setId(1);
        var students = List.of(new Student(), new Student(), new Student());
        lecture.setGroups(List.of(group));
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
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());
        when(studentDao.getByGroupId(group.getId())).thenReturn(students);

        lectureService.create(lecture);

        verify(lectureDao).create(lecture);
    }

    @Test
    void givenOutOfDateLecture_whenCreate_thenThrowRuntimeExceptionAndNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().minus(1, ChronoUnit.DAYS));

        assertThrows(RuntimeException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureAndAudienceIsBusyness_whenCreate_thenThrowRuntimeExceptionAndNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(List.of(lecture));

        assertThrows(RuntimeException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureOnHoliday_whenCreate_thenThrowRuntimeExceptionAndNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.of(new Holiday()));

        assertThrows(RuntimeException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureOnTeacherVacation_whenCreate_thenThrowRuntimeExceptionAndNotStartedLectureDaoCreate() {
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
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.of(new Vacation()));

        assertThrows(RuntimeException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureAndTeacherIsBusyness_whenCreate_thenThrowRuntimeExceptionAndNotStartedLectureDaoCreate() {
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
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(List.of(lecture));
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureAndTeacherHasNotNeededCourse_whenCreate_thenThrowRuntimeExceptionAndNotStartedLectureDaoCreate() {
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
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureGroupsAreMoreThenAllowed_whenCreate_thenThrowRuntimeExceptionAndNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        var course = new Course("math");
        teacher.setCourses(List.of(course));
        lecture.setCourse(course);
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());
        lecture.setGroups(List.of(new Group(), new Group(), new Group(), new Group()));

        assertThrows(RuntimeException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
    }

    @Test
    void givenValidLectureAndGroupIsBusy_whenCreate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        var date = LocalDate.now().plus(1, ChronoUnit.DAYS);
        lecture.setDate(date);
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        duration.setId(1);
        lecture.setDuration(duration);
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        var course = new Course("math");
        lecture.setCourse(course);
        teacher.setCourses(List.of(course));
        lecture.setTeacher(teacher);
        var group = new Group("group1");
        group.setId(1);
        lecture.setGroups(List.of(group));
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateDuration(1, date, 1)).thenReturn(Optional.of(new Lecture()));

        assertThrows(RuntimeException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureAndGroupsStudentAreTooMany_whenCreate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        var date = LocalDate.now().plus(1, ChronoUnit.DAYS);
        lecture.setDate(date);
        var audience = new Audience(1, 5);
        audience.setId(1);
        lecture.setAudience(audience);
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        duration.setId(1);
        lecture.setDuration(duration);
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        var course = new Course("math");
        lecture.setCourse(course);
        teacher.setCourses(List.of(course));
        lecture.setTeacher(teacher);
        var group = new Group("group1");
        group.setId(1);
        lecture.setGroups(List.of(group));
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateDuration(1, date, 1)).thenReturn(Optional.empty());
        when(studentDao.getByGroupId(1)).thenReturn(List.of(new Student(), new Student(), new Student(), new Student(), new Student(), new Student()));

        assertThrows(RuntimeException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLecture_whenUpdate_thenStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var course = new Course("math");
        lecture.setCourse(course);
        var group = new Group("group");
        group.setId(1);
        var students = List.of(new Student(), new Student(), new Student());
        lecture.setGroups(List.of(group));
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
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());
        when(studentDao.getByGroupId(group.getId())).thenReturn(students);

        lectureService.update(lecture);

        verify(lectureDao).update(lecture);
    }

    @Test
    void givenOutOfDateLecture_whenUpdate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().minus(1, ChronoUnit.DAYS));

        assertThrows(RuntimeException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureAndAudienceIsBusyness_whenUpdate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(List.of(lecture));

        assertThrows(RuntimeException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureOnHoliday_whenUpdate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.of(new Holiday()));

        assertThrows(RuntimeException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureOnTeacherVacation_whenUpdate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
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
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.of(new Vacation()));

        assertThrows(RuntimeException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureAndTeacherIsBusyness_whenUpdate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
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
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(List.of(lecture));
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureAndTeacherHasNotNeededCourse_whenUpdate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
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
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureGroupsAreMoreThenAllowed_whenUpdate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        var course = new Course("math");
        teacher.setCourses(List.of(course));
        lecture.setCourse(course);
        lecture.setTeacher(teacher);
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());
        lecture.setGroups(List.of(new Group(), new Group(), new Group(), new Group()));

        assertThrows(RuntimeException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureAndGroupIsBusy_whenUpdate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        var date = LocalDate.now().plus(1, ChronoUnit.DAYS);
        lecture.setDate(date);
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        duration.setId(1);
        lecture.setDuration(duration);
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        var course = new Course("math");
        lecture.setCourse(course);
        teacher.setCourses(List.of(course));
        lecture.setTeacher(teacher);
        var group = new Group("group1");
        group.setId(1);
        lecture.setGroups(List.of(group));
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateDuration(1, date, 1)).thenReturn(Optional.of(new Lecture()));

        assertThrows(RuntimeException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenValidLectureAndGroupsStudentAreTooMany_whenUpdate_thenThrowRuntimeExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        var date = LocalDate.now().plus(1, ChronoUnit.DAYS);
        lecture.setDate(date);
        var audience = new Audience(1, 5);
        audience.setId(1);
        lecture.setAudience(audience);
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        duration.setId(1);
        lecture.setDuration(duration);
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        var course = new Course("math");
        lecture.setCourse(course);
        teacher.setCourses(List.of(course));
        lecture.setTeacher(teacher);
        var group = new Group("group1");
        group.setId(1);
        lecture.setGroups(List.of(group));
        when(lectureDao.getByAudienceId(audience.getId())).thenReturn(new ArrayList<>());
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherId(teacher.getId())).thenReturn(new ArrayList<>());
        when(vacationDao.getByTeacherAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateDuration(1, date, 1)).thenReturn(Optional.empty());
        when(studentDao.getByGroupId(1)).thenReturn(List.of(new Student(), new Student(), new Student(), new Student(), new Student(), new Student()));

        assertThrows(RuntimeException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
    }

    @Test
    void givenLectureDaoGetReturnNotEmptyOptional_whenDelete_thenStartedLectureUpdateDelete() {
        when(lectureDao.get(1)).thenReturn(Optional.of(new Lecture()));

        lectureService.delete(1);

        verify(lectureDao).delete(1);
    }

    @Test
    void givenLectureDaoGetReturnEmptyOptional_whenDelete_thenEntityNotFoundExceptionAndNotStartedLectureUpdateDelete() {
        when(lectureDao.get(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lectureService.delete(1));

        verify(lectureDao, never()).delete(1);
    }
}