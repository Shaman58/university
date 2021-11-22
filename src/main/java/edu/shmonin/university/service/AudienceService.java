package edu.shmonin.university.service;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.LinkedEntityException;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ValidationException;
import edu.shmonin.university.model.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AudienceService implements EntityService<Audience> {

    private static final Logger log = LoggerFactory.getLogger(AudienceService.class);

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
        try {
            log.debug("Get audience with id={}", audienceId);
            return jdbcAudienceDao.get(audienceId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the audience. There is no audience with id=" + audienceId);
        }
    }

    @Override
    public List<Audience> getAll() {
        log.debug("Get all audiences");
        return jdbcAudienceDao.getAll();
    }

    @Override
    public void create(Audience audience) {
        validateAudience(audience);
        log.debug("Create audience {}", audience);
        jdbcAudienceDao.create(audience);
    }

    @Override
    public void update(Audience audience) {
        validateAudience(audience);
        log.debug("Update audience {}", audience);
        jdbcAudienceDao.update(audience);
    }

    @Override
    public void delete(int audienceId) {
        try {
            jdbcAudienceDao.get(audienceId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not delete the audience. There is no audience with id=" + audienceId);
        }
        if (!jdbcLectureDao.getByAudienceId(audienceId).isEmpty()) {
            throw new LinkedEntityException("Can not delete audience with id=" + audienceId + ", there are lectures with this audience in database");
        }
        log.debug("Delete audience by id={}", audienceId);
        jdbcAudienceDao.delete(audienceId);
    }

    private void validateAudience(Audience audience) {
        if (audience.getRoomNumber() <= 0) {
            throw new ValidationException("The audience " + audience + " did not pass the validity check. Room number can not be negative or zero");
        }
        if (audience.getRoomNumber() > audiencesMaxRoomNumber) {
            throw new ValidationException("The audience " + audience + " did not pass the validity check. RoomNumber can not be greater " + audiencesMaxRoomNumber);
        }
        if (audience.getCapacity() <= 0) {
            throw new ValidationException("The audience " + audience + " did not pass the validity check. Audience capacity can not be negative or zero");
        }
        if (audience.getCapacity() > audienceMaxCapacity) {
            throw new ValidationException("The audience " + audience + " did not pass the validity check. Audience capacity can not be greater " + audienceMaxCapacity);
        }
    }
}