package edu.shmonin.university.service;

import edu.shmonin.university.dao.*;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ValidationException;
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
        var lecture = lectureDao.get(lectureId);
        if (lecture.isEmpty()) {
            throw new EntityNotFoundException("Can not find the lecture. There is no lecture with id=" + lectureId);
        }
        log.debug("Get lecture with id={}", lectureId);
        return lecture.get();

    }

    @Override
    public List<Lecture> getAll() {
        log.debug("Get all lectures");
        return lectureDao.getAll();
    }

    @Override
    public void create(Lecture lecture) {
        validateLecture(lecture);
        log.debug("Create lecture {}", lecture);
        lectureDao.create(lecture);
    }

    @Override
    public void update(Lecture lecture) {
        validateLecture(lecture);
        log.debug("Update lecture {}", lecture);
        lectureDao.update(lecture);
    }

    @Override
    public void delete(int lectureId) {
        if (lectureDao.get(lectureId).isEmpty()) {
            throw new EntityNotFoundException("Can not find the lecture. There is no lecture with id=" + lectureId);
        }
        log.debug("Delete lecture by id={}", lectureId);
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
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Lecture's date can not be earlier than the current time");
        }
    }

    private void validateVacationsForLecture(Lecture lecture) {
        if (vacationDao.getByTeacherAndDate(lecture.getTeacher().getId(), lecture.getDate()).isPresent()) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Lecture's date is on the vacation of teacher");
        }
    }

    private void validateHolidaysForLecture(Lecture lecture) {
        if (holidayDao.getByDate(lecture.getDate()).isPresent()) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Lecture's date is on the holiday");
        }
    }

    private void validateTeacherCourses(Lecture lecture) {
        if (lecture.getTeacher().getCourses().stream().noneMatch(lecture.getCourse()::equals)) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Lecture's teacher has not needed course");
        }
    }

    private void validateMaxGroups(Lecture lecture) {
        if (lecture.getGroups().size() > maxGroups) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Group limit exceeded");
        }
    }

    private void validateAudienceBusyness(Lecture lecture) {
        if (lectureDao.getByAudienceId(lecture.getAudience().getId()).stream().
                anyMatch(p -> p.getDate().isEqual(lecture.getDate()) &&
                              p.getDuration().equals(lecture.getDuration()))) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Audience is busy on the lecture's date");
        }
    }

    private void validateTeacherBusyness(Lecture lecture) {
        if (lectureDao.getByTeacherId(lecture.getTeacher().getId()).stream().
                anyMatch(p -> p.getDate().isEqual(lecture.getDate()) &&
                              p.getDuration().equals(lecture.getDuration()))) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Teacher is busy on the lecture's date");
        }
    }

    private void validateGroupsBusyness(Lecture lecture) {
        if (lecture.getGroups().stream().anyMatch((p -> lectureDao.getByGroupDateDuration(p.getId(), lecture.getDate(), lecture.getDuration().getId()).isPresent()))) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. One or more groups are busy on the lecture's date");
        }
    }

    private void validateAudienceStudentCapacity(Lecture lecture) {
        lecture.getGroups().forEach(p -> p.setStudents(studentDao.getByGroupId(p.getId())));
        if (lecture.getGroups().stream().mapToInt(g -> g.getStudents().size()).sum() > lecture.getAudience().getCapacity()) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Audience can not accommodate all students");
        }
    }
}