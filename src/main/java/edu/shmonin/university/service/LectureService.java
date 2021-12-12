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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LectureService implements EntityService<Lecture> {

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

    @Override
    public Lecture get(int lectureId) {
        log.debug("Get lecture with id={}", lectureId);
        return lectureDao.get(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find lecture by id=" + lectureId));
    }

    @Override
    public List<Lecture> getAll() {
        log.debug("Get all lectures");
        return lectureDao.getAll();
    }

    @Override
    public void create(Lecture lecture) {
        log.debug("Create lecture {}", lecture);
        validateLecture(lecture);
        lectureDao.create(lecture);
    }

    @Override
    public void update(Lecture lecture) {
        log.debug("Update lecture {}", lecture);
        validateLecture(lecture);
        lectureDao.update(lecture);
    }

    @Override
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
            throw new DateNotAvailableException("Lecture's date can not be earlier than the current time");
        }
    }

    private void validateVacationsForLecture(Lecture lecture) {
        if (vacationDao.getByTeacherIdAndDate(lecture.getTeacher().getId(), lecture.getDate()).isPresent()) {
            throw new TeacherNotAvailableException("Lecture's date is on the vacation of teacher");
        }
    }

    private void validateHolidaysForLecture(Lecture lecture) {
        if (holidayDao.getByDate(lecture.getDate()).isPresent()) {
            throw new DateNotAvailableException("Lecture's date is on the holiday");
        }
    }

    private void validateTeacherCourses(Lecture lecture) {
        if (lecture.getTeacher().getCourses().stream().noneMatch(lecture.getCourse()::equals)) {
            throw new TeacherNotAvailableException("The teacher doesn't have the right course");
        }
    }

    private void validateMaxGroups(Lecture lecture) {
        if (lecture.getGroups().size() > maxGroups) {
            throw new GroupLimitReachedException("Group limit reached");
        }
    }

    private void validateAudienceBusyness(Lecture lecture) {
        if (lectureDao.getByAudienceId(lecture.getAudience().getId()).stream().
                anyMatch(p -> p.getDate().isEqual(lecture.getDate()) &&
                              p.getDuration().equals(lecture.getDuration()))) {
            throw new AudienceNotAvailableException("Audience is busy on the lecture's date");
        }
    }

    private void validateTeacherBusyness(Lecture lecture) {
        if (lectureDao.getByTeacherId(lecture.getTeacher().getId()).stream().
                anyMatch(p -> p.getDate().isEqual(lecture.getDate()) &&
                              p.getDuration().equals(lecture.getDuration()))) {
            throw new TeacherNotAvailableException("Teacher is busy on the lecture's date");
        }
    }

    private void validateGroupsBusyness(Lecture lecture) {
        if (lecture.getGroups().stream().anyMatch((p -> lectureDao.getByGroupIdAndDateAndDuration(p.getId(), lecture.getDate(), lecture.getDuration().getId()).isPresent()))) {
            throw new GroupNotAvailableException("One or more groups are busy on the lecture's date");
        }
    }

    private void validateAudienceStudentCapacity(Lecture lecture) {
        lecture.getGroups().forEach(p -> p.setStudents(studentDao.getByGroupId(p.getId())));
        if (lecture.getGroups().stream().mapToInt(g -> g.getStudents().size()).sum() > lecture.getAudience().getCapacity()) {
            throw new InvalidCapacityException("Audience can not accommodate all students");
        }
    }
}