package edu.shmonin.university.service;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.ChainedEntityException;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.model.Audience;
import edu.shmonin.university.model.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class AudienceServiceTest {

    @Mock
    private AudienceDao audienceDao;
    @Mock
    private LectureDao lectureDao;

    @InjectMocks
    private AudienceService audienceService;

    @BeforeEach
    void setUp() {
        setField(audienceService, "audiencesMaxRoomNumber", 300);
        setField(audienceService, "audienceMaxCapacity", 60);
    }

    @Test
    void givenId_whenGet_thenReturnedAudience() {
        var expected = new Audience(1, 30);
        when(audienceDao.get(1)).thenReturn(Optional.of(expected));

        var actual = audienceService.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnedAllAudiences() {
        var expected = new ArrayList<Audience>();
        expected.add(new Audience(1, 30));
        expected.add(new Audience(2, 60));
        when(audienceDao.getAll()).thenReturn(expected);

        var actual = audienceService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenValidAudience_whenCreate_thenStartedDaoCreate() {
        var audience = new Audience(1, 60);

        audienceService.create(audience);

        verify(audienceDao).create(audience);
    }

    @Test
    void givenInvalidAudience_whenCreate_thenThrowsRuntimeExceptionAndNotStartedDaoCreate() {
        var invalidAudience = new Audience(1, 91);

        assertThrows(RuntimeException.class, () -> audienceService.create(invalidAudience));
        verify(audienceDao, never()).create(any());
    }

    @Test
    void givenValidAudience_whenUpdate_thenStartedDaoUpdate() {
        var audience = new Audience(1, 60);

        audienceService.update(audience);

        verify(audienceDao).update(audience);
    }

    @Test
    void givenInvalidAudience_whenUpdate_thenThrowRuntimeExceptionAndNotStartedDaoUpdate() {
        var audience = new Audience(1, 91);

        assertThrows(RuntimeException.class, () -> audienceService.update(audience));
        verify(audienceDao, never()).update(any());
    }

    @Test
    void givenIdAndEmptyListLecturesAndAudienceDaoGetReturnAudience_whenDelete_thenStartedDaoDelete() {
        when(lectureDao.getByAudienceId(1)).thenReturn(new ArrayList<>());
        when(audienceDao.get(1)).thenReturn(Optional.of(new Audience()));

        audienceService.delete(1);

        verify(audienceDao).delete(1);
    }

    @Test
    void givenIdAndNotEmptyListLecturesAndAudienceDaoGetReturnAudience_whenDelete_thenThrowChainedEntityExceptionAndNotStartedDaoDelete() {
        var lectures = new ArrayList<Lecture>();
        lectures.add(new Lecture());
        when(lectureDao.getByAudienceId(1)).thenReturn(lectures);
        when(audienceDao.get(1)).thenReturn(Optional.of(new Audience()));

        assertThrows(ChainedEntityException.class, () -> audienceService.delete(1));

        verify(audienceDao, never()).delete(1);
    }

    @Test
    void givenIdAndAudienceDaoGetReturnEmpty_whenDelete_thenThrowEntityNotFoundExceptionAndNotStartedDaoDelete() {
        when(audienceDao.get(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> audienceService.delete(1));

        verify(audienceDao, never()).delete(1);
    }
}