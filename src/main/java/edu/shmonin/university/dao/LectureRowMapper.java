package edu.shmonin.university.dao;

import edu.shmonin.university.model.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class LectureRowMapper implements RowMapper<Lecture> {

    private final JdbcCourseDao jdbcCourseDao;
    private final JdbcGroupDao jdbcGroupDao;
    private final JdbcAudienceDao jdbcAudienceDao;
    private final JdbcDurationDao jdbcDurationDao;
    private final JdbcTeacherDao jdbcTeacherDao;

    @Autowired
    public LectureRowMapper(JdbcCourseDao jdbcCourseDao, JdbcGroupDao jdbcGroupDao,
                            JdbcAudienceDao jdbcAudienceDao, JdbcDurationDao jdbcDurationDao,
                            JdbcTeacherDao jdbcTeacherDao) {
        this.jdbcCourseDao = jdbcCourseDao;
        this.jdbcGroupDao = jdbcGroupDao;
        this.jdbcAudienceDao = jdbcAudienceDao;
        this.jdbcDurationDao = jdbcDurationDao;
        this.jdbcTeacherDao = jdbcTeacherDao;
    }

    @Override
    public Lecture mapRow(ResultSet resultSet, int i) throws SQLException {
        var lecture = new Lecture();
        lecture.setId(resultSet.getInt("id"));
        lecture.setDate(resultSet.getDate("date").toLocalDate());
        lecture.setCourse(jdbcCourseDao.get(resultSet.getInt("course_id")));
        lecture.setGroups(jdbcGroupDao.getLectureGroups(lecture.getId()));
        lecture.setAudience(jdbcAudienceDao.get(resultSet.getInt("audience_id")));
        lecture.setDuration(jdbcDurationDao.get(resultSet.getInt("duration_id")));
        lecture.setTeacher(jdbcTeacherDao.get(resultSet.getInt("teacher_id")));
        return lecture;
    }
}
