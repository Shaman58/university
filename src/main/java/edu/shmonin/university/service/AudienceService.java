package edu.shmonin.university.service;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityException;
import edu.shmonin.university.model.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AudienceService implements EntityService<Audience> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudienceService.class);

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
        var audience = jdbcAudienceDao.get(audienceId);
        LOGGER.debug("The audience has been got");
        return audience;
    }

    @Override
    public List<Audience> getAll() {
        var audiences = jdbcAudienceDao.getAll();
        LOGGER.debug("The audiences has been got");
        return audiences;
    }

    @Override
    public void create(Audience audience) {
        if (validateAudience(audience)) {
            jdbcAudienceDao.create(audience);
        }
    }

    @Override
    public void update(Audience audience) {
        if (validateAudience(audience)) {
            jdbcAudienceDao.update(audience);
        }
    }

    @Override
    public void delete(int audienceId) {
        if (jdbcLectureDao.getByAudienceId(audienceId).isEmpty()) {
            jdbcAudienceDao.delete(audienceId);
        } else {
            throw new EntityException("Can not delete audience with id=" + audienceId + ", there are links to the audience in database");
        }
    }

    private boolean validateAudience(Audience audience) {
        if (audience.getRoomNumber() > 0 && audience.getRoomNumber() <= audiencesMaxRoomNumber &&
            audience.getCapacity() > 0 && audience.getCapacity() <= audienceMaxCapacity) {
            return true;
        } else {
            throw new EntityException("The audience " + audience + " did not pass the validity check");
        }
    }
}