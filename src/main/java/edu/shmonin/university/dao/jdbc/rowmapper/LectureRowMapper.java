package edu.shmonin.university.dao.jdbc.rowmapper;

import edu.shmonin.university.dao.jdbc.*;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.model.Lecture;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class LectureRowMapper implements RowMapper<Lecture> {

    private final JdbcCourseDao courseDao;
    private final JdbcGroupDao groupDao;
    private final JdbcAudienceDao audienceDao;
    private final JdbcDurationDao durationDao;
    private final JdbcTeacherDao teacherDao;

    public LectureRowMapper(JdbcCourseDao courseDao, JdbcGroupDao groupDao,
                            JdbcAudienceDao audienceDao, JdbcDurationDao durationDao,
                            JdbcTeacherDao teacherDao) {
        this.courseDao = courseDao;
        this.groupDao = groupDao;
        this.audienceDao = audienceDao;
        this.durationDao = durationDao;
        this.teacherDao = teacherDao;
    }

    @Override
    public Lecture mapRow(ResultSet resultSet, int i) throws SQLException {
        var lecture = new Lecture();
        lecture.setId(resultSet.getInt("id"));
        lecture.setDate(resultSet.getObject("date", LocalDate.class));
        var finalCourseId = resultSet.getInt("course_id");
        lecture.setCourse(courseDao.get(finalCourseId).
                orElseThrow(() -> new EntityNotFoundException("Can not find course by id=" + finalCourseId)));
        lecture.setGroups(groupDao.getByLectureId(lecture.getId()));
        var finalAudienceId = resultSet.getInt("audience_id");
        lecture.setAudience(audienceDao.get(finalAudienceId).
                orElseThrow(() -> new EntityNotFoundException("Can not find audience by id=" + finalAudienceId)));
        var finalDurationId = resultSet.getInt("duration_id");
        lecture.setDuration(durationDao.get(finalDurationId).
                orElseThrow(() -> new EntityNotFoundException("Can not find duration by id=" + finalDurationId)));
        var finalTeacherId = resultSet.getInt("teacher_id");
        lecture.setTeacher(teacherDao.get(finalTeacherId).
                orElseThrow(() -> new EntityNotFoundException("Can not find teacher by id=" + finalTeacherId)));
        return lecture;
    }
}