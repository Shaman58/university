package edu.shmonin.university.service.implementation;

import edu.shmonin.university.dao.DurationDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ForeignReferenceException;
import edu.shmonin.university.exception.InvalidDurationException;
import edu.shmonin.university.model.Duration;
import edu.shmonin.university.service.DurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DurationServiceImpl implements DurationService {

    private static final Logger log = LoggerFactory.getLogger(DurationServiceImpl.class);

    private final DurationDao durationDao;
    private final LectureDao lectureDao;

    public DurationServiceImpl(DurationDao durationDao, LectureDao lectureDao) {
        this.durationDao = durationDao;
        this.lectureDao = lectureDao;
    }

    @Override
    public Duration get(int durationId) {
        log.debug("Get duration with id={}", durationId);
        var duration = durationDao.get(durationId);
        if (duration.isEmpty()) {
            throw new EntityNotFoundException("Can not find duration by id=" + durationId);
        }
        return duration.get();
    }

    @Override
    public List<Duration> getAll() {
        log.debug("Get all durations");
        return durationDao.getAll();
    }

    @Override
    public Page<Duration> getAll(Pageable pageable) {
        log.debug("Get all sorted durations");
        return durationDao.getAll(pageable);
    }

    @Override
    public void create(Duration duration) {
        log.debug("Create duration {}", duration);
        validateDuration(duration);
        durationDao.create(duration);
    }

    @Override
    public void update(Duration duration) {
        log.debug("Update duration {}", duration);
        validateDuration(duration);
        durationDao.update(duration);
    }

    @Override
    public void delete(int durationId) {
        log.debug("Delete duration by id={}", durationId);
        this.get(durationId);
        if (!lectureDao.getByDurationId(durationId).isEmpty()) {
            throw new ForeignReferenceException("There are lectures with this duration");
        }
        durationDao.delete(durationId);
    }

    private void validateDuration(Duration duration) {
        if (!duration.getStartTime().isBefore(duration.getEndTime())) {
            throw new InvalidDurationException("Duration end time must be after start time, but got startTime=" + duration.getStartTime() + ", endTime=" + duration.getEndTime());
        }
    }
}