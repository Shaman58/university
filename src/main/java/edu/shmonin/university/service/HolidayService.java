package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.model.Holiday;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayService implements EntityService<Holiday> {

    private final HolidayDao jdbcHolidayDao;

    public HolidayService(HolidayDao jdbcHolidayDao) {
        this.jdbcHolidayDao = jdbcHolidayDao;
    }

    @Override
    public Holiday get(int holidayId) {
        return jdbcHolidayDao.get(holidayId);
    }

    @Override
    public List<Holiday> getAll() {
        return jdbcHolidayDao.getAll();
    }

    @Override
    public void create(Holiday holiday) {
        if (validateHoliday(holiday)) {
            jdbcHolidayDao.create(holiday);
        }
    }

    @Override
    public void update(Holiday holiday) {
        if (validateHoliday(holiday)) {
            jdbcHolidayDao.update(holiday);
        }
    }

    @Override
    public void delete(int holidayId) {
        jdbcHolidayDao.delete(holidayId);
    }

    private boolean validateHoliday(Holiday holiday) {
        return holiday.getDate().isAfter(LocalDate.now());
    }
}