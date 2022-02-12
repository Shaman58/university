package edu.shmonin.university.service;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ForeignReferenceException;
import edu.shmonin.university.exception.InvalidCapacityException;
import edu.shmonin.university.exception.InvalidRoomNumberException;
import edu.shmonin.university.model.Audience;
import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.service.implementation.AudienceServiceImpl;
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
    private AudienceServiceImpl audienceService;

    @BeforeEach
    void setUp() {
        setField(audienceService, "audiencesMaxRoomNumber", 300);
        setField(audienceService, "audienceMaxCapacity", 90);
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
    void givenInvalidAudienceCapacity_whenCreate_thenThrowsInvalidCapacityExceptionAndNotStartedDaoCreate() {
        var invalidAudience = new Audience(1, 91);

        var exception = assertThrows(InvalidCapacityException.class, () -> audienceService.create(invalidAudience));

        verify(audienceDao, never()).create(any());
        assertEquals("Audience capacity must be greater than 0 and less than 90", exception.getMessage());
    }

    @Test
    void givenInvalidAudienceRoomNumber_whenCreate_thenThrowsInvalidRoomNumberExceptionAndNotStartedDaoCreate() {
        var invalidAudience = new Audience(301, 30);

        var exception = assertThrows(InvalidRoomNumberException.class, () -> audienceService.create(invalidAudience));

        verify(audienceDao, never()).create(any());
        assertEquals("RoomNumber must be greater than 0 and less then 300", exception.getMessage());
    }

    @Test
    void givenValidAudience_whenUpdate_thenStartedDaoUpdate() {
        var audience = new Audience(1, 60);

        audienceService.update(audience);

        verify(audienceDao).update(audience);
    }

    @Test
    void givenInvalidAudienceCapacity_whenUpdate_thenThrowInvalidCapacityExceptionAndNotStartedDaoUpdate() {
        var audience = new Audience(1, 91);

        var exception = assertThrows(InvalidCapacityException.class, () -> audienceService.update(audience));

        verify(audienceDao, never()).update(any());
        assertEquals("Audience capacity must be greater than 0 and less than 90", exception.getMessage());
    }

    @Test
    void givenInvalidAudienceRoomNumber_whenUpdate_thenThrowInvalidRoomNumberExceptionAndNotStartedDaoUpdate() {
        var audience = new Audience(301, 60);

        var exception = assertThrows(InvalidRoomNumberException.class, () -> audienceService.update(audience));

        verify(audienceDao, never()).update(any());
        assertEquals("RoomNumber must be greater than 0 and less then 300", exception.getMessage());
    }

    @Test
    void givenIdAndEmptyListLecturesAndAudienceDaoGetReturnAudience_whenDelete_thenStartedDaoDelete() {
        when(lectureDao.getByAudienceId(1)).thenReturn(new ArrayList<>());
        when(audienceDao.get(1)).thenReturn(Optional.of(new Audience()));

        audienceService.delete(1);

        verify(audienceDao).delete(1);
    }

    @Test
    void givenIdAndNotEmptyListLecturesAndAudienceDaoGetReturnAudience_whenDelete_thenThrowForeignReferenceExceptionAndNotStartedDaoDelete() {
        var lectures = new ArrayList<Lecture>();
        lectures.add(new Lecture());
        when(lectureDao.getByAudienceId(1)).thenReturn(lectures);
        when(audienceDao.get(1)).thenReturn(Optional.of(new Audience()));

        var exception = assertThrows(ForeignReferenceException.class, () -> audienceService.delete(1));

        verify(audienceDao, never()).delete(1);
        assertEquals("There are lectures with this audience", exception.getMessage());
    }

    @Test
    void givenIdAndAudienceDaoGetReturnEmpty_whenDelete_thenThrowEntityNotFoundExceptionAndNotStartedDaoDelete() {
        when(audienceDao.get(1)).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class, () -> audienceService.delete(1));

        verify(audienceDao, never()).delete(1);
        assertEquals("Can not find audience by id=1", exception.getMessage());
    }
}