package edu.shmonin.university.service;

import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.model.Vacation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacationServiceTest {

    @Mock
    private VacationDao vacationDao;

    @InjectMocks
    private VacationService vacationService;

    @Test
    void givenId_whenGet_thenReturnedVacation() {
        var expected = new Vacation(LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 2, 1));
        when(vacationDao.get(1)).thenReturn(expected);

        var actual = vacationService.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnedAllVacations() {
        var expected = List.of(
                new Vacation(LocalDate.of(2021, 1, 1),
                        LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 2, 1),
                        LocalDate.of(2021, 3, 1)));
        when(vacationDao.getAll()).thenReturn(expected);

        var actual = vacationService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenValidVacation_whenCreate_thenStartedVacationDaoCreate() {
        var vacation = new Vacation(LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(1));

        vacationService.create(vacation);

        verify(vacationDao).create(vacation);
    }

    @Test
    void givenInvalidVacation_whenCreate_thenNotStartedVacationDaoCreate() {
        var vacation = new Vacation(LocalDate.now().plusDays(1),
                LocalDate.now().minusMonths(1));

        vacationService.create(vacation);

        verify(vacationDao, never()).create(vacation);
    }

    @Test
    void givenValidOutOfDateVacation_whenCreate_thenNotStartedVacationDaoCreate() {
        var vacation = new Vacation(LocalDate.now().minusDays(1),
                LocalDate.now().plusMonths(1));

        vacationService.create(vacation);

        verify(vacationDao, never()).create(vacation);
    }

    @Test
    void givenValidVacation_whenUpdate_thenStartedVacationDaoUpdate() {
        var vacation = new Vacation(LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(1));

        vacationService.update(vacation);

        verify(vacationDao).update(vacation);
    }

    @Test
    void givenInvalidVacation_whenUpdate_thenNotStartedVacationDaoUpdate() {
        var vacation = new Vacation(LocalDate.now().plusDays(1),
                LocalDate.now().minusMonths(1));

        vacationService.update(vacation);

        verify(vacationDao, never()).update(vacation);
    }

    @Test
    void givenValidOutOfDateVacation_whenUpdate_thenStartedVacationDaoUpdate() {
        var vacation = new Vacation(LocalDate.now().minusDays(1),
                LocalDate.now().plusMonths(1));

        vacationService.update(vacation);

        verify(vacationDao, never()).update(vacation);
    }

    @Test
    void givenId_whenDelete_thenStartedVacationDaoDelete() {
        vacationService.delete(1);

        verify(vacationDao).delete(1);
    }

    @Test
    void givenId_whenGetByTeacherId_thenReturnedVacations() {
        var expected = List.of(
                new Vacation(LocalDate.of(2021, 1, 1),
                        LocalDate.of(2021, 2, 1)),
                new Vacation(LocalDate.of(2021, 2, 1),
                        LocalDate.of(2021, 3, 1)));
        when(vacationDao.getByTeacherId(1)).thenReturn(expected);

        var actual = vacationService.getByTeacherId(1);

        assertEquals(expected, actual);
    }
}