package edu.shmonin.university.dao;

import edu.shmonin.university.model.Lecture;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureDao extends Dao<Lecture> {
    List<Lecture> getByAudienceId(int audienceId);

    List<Lecture> getByCourseId(int courseId);

    List<Lecture> getByDurationId(int durationId);

    List<Lecture> getByGroupId(int groupId);

    List<Lecture> getByTeacherId(int teacherId);

    Optional<Lecture> getByGroupDateDuration(int groupId, LocalDate localDate, int durationId);
}