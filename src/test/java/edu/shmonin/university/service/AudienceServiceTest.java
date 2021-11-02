package edu.shmonin.university.service;

import config.TestConfig;
import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class AudienceServiceTest {

    @Mock
    private AudienceDao jdbcAudienceDao;
    @Mock
    private LectureDao jdbcLectureDao;
    @InjectMocks
    private AudienceService audienceService;

    @Test
    void givenId_whenGet_thenReturnedAudience() {
        var expected = new Audience(1, 30);

        Mockito.when(jdbcAudienceDao.get(1)).thenReturn(expected);

        var actual = audienceService.get(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetAll_thenReturnedAllAudiences() {
        var expected = new ArrayList<Audience>();
        expected.add(new Audience(1, 30));
        expected.add(new Audience(2, 60));

        Mockito.when(jdbcAudienceDao.getAll()).thenReturn(expected);

        var actual = audienceService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenValidAudience_whenCreate_thenStartedDaoCreate() {
        audienceService.create(new Audience(1, 60));

        Mockito.verify(jdbcAudienceDao).create(Mockito.any());
    }

    @Test
    void givenInvalidAudience_whenCreate_thenStartedDaoCreate() {
        audienceService.create(new Audience(1, 91));

        Mockito.verify(jdbcAudienceDao, Mockito.never()).create(Mockito.any());
    }

    @Test
    void givenValidAudience_whenUpdate_thenStartedDaoUpdate() {
        audienceService.update(new Audience(1, 60));

        Mockito.verify(jdbcAudienceDao).update(Mockito.any());
    }

    @Test
    void givenInvalidAudience_whenUpdate_thenStartedDaoUpdate() {
        audienceService.update(new Audience(1, 91));

        Mockito.verify(jdbcAudienceDao, Mockito.never()).update(Mockito.any());
    }

    @Test
    void givenId_whenDelete_thenStartedDaoDelete() {
        Mockito.when(jdbcLectureDao.getByAudienceId(1)).thenReturn(new ArrayList<>());

        audienceService.delete(1);

        Mockito.verify(jdbcAudienceDao).delete(Mockito.any());
    }
}