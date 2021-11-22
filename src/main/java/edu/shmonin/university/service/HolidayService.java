package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ValidationException;
import edu.shmonin.university.model.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayService implements EntityService<Holiday> {

    private static final Logger log = LoggerFactory.getLogger(HolidayService.class);

    private final HolidayDao jdbcHolidayDao;

    public HolidayService(HolidayDao jdbcHolidayDao) {
        this.jdbcHolidayDao = jdbcHolidayDao;
    }

    @Override
    public Holiday get(int holidayId) {
        try {
            log.debug("Get holiday with id={}", holidayId);
            return jdbcHolidayDao.get(holidayId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the holiday. There is no holiday with id=" + holidayId);
        }
    }

    @Override
    public List<Holiday> getAll() {
        log.debug("Get all holidays");
        return jdbcHolidayDao.getAll();
    }

    @Override
    public void create(Holiday holiday) {
        validateHoliday(holiday);
        log.debug("Create holiday {}", holiday);
        jdbcHolidayDao.create(holiday);
    }

    @Override
    public void update(Holiday holiday) {
        validateHoliday(holiday);
        log.debug("Update holiday {}", holiday);
        jdbcHolidayDao.update(holiday);
    }

    @Override
    public void delete(int holidayId) {
        try {
            jdbcHolidayDao.get(holidayId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the holiday. There is no holiday with id=" + holidayId);
        }
        log.debug("Delete holiday by id={}", holidayId);
        jdbcHolidayDao.delete(holidayId);
    }

    private void validateHoliday(Holiday holiday) {
        if (!holiday.getDate().isAfter(LocalDate.now())) {
            throw new ValidationException("The holiday " + holiday + " did not pass the validity check. The date can not be earlier than the current time");
        }
    }
}