package edu.shmonin.university.service;

import edu.shmonin.university.dao.DurationDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.Duration;
import edu.shmonin.university.model.Lecture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DurationServiceTest {

    @Mock
    private DurationDao durationDao;
    @Mock
    private LectureDao lectureDao;

    @InjectMocks
    private DurationService durationService;

    @Test
    void givenId_whenGet_thenReturnDuration() {
        var expected = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));
        when(durationDao.get(1)).thenReturn(expected);

        var actual = durationService.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnAllDurations() {
        var expected = List.of(
                new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0)),
                new Duration(LocalTime.of(15, 0), LocalTime.of(17, 0)));
        when(durationDao.getAll()).thenReturn(expected);

        var actual = durationService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenValidDuration_whenCreate_thenStartedDurationDaoCreate() {
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));

        durationService.create(duration);

        verify(durationDao).create(duration);
    }

    @Test
    void givenInvalidDuration_whenCreate_thenNotStartedDurationDaoCreate() {
        var duration = new Duration(LocalTime.of(15, 0), LocalTime.of(14, 0));

        durationService.create(duration);

        verify(durationDao, never()).create(duration);
    }

    @Test
    void givenValidDuration_whenUpdate_thenStartedDurationDaoUpdate() {
        var duration = new Duration(LocalTime.of(12, 0), LocalTime.of(14, 0));

        durationService.update(duration);

        verify(durationDao).update(duration);
    }

    @Test
    void givenInvalidDuration_whenUpdate_thenNotStartedDurationDaoUpdate() {
        var duration = new Duration(LocalTime.of(15, 0), LocalTime.of(14, 0));

        durationService.update(duration);

        verify(durationDao, never()).update(duration);
    }

    @Test
    void givenIdAndEmptyLecturesInLectureDaoGetByDuration_whenDelete_thenStartedDurationDaoDelete() {
        when(lectureDao.getByDurationId(1)).thenReturn(new ArrayList<>());

        durationService.delete(1);

        verify(durationDao).delete(1);
    }

    @Test
    void givenIdAndNotEmptyLecturesInLectureDaoGetByDuration_whenDelete_thenNotStartedDurationDaoDelete() {
        when(lectureDao.getByDurationId(1)).thenReturn(List.of(new Lecture()));

        durationService.delete(1);

        verify(durationDao, never()).delete(1);
    }
}