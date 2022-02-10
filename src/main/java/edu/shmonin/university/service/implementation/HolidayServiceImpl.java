package edu.shmonin.university.service.implementation;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.exception.DateNotAvailableException;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.model.Holiday;
import edu.shmonin.university.service.HolidayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayServiceImpl implements HolidayService {

    private static final Logger log = LoggerFactory.getLogger(HolidayServiceImpl.class);

    private final HolidayDao holidayDao;

    public HolidayServiceImpl(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    @Override
    public Holiday get(int holidayId) {
        log.debug("Get holiday with id={}", holidayId);
        return holidayDao.get(holidayId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find holiday by id=" + holidayId));
    }

    @Override
    public List<Holiday> getAll() {
        log.debug("Get all holidays");
        return holidayDao.getAll();
    }

    @Override
    public Page<Holiday> getAll(Pageable pageable) {
        log.debug("Get all sorted holidays");
        return holidayDao.getAll(pageable);
    }

    @Override
    public void create(Holiday holiday) {
        log.debug("Create holiday {}", holiday);
        validateHoliday(holiday);
        holidayDao.create(holiday);
    }

    @Override
    public void update(Holiday holiday) {
        log.debug("Update holiday {}", holiday);
        validateHoliday(holiday);
        holidayDao.update(holiday);
    }

    @Override
    public void delete(int holidayId) {
        log.debug("Delete holiday by id={}", holidayId);
        this.get(holidayId);
        holidayDao.delete(holidayId);
    }

    private void validateHoliday(Holiday holiday) {
        if (!holiday.getDate().isAfter(LocalDate.now())) {
            throw new DateNotAvailableException("The date can not be earlier than the current time");
        }
    }
}