package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.model.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayService implements EntityService<Holiday> {

    private static final Logger log = LoggerFactory.getLogger(HolidayService.class);

    private final HolidayDao holidayDao;

    public HolidayService(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    @Override
    public Holiday get(int holidayId) {
        var holiday = holidayDao.get(holidayId);
        if (holiday.isEmpty()) {
            throw new EntityNotFoundException("Can not find holiday by id=" + holidayId);
        }
        return holiday.get();
    }

    @Override
    public List<Holiday> getAll() {
        log.debug("Get all holidays");
        return holidayDao.getAll();
    }

    @Override
    public void create(Holiday holiday) {
        validateHoliday(holiday);
        log.debug("Create holiday {}", holiday);
        holidayDao.create(holiday);
    }

    @Override
    public void update(Holiday holiday) {
        validateHoliday(holiday);
        log.debug("Update holiday {}", holiday);
        holidayDao.update(holiday);
    }

    @Override
    public void delete(int holidayId) {
        if (holidayDao.get(holidayId).isEmpty()) {
            throw new EntityNotFoundException("Can not find holiday by id=" + holidayId);
        }
        log.debug("Delete holiday by id={}", holidayId);
        holidayDao.delete(holidayId);
    }

    private void validateHoliday(Holiday holiday) {
        if (!holiday.getDate().isAfter(LocalDate.now())) {
            throw new DateTimeException("The holiday " + holiday + " did not pass the validity check. The date can not be earlier than the current time");
        }
    }
}