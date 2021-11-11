package edu.shmonin.university.service;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.Audience;
import edu.shmonin.university.model.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        when(audienceDao.get(1)).thenReturn(expected);

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
    void givenInvalidAudience_whenCreate_thenNotStartedDaoCreate() {
        audienceService.create(new Audience(1, 91));

        verify(audienceDao, never()).create(any());
    }

    @Test
    void givenValidAudience_whenUpdate_thenStartedDaoUpdate() {
        var audience = new Audience(1, 60);

        audienceService.update(audience);

        verify(audienceDao).update(audience);
    }

    @Test
    void givenInvalidAudience_whenUpdate_thenNotStartedDaoUpdate() {
        audienceService.update(new Audience(1, 91));

        verify(audienceDao, never()).update(any());
    }

    @Test
    void givenIdAndEmptyListLectures_whenDelete_thenStartedDaoDelete() {
        when(lectureDao.getByAudienceId(1)).thenReturn(new ArrayList<>());

        audienceService.delete(1);

        verify(audienceDao).delete(1);
    }

    @Test
    void givenIdAndNotEmptyListLectures_whenDelete_thenNotStartedDaoDelete() {
        var lectures = new ArrayList<Lecture>();
        lectures.add(new Lecture());
        when(lectureDao.getByAudienceId(1)).thenReturn(lectures);

        audienceService.delete(1);

        verify(audienceDao, never()).delete(1);
    }
}