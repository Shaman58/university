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

    public AudienceService(AudienceDao jdbcAudienceDao, LectureDao jdbcLectureDao) {
        this.jdbcAudienceDao = jdbcAudienceDao;
        this.jdbcLectureDao = jdbcLectureDao;
    }

    @Override
    public Audience get(int audienceId) {
        return jdbcAudienceDao.get(audienceId);
    }

    @Override
    public List<Audience> getAll() {
        return jdbcAudienceDao.getAll();
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
        }
    }

    private boolean validateAudience(Audience audience) {
        return audience.getRoomNumber() > 0 && audience.getRoomNumber() <= audiencesMaxRoomNumber &&
               audience.getCapacity() > 0 && audience.getCapacity() <= audienceMaxCapacity;
    }
}