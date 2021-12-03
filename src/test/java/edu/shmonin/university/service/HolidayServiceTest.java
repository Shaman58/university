package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.model.Holiday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        when(holidayDao.get(1)).thenReturn(Optional.of(expected));

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
    void givenNotValidHoliday_whenCreate_thenThrownRuntimeExceptionAndNotStartedHolidayDaoCreate() {
        var holiday = new Holiday("holiday", LocalDate.now().minus(1, ChronoUnit.DAYS));

        assertThrows(RuntimeException.class, () -> holidayService.create(holiday));

        verify(holidayDao, never()).create(holiday);
    }

    @Test
    void givenValidHoliday_whenUpdate_thenStartedHolidayUpdate() {
        var holiday = new Holiday("holiday", LocalDate.now().plus(1, ChronoUnit.DAYS));
        holidayService.update(holiday);

        verify(holidayDao).update(holiday);
    }

    @Test
    void givenNotValidHoliday_whenUpdate_thenThrowRuntimeExceptionAndNotStartedHolidayUpdate() {
        var holiday = new Holiday("holiday", LocalDate.now().minus(1, ChronoUnit.DAYS));

        assertThrows(RuntimeException.class, () -> holidayService.update(holiday));

        verify(holidayDao, never()).update(holiday);
    }

    @Test
    void givenIdAndHolidayDaoGetReturnNotEmptyOptional_whenDelete_thenStartedHolidayDaoDelete() {
        when(holidayDao.get(1)).thenReturn(Optional.of(new Holiday()));

        holidayService.delete(1);

        verify(holidayDao).delete(1);
    }

    @Test
    void givenIdAndHolidayDaoGetReturnEmptyOptional_whenDelete_thenThrowEntityNotFoundExceptionAndNotStartedHolidayDaoDelete() {
        when(holidayDao.get(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> holidayService.delete(1));

        verify(holidayDao, never()).delete(1);
    }
}