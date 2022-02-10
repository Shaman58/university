package edu.shmonin.university.dao;

import edu.shmonin.university.model.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureDao extends Dao<Lecture> {
    List<Lecture> getByAudienceId(int audienceId);

    List<Lecture> getByCourseId(int courseId);

    List<Lecture> getByDurationId(int durationId);

    List<Lecture> getByGroupId(int groupId);

    List<Lecture> getByTeacherId(int teacherId);

    Optional<Lecture> getByGroupIdAndDateAndDurationId(int groupId, LocalDate date, int durationId);

    Optional<Lecture> getByTeacherIdAndDateAndDurationId(int teacherId, LocalDate date, int durationId);

    Optional<Lecture> getByAudienceIdAndDateAndDurationId(int audienceId, LocalDate date, int durationId);
}