package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ValidationException;
import edu.shmonin.university.model.Lecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LectureService implements EntityService<Lecture> {

    private static final Logger log = LoggerFactory.getLogger(LectureService.class);

    @Value("${university.lecture.max.groups}")
    private int maxGroups;

    private final LectureDao jdbcLectureDao;
    private final HolidayDao jdbcHolidayDao;

    public LectureService(LectureDao jdbcLectureDao, HolidayDao jdbcHolidayDao) {
        this.jdbcLectureDao = jdbcLectureDao;
        this.jdbcHolidayDao = jdbcHolidayDao;
    }

    @Override
    public Lecture get(int lectureId) {
        try {
            log.debug("Get lecture with id={}", lectureId);
            return jdbcLectureDao.get(lectureId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the lecture. There is no lecture with id=" + lectureId);
        }
    }

    @Override
    public List<Lecture> getAll() {
        log.debug("Get all lectures");
        return jdbcLectureDao.getAll();
    }

    @Override
    public void create(Lecture lecture) {
        validateLecture(lecture);
        log.debug("Create lecture {}", lecture);
        jdbcLectureDao.create(lecture);
    }

    @Override
    public void update(Lecture lecture) {
        validateLecture(lecture);
        log.debug("Update lecture {}", lecture);
        jdbcLectureDao.update(lecture);
    }

    @Override
    public void delete(int lectureId) {
        try {
            jdbcLectureDao.get(lectureId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the lecture. There is no lecture with id=" + lectureId);
        }
        jdbcLectureDao.delete(lectureId);
    }

    private void validateLecture(Lecture lecture) {
        validateDateOfLecture(lecture);
        validateVacationsForLecture(lecture);
        validateHolidaysForLecture(lecture);
        validateTeacherCourses(lecture);
        validateMaxGroups(lecture);
        validateAudienceBusyness(lecture);
        validateTeacherBusyness(lecture);
    }

    private void validateDateOfLecture(Lecture lecture) {
        if (lecture.getDate().isBefore(LocalDate.now())) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Lecture's date can not be earlier than the current time");
        }
    }

    private void validateVacationsForLecture(Lecture lecture) {
        if (lecture.getTeacher().getVacations().stream().anyMatch(p -> p.getStartDate().isBefore(lecture.getDate()) && p.getEndDate().isAfter(lecture.getDate()))) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Lecture's date is on the vacation of teacher");
        }
    }

    private void validateHolidaysForLecture(Lecture lecture) {
        if (jdbcHolidayDao.getAll().stream().anyMatch(p -> p.getDate().isEqual(lecture.getDate()))) {
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
        if (jdbcLectureDao.getByAudienceId(lecture.getAudience().getId()).stream().
                anyMatch(p -> p.getDate().isEqual(lecture.getDate()) &&
                              p.getDuration().equals(lecture.getDuration()))) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Audience is busy on the lecture's date");
        }
    }

    private void validateTeacherBusyness(Lecture lecture) {
        if (jdbcLectureDao.getByTeacherId(lecture.getTeacher().getId()).stream().
                anyMatch(p -> p.getDate().isEqual(lecture.getDate()) &&
                              p.getDuration().equals(lecture.getDuration()))) {
            throw new ValidationException("The lecture " + lecture + " did not pass the validity check. Teacher is busy on the lecture's date");
        }
    }
}