package edu.shmonin.university.service;

import edu.shmonin.university.dao.DurationDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.LinkedEntityException;
import edu.shmonin.university.exception.ValidationException;
import edu.shmonin.university.model.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DurationService implements EntityService<Duration> {

    private static final Logger log = LoggerFactory.getLogger(DurationService.class);

    private final DurationDao jdbcDurationDao;
    private final LectureDao jdbcLectureDao;

    public DurationService(DurationDao jdbcDurationDao, LectureDao jdbcLectureDao) {
        this.jdbcDurationDao = jdbcDurationDao;
        this.jdbcLectureDao = jdbcLectureDao;
    }

    @Override
    public Duration get(int durationId) {
        try {
            log.debug("Get duration with id={}", durationId);
            return jdbcDurationDao.get(durationId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the duration. There is no duration with id=" + durationId);
        }
    }

    @Override
    public List<Duration> getAll() {
        log.debug("Get all durations");
        return jdbcDurationDao.getAll();
    }

    @Override
    public void create(Duration duration) {
        validateDuration(duration);
        log.debug("Create duration {}", duration);
        jdbcDurationDao.create(duration);
    }

    @Override
    public void update(Duration duration) {
        validateDuration(duration);
        log.debug("Update duration {}", duration);
        jdbcDurationDao.update(duration);
    }

    @Override
    public void delete(int durationId) {
        try {
            jdbcDurationDao.get(durationId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not delete the duration. There is no duration with id=" + durationId);
        }
        if (!jdbcLectureDao.getByDurationId(durationId).isEmpty()) {
            throw new LinkedEntityException("Can not delete duration with id=" + durationId + ", there are lectures with this duration in database");
        }
        jdbcDurationDao.delete(durationId);
    }

    private void validateDuration(Duration duration) {
        if (!duration.getStartTime().isBefore(duration.getEndTime())) {
            throw new ValidationException("The duration " + duration + " did not pass the validity check. Duration end time must be after start time");
        }
    }
}