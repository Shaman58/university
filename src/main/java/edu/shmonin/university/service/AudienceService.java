package edu.shmonin.university.service;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.Audience;
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
    private static final String GET_ERROR_MESSAGE = "Audience was not got";
    private static final String GET_ALL_ERROR_MESSAGE = "Audiences were not been got";
    private static final String CREATE_ERROR_MESSAGE = "The audience has not been not created";
    private static final String UPDATE_ERROR_MESSAGE = "The audience has not been updated";
    private static final String DELETE_ERROR_MESSAGE = "The audience has not been deleted";

    public AudienceService(AudienceDao jdbcAudienceDao, LectureDao jdbcLectureDao) {
        this.jdbcAudienceDao = jdbcAudienceDao;
        this.jdbcLectureDao = jdbcLectureDao;
    }

    @Override
    public Audience get(int audienceId) {
        try {
            return jdbcAudienceDao.get(audienceId);
        } catch (Exception e) {
            throw new EntityException(GET_ERROR_MESSAGE);
        }
    }

    @Override
    public List<Audience> getAll() {
        try {
            return jdbcAudienceDao.getAll();
        } catch (Exception e) {
            throw new EntityException(GET_ALL_ERROR_MESSAGE);
        }
    }

    @Override
    public void create(Audience audience) {
        if (validateAudience(audience)) {
            jdbcAudienceDao.create(audience);
        } else {
            throw new EntityException(CREATE_ERROR_MESSAGE);
        }
    }

    @Override
    public void update(Audience audience) {
        if (validateAudience(audience)) {
            jdbcAudienceDao.update(audience);
        } else {
            throw new EntityException(UPDATE_ERROR_MESSAGE);
        }
    }

    @Override
    public void delete(int audienceId) {
        if (jdbcLectureDao.getByAudienceId(audienceId).isEmpty()) {
            jdbcAudienceDao.delete(audienceId);
        } else {
            throw new EntityException(DELETE_ERROR_MESSAGE);
        }
    }

    private boolean validateAudience(Audience audience) {
        return audience.getRoomNumber() > 0 && audience.getRoomNumber() <= audiencesMaxRoomNumber &&
               audience.getCapacity() > 0 && audience.getCapacity() <= audienceMaxCapacity;
    }
}