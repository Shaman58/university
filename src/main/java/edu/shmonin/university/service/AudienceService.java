package edu.shmonin.university.service;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ForeignReferenceException;
import edu.shmonin.university.exception.InvalidCapacityException;
import edu.shmonin.university.exception.InvalidRoomNumberException;
import edu.shmonin.university.model.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AudienceService {

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

    public Audience get(int audienceId) {
        log.debug("Get audience with id={}", audienceId);
        return audienceDao.get(audienceId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find audience by id=" + audienceId));
    }

    public List<Audience> getAll() {
        log.debug("Get all audiences");
        return audienceDao.getAll();
    }

    public Page<Audience> getAll(Pageable pageable) {
        log.debug("Get all sorted audiences");
        return audienceDao.getAll(pageable);
    }

    public void create(Audience audience) {
        log.debug("Create audience {}", audience);
        validateAudience(audience);
        audienceDao.create(audience);
    }

    public void update(Audience audience) {
        log.debug("Update audience {}", audience);
        validateAudience(audience);
        audienceDao.update(audience);
    }

    public void delete(int audienceId) {
        log.debug("Delete audience by id={}", audienceId);
        this.get(audienceId);
        if (!lectureDao.getByAudienceId(audienceId).isEmpty()) {
            throw new ForeignReferenceException("There are lectures with this audience");
        }
        audienceDao.delete(audienceId);
    }

    private void validateAudience(Audience audience) {
        if ((audience.getRoomNumber() <= 0) || (audience.getRoomNumber() > audiencesMaxRoomNumber)) {
            throw new InvalidRoomNumberException("RoomNumber must be greater than 0 and less then " + audiencesMaxRoomNumber);
        }
        if ((audience.getCapacity() <= 0) || (audience.getCapacity() > audienceMaxCapacity)) {
            throw new InvalidCapacityException("Audience capacity must be greater than 0 and less than " + audienceMaxCapacity);
        }
    }
}