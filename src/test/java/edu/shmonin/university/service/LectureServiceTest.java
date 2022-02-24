package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.StudentDao;
import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.exception.*;
import edu.shmonin.university.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
    void givenPageRequest_whenGetAll_thenReturnPageOfLectures() {
        var pageRequest = PageRequest.of(0, 20);
        var lectures = List.of(new Lecture());
        var expected = new PageImpl<>(lectures, pageRequest, 1);
        when(lectureDao.getAll(pageRequest)).thenReturn(expected);

        var actual = lectureService.getAll(pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void givenGroupIdAndPageRequest_whenGetByGroupIdAndAcademicYear_thenReturnedPageOfLectures() {
        var pageRequest = PageRequest.of(0, 20);
        var startDate = LocalDate.of(2021, 9, 1);
        var endDate = LocalDate.of(2021, 10, 1);
        var expected = new PageImpl<>(new ArrayList<Lecture>(), pageRequest, 1);
        when(lectureDao.getByGroupIdAndPeriod(pageRequest, 1, startDate, endDate)).thenReturn(expected);

        var actual = lectureService.getByGroupIdAndPeriod(pageRequest, 1, startDate, endDate);

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherIdAndPageRequest_whenGetByTeacherIdAndAcademicYear_thenReturnedPageOfLectures() {
        var pageRequest = PageRequest.of(0, 20);
        var startDate = LocalDate.of(2021, 9, 1);
        var endDate = LocalDate.of(2021, 10, 1);
        var expected = new PageImpl<>(new ArrayList<Lecture>(), pageRequest, 1);
        when(lectureDao.getByTeacherIdAndPeriod(pageRequest, 1, startDate, endDate)).thenReturn(expected);

        var actual = lectureService.getByTeacherIdAndPeriod(pageRequest, 1, startDate, endDate);

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
        lecture.setGroups(List.of(group));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        teacher.setCourses(List.of(course));
        lecture.setTeacher(teacher);

        lectureService.create(lecture);

        verify(lectureDao).create(lecture);
    }

    @Test
    void givenOutOfDateLecture_whenCreate_thenThrowDateNotAvailableExceptionAndNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().minus(1, ChronoUnit.DAYS));

        var exception = assertThrows(DateNotAvailableException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
        assertEquals("Lecture's date " + lecture.getDate() + " can not be earlier than the current time", exception.getMessage());
    }

    @Test
    void givenValidLectureAndAudienceIsBusyness_whenCreate_thenThrowAudienceNotAvailableExceptionAndNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        duration.setId(1);
        lecture.setDuration(duration);
        when(lectureDao.getByAudienceIdAndDateAndDurationId(audience.getId(), lecture.getDate(), duration.getId()))
                .thenReturn(Optional.of(new Lecture()));

        var exception = assertThrows(AudienceNotAvailableException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
        assertEquals("Audience is busy on the lecture's date", exception.getMessage());
    }

    @Test
    void givenValidLectureOnHoliday_whenCreate_thenThrowDateNotAvailableExceptionAndNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.of(new Holiday()));

        var exception = assertThrows(DateNotAvailableException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
        assertEquals("Lecture's date is on the holiday", exception.getMessage());
    }

    @Test
    void givenValidLectureOnTeacherVacation_whenCreate_thenThrowTeacherNotAvailableExceptionAndNotStartedLectureDaoCreate() {
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
        when(vacationDao.getByTeacherIdAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.of(new Vacation()));

        var exception = assertThrows(TeacherNotAvailableException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
        assertEquals("Teacher is on vacation on date: " + lecture.getDate(), exception.getMessage());
    }

    @Test
    void givenValidLectureAndTeacherIsBusyness_whenCreate_thenThrowTeacherNotAvailableExceptionAndNotStartedLectureDaoCreate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        duration.setId(1);
        lecture.setDuration(duration);
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        lecture.setTeacher(teacher);
        when(lectureDao.getByTeacherIdAndDateAndDurationId(teacher.getId(), lecture.getDate(), duration.getId()))
                .thenReturn(Optional.of(new Lecture()));

        var exception = assertThrows(TeacherNotAvailableException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
        assertEquals("Teacher is busy on the lecture's date", exception.getMessage());
    }

    @Test
    void givenValidLectureAndTeacherHasNotNeededCourse_whenCreate_thenThrowTeacherNotAvailableExceptionAndNotStartedLectureDaoCreate() {
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

        var exception = assertThrows(TeacherNotAvailableException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
        assertEquals("The teacher " + lecture.getTeacher().getFirstName() + " " +
                     lecture.getTeacher().getLastName() +
                     " doesn't have the course: " + lecture.getCourse().getName(), exception.getMessage());
    }

    @Test
    void givenValidLectureGroupsAreMoreThenAllowed_whenCreate_thenThrowGroupLimitReachedExceptionAndNotStartedLectureDaoCreate() {
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
        lecture.setGroups(List.of(new Group(), new Group(), new Group(), new Group()));

        var exception = assertThrows(GroupLimitReachedException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
        assertEquals("The lecture can not contain more then 3 groups", exception.getMessage());
    }

    @Test
    void givenValidLectureAndGroupIsBusy_whenCreate_thenThrowGroupNotAvailableExceptionAndNotStartedLectureDaoUpdate() {
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
        when(lectureDao.getByGroupIdAndDateAndDurationId(1, date, 1)).thenReturn(Optional.of(new Lecture()));

        var exception = assertThrows(GroupNotAvailableException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).update(lecture);
        assertEquals("One or more groups are busy on the lecture's date", exception.getMessage());
    }

    @Test
    void givenValidLectureAndGroupsStudentsAreTooMany_whenCreate_thenThrowInvalidCapacityExceptionAndNotStartedLectureDaoUpdate() {
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
        when(studentDao.getByGroupId(1)).thenReturn(List.of(new Student(), new Student(), new Student(), new Student(), new Student(), new Student()));

        var exception = assertThrows(InvalidCapacityException.class, () -> lectureService.create(lecture));

        verify(lectureDao, never()).create(lecture);
        assertEquals("Audience " + audience.getRoomNumber() +
                     "with capacity " + audience.getCapacity() +
                     " can not accommodate all students", exception.getMessage());
    }

    @Test
    void givenValidLecture_whenUpdate_thenStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var course = new Course("math");
        lecture.setCourse(course);
        var group = new Group("group");
        group.setId(1);
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

        lectureService.update(lecture);

        verify(lectureDao).update(lecture);
    }

    @Test
    void givenOutOfDateLecture_whenUpdate_thenThrowDateNotAvailableExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().minus(1, ChronoUnit.DAYS));

        var exception = assertThrows(DateNotAvailableException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
        assertEquals("Lecture's date " + lecture.getDate() + " can not be earlier than the current time", exception.getMessage());
    }

    @Test
    void givenValidLectureAndAudienceIsBusyness_whenUpdate_thenThrowAudienceNotAvailableExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        duration.setId(1);
        lecture.setDuration(duration);
        when(lectureDao.getByAudienceIdAndDateAndDurationId(lecture.getAudience().getId(), lecture.getDate(), lecture.getDuration().getId()))
                .thenReturn(Optional.of(new Lecture()));

        var exception = assertThrows(AudienceNotAvailableException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
        assertEquals("Audience is busy on the lecture's date", exception.getMessage());
    }

    @Test
    void givenValidLectureOnHoliday_whenUpdate_thenThrowDateNotAvailableExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        lecture.setDuration(new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        when(holidayDao.getByDate(lecture.getDate())).thenReturn(Optional.of(new Holiday()));

        var exception = assertThrows(DateNotAvailableException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
        assertEquals("Lecture's date is on the holiday", exception.getMessage());
    }

    @Test
    void givenValidLectureOnTeacherVacation_whenUpdate_thenThrowTeacherNotAvailableExceptionAndNotStartedLectureDaoUpdate() {
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
        when(vacationDao.getByTeacherIdAndDate(teacher.getId(), lecture.getDate())).thenReturn(Optional.of(new Vacation()));

        var exception = assertThrows(TeacherNotAvailableException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
        assertEquals("Teacher is on vacation on date: " + lecture.getDate(), exception.getMessage());
    }

    @Test
    void givenValidLectureAndTeacherIsBusyness_whenUpdate_thenThrowTeacherNotAvailableExceptionAndNotStartedLectureDaoUpdate() {
        var lecture = new Lecture();
        lecture.setDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        var audience = new Audience(1, 60);
        audience.setId(1);
        lecture.setAudience(audience);
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        duration.setId(1);
        lecture.setDuration(duration);
        var teacher = new Teacher();
        teacher.setId(1);
        teacher.setVacations(new ArrayList<>());
        lecture.setTeacher(teacher);
        when(lectureDao.getByTeacherIdAndDateAndDurationId(teacher.getId(), lecture.getDate(), lecture.getDuration().getId()))
                .thenReturn(Optional.of(new Lecture()));

        var exception = assertThrows(TeacherNotAvailableException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
        assertEquals("Teacher is busy on the lecture's date", exception.getMessage());
    }

    @Test
    void givenValidLectureAndTeacherHasNotNeededCourse_whenUpdate_thenThrowTeacherNotAvailableExceptionAndNotStartedLectureDaoUpdate() {
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

        var exception = assertThrows(TeacherNotAvailableException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
        assertEquals("The teacher " + lecture.getTeacher().getFirstName() + " " +
                     lecture.getTeacher().getLastName() +
                     " doesn't have the course: " + lecture.getCourse().getName(), exception.getMessage());
    }

    @Test
    void givenValidLectureGroupsAreMoreThenAllowed_whenUpdate_thenThrowGroupLimitReachedExceptionAndNotStartedLectureDaoUpdate() {
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
        lecture.setGroups(List.of(new Group(), new Group(), new Group(), new Group()));

        var exception = assertThrows(GroupLimitReachedException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
        assertEquals("The lecture can not contain more then 3 groups", exception.getMessage());
    }

    @Test
    void givenValidLectureAndGroupIsBusy_whenUpdate_thenThrowGroupNotAvailableExceptionAndNotStartedLectureDaoUpdate() {
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
        when(lectureDao.getByGroupIdAndDateAndDurationId(1, date, 1)).thenReturn(Optional.of(new Lecture()));

        var exception = assertThrows(GroupNotAvailableException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
        assertEquals("One or more groups are busy on the lecture's date", exception.getMessage());
    }

    @Test
    void givenValidLectureAndGroupsStudentsAreTooMany_whenUpdate_thenThrowInvalidCapacityExceptionAndNotStartedLectureDaoUpdate() {
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
        when(studentDao.getByGroupId(1)).thenReturn(List.of(new Student(), new Student(), new Student(), new Student(), new Student(), new Student()));

        var exception = assertThrows(InvalidCapacityException.class, () -> lectureService.update(lecture));

        verify(lectureDao, never()).update(lecture);
        assertEquals("Audience " + audience.getRoomNumber() +
                     "with capacity " + audience.getCapacity() +
                     " can not accommodate all students", exception.getMessage());
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

        var exception = assertThrows(EntityNotFoundException.class, () -> lectureService.delete(1));

        verify(lectureDao, never()).delete(1);
        assertEquals("Can not find lecture by id=1", exception.getMessage());
    }
}