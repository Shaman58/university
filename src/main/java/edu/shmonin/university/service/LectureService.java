package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.Lecture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService implements EntityService<Lecture> {

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
        return jdbcLectureDao.get(lectureId);
    }

    @Override
    public List<Lecture> getAll() {
        return jdbcLectureDao.getAll();
    }

    @Override
    public void create(Lecture lecture) {
        if (validateLecture(lecture)) {
            jdbcLectureDao.create(lecture);
        }
    }

    @Override
    public void update(Lecture lecture) {
        if (validateLecture(lecture)) {
            jdbcLectureDao.update(lecture);
        }
    }

    @Override
    public void delete(int lectureId) {
        jdbcLectureDao.delete(lectureId);
    }

    private boolean validateLecture(Lecture lecture) {
        return validateAudienceBusyness(lecture) &&
               validateHolidaysForLecture(lecture) &&
               validateVacationsForLecture(lecture) &&
               validateTeacherBusyness(lecture) &&
               validateTeacherCourses(lecture) &&
               validateMaxCourses(lecture);
    }

    private boolean validateVacationsForLecture(Lecture lecture) {
        return lecture.getTeacher().getVacations().stream().noneMatch(p -> p.getStartDate().isBefore(lecture.getDate()) && p.getEndDate().isAfter(lecture.getDate()));
    }

    private boolean validateHolidaysForLecture(Lecture lecture) {
        return jdbcHolidayDao.getAll().stream().noneMatch(p -> p.getDate().isEqual(lecture.getDate()));
    }

    private boolean validateTeacherCourses(Lecture lecture) {
        return lecture.getTeacher().getCourses().stream().anyMatch(lecture.getCourse()::equals);
    }

    private boolean validateMaxCourses(Lecture lecture) {
        return lecture.getGroups().size() <= maxGroups;
    }

    private boolean validateAudienceBusyness(Lecture lecture) {
        return jdbcLectureDao.getByAudienceId(lecture.getAudience().getId()).stream().
                noneMatch(p -> p.getDate().isEqual(lecture.getDate()) &&
                               p.getDuration().equals(lecture.getDuration()));
    }

    private boolean validateTeacherBusyness(Lecture lecture) {
        return jdbcLectureDao.getByTeacherId(lecture.getTeacher().getId()).stream().
                noneMatch(p -> p.getDate().isEqual(lecture.getDate()) &&
                               p.getDuration().equals(lecture.getDuration()));
    }
}