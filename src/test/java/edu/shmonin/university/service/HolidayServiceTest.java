package edu.shmonin.university.service;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.exception.DateNotAvailableException;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.model.Holiday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
    void givenPageRequest_whenGetAll_ThenReturnedPageOfHolidays() {
        var pageRequest = PageRequest.of(0, 20);
        var holidays = List.of(
                new Holiday("holiday1", LocalDate.now().plus(1, ChronoUnit.DAYS)),
                new Holiday("holiday2", LocalDate.now().plus(1, ChronoUnit.DAYS)));
        var expected = new PageImpl<>(holidays, pageRequest, 1);
        when(holidayDao.getAll(pageRequest)).thenReturn(expected);

        var actual = holidayService.getAll(pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void givenValidHoliday_whenCreate_thenStartedHolidayDaoCreate() {
        var holiday = new Holiday("holiday", LocalDate.now().plus(1, ChronoUnit.DAYS));
        holidayService.create(holiday);

        verify(holidayDao).create(holiday);
    }

    @Test
    void givenNotValidHoliday_whenCreate_thenThrownDateNotAvailableExceptionAndNotStartedHolidayDaoCreate() {
        var holiday = new Holiday("holiday", LocalDate.now().minus(1, ChronoUnit.DAYS));

        var exception = assertThrows(DateNotAvailableException.class, () -> holidayService.create(holiday));

        verify(holidayDao, never()).create(holiday);
        assertEquals("The date can not be earlier than the current time", exception.getMessage());
    }

    @Test
    void givenValidHoliday_whenUpdate_thenStartedHolidayUpdate() {
        var holiday = new Holiday("holiday", LocalDate.now().plus(1, ChronoUnit.DAYS));
        holidayService.update(holiday);

        verify(holidayDao).update(holiday);
    }

    @Test
    void givenNotValidHoliday_whenUpdate_thenThrowDateNotAvailableExceptionAndNotStartedHolidayUpdate() {
        var holiday = new Holiday("holiday", LocalDate.now().minus(1, ChronoUnit.DAYS));

        var exception = assertThrows(DateNotAvailableException.class, () -> holidayService.update(holiday));

        verify(holidayDao, never()).update(holiday);
        assertEquals("The date can not be earlier than the current time", exception.getMessage());
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

        var exception = assertThrows(EntityNotFoundException.class, () -> holidayService.delete(1));

        verify(holidayDao, never()).delete(1);
        assertEquals("Can not find holiday by id=1", exception.getMessage());
    }
}