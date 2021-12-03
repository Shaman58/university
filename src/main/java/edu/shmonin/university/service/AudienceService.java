package edu.shmonin.university.service;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ChainedEntityException;
import edu.shmonin.university.exception.ValidationException;
import edu.shmonin.university.model.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AudienceService implements EntityService<Audience> {

    private static final Logger log = LoggerFactory.getLogger(AudienceService.class);

    @Value("${university.audiences.max.room.number}")
    private int audiencesMaxRoomNumber;

    @Value("${university.audience.capacity.max}")
    private int audienceMaxCapacity;

    private final AudienceDao audienceDao;
    private final LectureDao lectureDao;

    public AudienceService(AudienceDao audienceDao, LectureDao lectureDao) {
        this.audienceDao = audienceDao;
        this.lectureDao = lectureDao;
    }

    @Override
    public Audience get(int audienceId) {
        var audience = audienceDao.get(audienceId);
        if (audience.isEmpty()) {
            throw new EntityNotFoundException("Can not find the audience. There is no audience with id=" + audienceId);
        }
        log.debug("Get audience with id={}", audienceId);
        return audience.get();
    }

    @Override
    public List<Audience> getAll() {
        log.debug("Get all audiences");
        return audienceDao.getAll();
    }

    @Override
    public void create(Audience audience) {
        validateAudience(audience);
        log.debug("Create audience {}", audience);
        audienceDao.create(audience);
    }

    @Override
    public void update(Audience audience) {
        validateAudience(audience);
        log.debug("Update audience {}", audience);
        audienceDao.update(audience);
    }

    @Override
    public void delete(int audienceId) {
        if (audienceDao.get(audienceId).isEmpty()) {
            throw new EntityNotFoundException("Can not delete the audience. There is no audience with id=" + audienceId);
        }
        if (!lectureDao.getByAudienceId(audienceId).isEmpty()) {
            throw new ChainedEntityException("Can not delete audience with id=" + audienceId + ", there are lectures with this audience in database");
        }
        log.debug("Delete audience by id={}", audienceId);
        audienceDao.delete(audienceId);
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