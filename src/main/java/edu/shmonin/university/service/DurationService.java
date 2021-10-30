package edu.shmonin.university.service;

import edu.shmonin.university.dao.DurationDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.Duration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DurationService implements EntityService<Duration> {

    private final DurationDao jdbcDurationDao;
    private final LectureDao jdbcLectureDao;

    public DurationService(DurationDao jdbcDurationDao, LectureDao jdbcLectureDao) {
        this.jdbcDurationDao = jdbcDurationDao;
        this.jdbcLectureDao = jdbcLectureDao;
    }

    @Override
    public Duration get(int durationId) {
        return jdbcDurationDao.get(durationId);
    }

    @Override
    public List<Duration> getAll() {
        return jdbcDurationDao.getAll();
    }

    @Override
    public void create(Duration duration) {
        if (validateDuration(duration)) {
            jdbcDurationDao.create(duration);
        }
    }

    @Override
    public void update(Duration duration) {
        if (validateDuration(duration)) {
            jdbcDurationDao.update(duration);
        }
    }
    @Override
    public void delete(int durationId) {
        if (jdbcLectureDao.getByDurationId(durationId).isEmpty()) {
            jdbcDurationDao.delete(durationId);
        }
    }

    private boolean validateDuration(Duration duration) {
        return duration.getStartTime().isBefore(duration.getEndTime());
    }
}