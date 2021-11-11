package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.model.Holiday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayDao holidayDao;

    @InjectMocks
    private HolidayService holidayService;

    @Test
    void givenId_whenGet_thenReturnedHoliday() {
        var expected = new Holiday("holiday", LocalDate.now().plus(1, ChronoUnit.DAYS));
        when(holidayDao.get(1)).thenReturn(expected);

        var actual = holidayService.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_ThenReturnedAllHolidays() {
        var expected = List.of(
                new Holiday("holiday1", LocalDate.now().plus(1, ChronoUnit.DAYS)),
                new Holiday("holiday2", LocalDate.now().plus(1, ChronoUnit.DAYS)));
        when(holidayDao.getAll()).thenReturn(expected);

        var actual = holidayService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenValidHoliday_whenCreate_thenStartedHolidayDaoCreate() {
        var holiday = new Holiday("holiday", LocalDate.now().plus(1, ChronoUnit.DAYS));
        holidayService.create(holiday);

        verify(holidayDao).create(holiday);
    }

    @Test
    void givenNotValidHoliday_whenCreate_thenNotStartedHolidayDaoCreate() {
        var holiday = new Holiday("holiday", LocalDate.now().minus(1, ChronoUnit.DAYS));
        holidayService.create(holiday);

        verify(holidayDao, never()).create(holiday);
    }

    @Test
    void givenValidHoliday_whenUpdate_thenStartedHolidayUpdate() {
        var holiday = new Holiday("holiday", LocalDate.now().plus(1, ChronoUnit.DAYS));
        holidayService.update(holiday);

        verify(holidayDao).update(holiday);
    }

    @Test
    void givenNotValidHoliday_whenUpdate_thenNotStartedHolidayUpdate() {
        var holiday = new Holiday("holiday", LocalDate.now().minus(1, ChronoUnit.DAYS));
        holidayService.update(holiday);

        verify(holidayDao, never()).update(holiday);
    }

    @Test
    void givenId_whenDelete_thenStartedHolidayDaoDelete() {
        holidayService.delete(1);

        verify(holidayDao).delete(1);
    }
}