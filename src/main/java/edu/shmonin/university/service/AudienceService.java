package edu.shmonin.university.service;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.AudienceDeleteException;
import edu.shmonin.university.exception.ValidationException;
import edu.shmonin.university.model.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AudienceService implements EntityService<Audience> {

    private static final Logger LOG = LoggerFactory.getLogger(AudienceService.class);

    @Value("${university.audiences.max.room.number}")
    private int audiencesMaxRoomNumber;

    @Value("${university.audience.capacity.max}")
    private int audienceMaxCapacity;

    private final AudienceDao jdbcAudienceDao;
    private final LectureDao jdbcLectureDao;

    public AudienceService(AudienceDao jdbcAudienceDao, LectureDao jdbcLectureDao) {
        this.jdbcAudienceDao = jdbcAudienceDao;
        this.jdbcLectureDao = jdbcLectureDao;
    }

    @Override
    public Audience get(int audienceId) {
        LOG.debug("The audience with id {} has been got", audienceId);
        return jdbcAudienceDao.get(audienceId);
    }

    @Override
    public List<Audience> getAll() {
        LOG.debug("The audiences has been got");
        return jdbcAudienceDao.getAll();
    }

    @Override
    public void create(Audience audience) {
        try {
            validateAudience(audience);
            LOG.debug("Audience {} has been created", audience);
            jdbcAudienceDao.create(audience);
        } catch (ValidationException e) {
            LOG.error("Audience has not created.", e);
        }
    }

    @Override
    public void update(Audience audience) {
        try {
            validateAudience(audience);
            LOG.debug("Audience {} has been updated", audience);
            jdbcAudienceDao.update(audience);
        } catch (ValidationException e) {
            LOG.error("Audience has not updated.", e);
        }
    }

    @Override
    public void delete(int audienceId) {
        if (jdbcLectureDao.getByAudienceId(audienceId).isEmpty()) {
            LOG.debug("Audience with id={} has been deleted", audienceId);
            jdbcAudienceDao.delete(audienceId);
        } else {
            throw new AudienceDeleteException("Can not delete audience with id=" + audienceId + ", there are links to the audience in database");
        }
    }

    private void validateAudience(Audience audience) {
        if (!(audience.getRoomNumber() > 0 && audience.getRoomNumber() <= audiencesMaxRoomNumber &&
              audience.getCapacity() > 0 && audience.getCapacity() <= audienceMaxCapacity)) {
            throw new ValidationException("The audience " + audience + " did not pass the validity check");
        }
    }
}