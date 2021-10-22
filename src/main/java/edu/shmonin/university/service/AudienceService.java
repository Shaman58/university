package edu.shmonin.university.service;

import edu.shmonin.university.dao.jdbc.JdbcAudienceDao;
import edu.shmonin.university.model.Audience;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:university.properties")
public class AudienceService {

    @Value("${university.audiences.max}")
    private int audiencesMax;

    @Value("${university.audience.capacity.max}")
    private int audienceCapacity;

    private final JdbcAudienceDao jdbcAudienceDao;

    public AudienceService(JdbcAudienceDao jdbcAudienceDao) {
        this.jdbcAudienceDao = jdbcAudienceDao;
    }

    public void createAudience(Audience audience) {
        if (checkAudience(audience)) {
            jdbcAudienceDao.create(audience);
        }
    }

    public void updateAudience(Audience audience) {
        if (checkAudience(audience)) {
            jdbcAudienceDao.update(audience);
        }
    }

    private boolean checkAudience(Audience audience) throws ServiceException {
        if (audience.getRoomNumber() > 0 || audience.getRoomNumber() < audiencesMax ||
            audience.getCapacity() > 0 || audience.getCapacity() <= audienceCapacity) {
            return true;
        } else {
            throw new ServiceException("Out of bound room number or capacity");
        }
    }
}
