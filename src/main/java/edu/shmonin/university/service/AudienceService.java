package edu.shmonin.university.service;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AudienceService implements EntityService<Audience> {

    @Value("${university.audiences.max.room.number}")
    private int audiencesMaxRoomNumber;

    @Value("${university.audience.capacity.max}")
    private int audienceMaxCapacity;

    private final AudienceDao jdbcAudienceDao;
    private final LectureDao jdbcLectureDao;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String GET_ERROR_MESSAGE = "Audience was not got";
    private static final String GET_ALL_ERROR_MESSAGE = "Audiences were not got";
    private static final String CREATE_ERROR_MESSAGE = "Audience was not created";
    private static final String UPDATE_ERROR_MESSAGE = "Audience was not updated";
    private static final String DELETE_ERROR_MESSAGE = "Audience was not deleted";

    public AudienceService(AudienceDao jdbcAudienceDao, LectureDao jdbcLectureDao) {
        this.jdbcAudienceDao = jdbcAudienceDao;
        this.jdbcLectureDao = jdbcLectureDao;
    }

    @Override
    public Audience get(int audienceId) {
        try {
            return jdbcAudienceDao.get(audienceId);
        } catch (Exception e) {
            logger.error(GET_ERROR_MESSAGE);
            throw new EntityException(GET_ERROR_MESSAGE);
        }
    }

    @Override
    public List<Audience> getAll() {
        try {
            return jdbcAudienceDao.getAll();
        } catch (Exception e) {
            logger.error(GET_ALL_ERROR_MESSAGE);
            throw new EntityException(GET_ALL_ERROR_MESSAGE);
        }
    }

    @Override
    public void create(Audience audience) {
        if (validateAudience(audience)) {
            jdbcAudienceDao.create(audience);
        } else {
            logger.error(CREATE_ERROR_MESSAGE);
            throw new EntityException(CREATE_ERROR_MESSAGE);
        }
    }

    @Override
    public void update(Audience audience) {
        if (validateAudience(audience)) {
            jdbcAudienceDao.update(audience);
        } else {
            logger.error(UPDATE_ERROR_MESSAGE);
            throw new EntityException(UPDATE_ERROR_MESSAGE);
        }
    }

    @Override
    public void delete(int audienceId) {
        if (jdbcLectureDao.getByAudienceId(audienceId).isEmpty()) {
            jdbcAudienceDao.delete(audienceId);
        } else {
            logger.error(DELETE_ERROR_MESSAGE);
            throw new EntityException(DELETE_ERROR_MESSAGE);
        }
    }

    private boolean validateAudience(Audience audience) {
        return audience.getRoomNumber() > 0 && audience.getRoomNumber() <= audiencesMaxRoomNumber &&
               audience.getCapacity() > 0 && audience.getCapacity() <= audienceMaxCapacity;
    }
}