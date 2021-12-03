package edu.shmonin.university.service;

import edu.shmonin.university.dao.DurationDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ChainedEntityException;
import edu.shmonin.university.model.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.util.List;

@Service
public class DurationService implements EntityService<Duration> {

    private static final Logger log = LoggerFactory.getLogger(DurationService.class);

    private final DurationDao durationDao;
    private final LectureDao lectureDao;

    public DurationService(DurationDao durationDao, LectureDao lectureDao) {
        this.durationDao = durationDao;
        this.lectureDao = lectureDao;
    }

    @Override
    public Duration get(int durationId) {
        var duration = durationDao.get(durationId);
        if (duration.isEmpty()) {
            throw new EntityNotFoundException("Can not find duration by id=" + durationId);
        }
        log.debug("Get duration with id={}", durationId);
        return duration.get();
    }

    @Override
    public List<Duration> getAll() {
        log.debug("Get all durations");
        return durationDao.getAll();
    }

    @Override
    public void create(Duration duration) {
        validateDuration(duration);
        log.debug("Create duration {}", duration);
        durationDao.create(duration);
    }

    @Override
    public void update(Duration duration) {
        validateDuration(duration);
        log.debug("Update duration {}", duration);
        durationDao.update(duration);
    }

    @Override
    public void delete(int durationId) {
        if (durationDao.get(durationId).isEmpty()) {
            throw new EntityNotFoundException("Can not find duration by id=" + durationId);
        }
        if (!lectureDao.getByDurationId(durationId).isEmpty()) {
            throw new ChainedEntityException("Can not delete duration by id=" + durationId + ", there are entities with this duration in the system");
        }
        durationDao.delete(durationId);
    }

    private void validateDuration(Duration duration) {
        if (!duration.getStartTime().isBefore(duration.getEndTime())) {
            throw new DateTimeException("The duration " + duration + " did not pass the validity check. Duration end time must be after start time");
        }
    }
}