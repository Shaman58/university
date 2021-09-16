package edu.shmonin.university.dao;

import edu.shmonin.university.model.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LectureDao implements Dao<Lecture> {

    private static final String GET_QUERY = "SELECT * FROM lectures WHERE lecture_id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM lectures";
    private static final String CREATE_QUERY = "INSERT INTO lectures(date, course_id, audience_id, duration_id, teacher_id) VALUES(?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE lectures SET date=?, course_id=?, audience_id=?, duration_id=?, teacher_id=? WHERE lecture_id=?";
    private static final String DELETE_QUERY = "DELETE FROM lectures WHERE lecture_id=?";

    private JdbcTemplate jdbcTemplate;
    private CourseDao courseDao;
    private GroupDao groupDao;
    private AudienceDao audienceDao;
    private DurationDao durationDao;
    private TeacherDao teacherDao;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Autowired
    public void setAudienceDao(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
    }

    @Autowired
    public void setDurationDao(DurationDao durationDao) {
        this.durationDao = durationDao;
    }

    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Override
    public Lecture get(int id) {
        return jdbcTemplate.query(GET_QUERY, new BeanPropertyRowMapper<>(Lecture.class), id).
                stream().peek(p -> p.setCourse(courseDao.getLectureCourse(p))).
                peek(p -> p.setGroups(groupDao.getLectureGroups(p))).
                peek(p -> p.setAudience(audienceDao.getLectureAudience(p))).
                peek(p -> p.setDuration(durationDao.getLectureDuration(p))).
                peek(p -> p.setTeacher(teacherDao.getLectureTeacher(p))).
                findAny().orElse(null);
    }

    @Override
    public List<Lecture> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Lecture.class)).
                stream().peek(p -> p.setCourse(courseDao.getLectureCourse(p))).
                peek(p -> p.setGroups(groupDao.getLectureGroups(p))).
                peek(p -> p.setAudience(audienceDao.getLectureAudience(p))).
                peek(p -> p.setDuration(durationDao.getLectureDuration(p))).
                peek(p -> p.setTeacher(teacherDao.getLectureTeacher(p))).
                collect(Collectors.toList());
    }

    @Override
    public void create(Lecture entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getDate(), entity.getCourse().getCourseId(), entity.getAudience().getAudienceId(),
                entity.getDuration().getDurationId(), entity.getTeacher().getTeacherId());
    }

    @Override
    public void update(Lecture entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getDate(), entity.getCourse().getCourseId(), entity.getAudience().getAudienceId(),
                entity.getDuration().getDurationId(), entity.getTeacher().getTeacherId(), entity.getLectureId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}
