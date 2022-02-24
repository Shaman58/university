package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.StudentDao;
import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.exception.*;
import edu.shmonin.university.model.Lecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LectureService {

    private static final Logger log = LoggerFactory.getLogger(LectureService.class);

    @Value("${university.lecture.max.groups}")
    private int maxGroups;

    private final LectureDao lectureDao;
    private final HolidayDao holidayDao;
    private final VacationDao vacationDao;
    private final StudentDao studentDao;

    public LectureService(LectureDao lectureDao, HolidayDao holidayDao, VacationDao vacationDao, StudentDao studentDao) {
        this.lectureDao = lectureDao;
        this.holidayDao = holidayDao;
        this.vacationDao = vacationDao;
        this.studentDao = studentDao;
    }

    public Lecture get(int lectureId) {
        log.debug("Get lecture with id={}", lectureId);
        return lectureDao.get(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find lecture by id=" + lectureId));
    }

    public List<Lecture> getAll() {
        log.debug("Get all lectures");
        return lectureDao.getAll();
    }

    public Page<Lecture> getAll(Pageable pageable) {
        log.debug("Get all sorted lectures");
        return lectureDao.getAll(pageable);
    }

    public Page<Lecture> getByGroupIdAndPeriod(Pageable pageable, int groupId, LocalDate startDate, LocalDate endDate) {
        log.debug("Get lectures with groupId = {} and current academic year", groupId);
        return lectureDao.getByGroupIdAndPeriod(pageable, groupId, startDate, endDate);
    }

    public Page<Lecture> getByTeacherIdAndPeriod(Pageable pageable, int teacherId, LocalDate startDate, LocalDate endDate) {
        log.debug("Get lectures with teacherId = {} and current academic year", teacherId);
        return lectureDao.getByTeacherIdAndPeriod(pageable, teacherId, startDate, endDate);
    }

    public void create(Lecture lecture) {
        log.debug("Create lecture {}", lecture);
        validateLecture(lecture);
        lectureDao.create(lecture);
    }

    public void update(Lecture lecture) {
        log.debug("Update lecture {}", lecture);
        validateLecture(lecture);
        lectureDao.update(lecture);
    }

    public void delete(int lectureId) {
        log.debug("Delete lecture by id={}", lectureId);
        this.get(lectureId);
        lectureDao.delete(lectureId);
    }

    private void validateLecture(Lecture lecture) {
        validateDateOfLecture(lecture);
        validateAudienceBusyness(lecture);
        validateHolidaysForLecture(lecture);
        validateVacationsForLecture(lecture);
        validateTeacherBusyness(lecture);
        validateTeacherCourses(lecture);
        validateMaxGroups(lecture);
        validateGroupsBusyness(lecture);
        validateAudienceStudentCapacity(lecture);
    }

    private void validateDateOfLecture(Lecture lecture) {
        if (lecture.getDate().isBefore(LocalDate.now())) {
            throw new DateNotAvailableException("Lecture's date " + lecture.getDate() + " can not be earlier than the current time");
        }
    }

    private void validateVacationsForLecture(Lecture lecture) {
        if (vacationDao.getByTeacherIdAndDate(lecture.getTeacher().getId(), lecture.getDate()).isPresent()) {
            throw new TeacherNotAvailableException("Teacher is on vacation on date: " + lecture.getDate());
        }
    }

    private void validateHolidaysForLecture(Lecture lecture) {
        if (holidayDao.getByDate(lecture.getDate()).isPresent()) {
            throw new DateNotAvailableException("Lecture's date is on the holiday");
        }
    }

    private void validateTeacherCourses(Lecture lecture) {
        if (!lecture.getTeacher().getCourses().contains(lecture.getCourse())) {
            throw new TeacherNotAvailableException("The teacher " + lecture.getTeacher().getFirstName() + " " +
                                                   lecture.getTeacher().getLastName() +
                                                   " doesn't have the course: " + lecture.getCourse().getName());
        }
    }

    private void validateMaxGroups(Lecture lecture) {
        if (lecture.getGroups().size() > maxGroups) {
            throw new GroupLimitReachedException("The lecture can not contain more then " + maxGroups + " groups");
        }
    }

    private void validateAudienceBusyness(Lecture lecture) {
        if (lectureDao.getByAudienceIdAndDateAndDurationId(lecture.getAudience().getId(), lecture.getDate(), lecture.getDuration().getId()).isPresent()) {
            throw new AudienceNotAvailableException("Audience is busy on the lecture's date");
        }
    }

    private void validateTeacherBusyness(Lecture lecture) {
        if (lectureDao.getByTeacherIdAndDateAndDurationId(lecture.getTeacher().getId(), lecture.getDate(), lecture.getDuration().getId()).isPresent()) {
            throw new TeacherNotAvailableException("Teacher is busy on the lecture's date");
        }
    }

    private void validateGroupsBusyness(Lecture lecture) {
        if (lecture.getGroups().stream().anyMatch((p -> lectureDao.getByGroupIdAndDateAndDurationId(p.getId(), lecture.getDate(), lecture.getDuration().getId()).isPresent()))) {
            throw new GroupNotAvailableException("One or more groups are busy on the lecture's date");
        }
    }

    private void validateAudienceStudentCapacity(Lecture lecture) {
        lecture.getGroups().forEach(p -> p.setStudents(studentDao.getByGroupId(p.getId())));
        if (lecture.getGroups().stream().mapToInt(g -> g.getStudents().size()).sum() > lecture.getAudience().getCapacity()) {
            throw new InvalidCapacityException("Audience " + lecture.getAudience().getRoomNumber() +
                                               "with capacity " + lecture.getAudience().getCapacity() +
                                               " can not accommodate all students");
        }
    }
}