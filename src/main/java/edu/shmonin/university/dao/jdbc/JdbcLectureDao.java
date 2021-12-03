package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.jdbc.rowmapper.LectureRowMapper;
import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Lecture;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcLectureDao implements LectureDao {

    private static final String GET_QUERY = "SELECT * FROM lectures WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM lectures";
    private static final String CREATE_QUERY = "INSERT INTO lectures(date, course_id, audience_id, duration_id, teacher_id) VALUES(?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE lectures SET date=?, course_id=?, audience_id=?, duration_id=?, teacher_id=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM lectures WHERE id=?";
    private static final String ADD_LECTURE_GROUP = "INSERT INTO lecture_groups(group_id, lecture_id) VALUES (?,?)";
    private static final String DELETE_LECTURE_GROUP = "DELETE FROM lecture_groups WHERE group_id=? AND lecture_id=?";
    private static final String GET_ALL_BY_AUDIENCE = "SELECT * FROM lectures WHERE audience_id=?";
    private static final String GET_ALL_BY_COURSE = "SELECT * FROM lectures WHERE course_id=?";
    private static final String GET_ALL_BY_DURATION = "SELECT * FROM lectures WHERE duration_id=?";
    private static final String GET_ALL_BY_GROUP = "SELECT id,date,course_id,audience_id,duration_id,teacher_id FROM lectures INNER JOIN lecture_groups ON lectures.id = lecture_groups.lecture_id WHERE group_id=?";
    private static final String GET_ALL_BY_TEACHER = "SELECT * FROM lectures WHERE teacher_id=?";
    private static final String GET_ALL_BY_GROUP_DATE_DURATION = "SELECT id,date,course_id,audience_id,duration_id,teacher_id FROM lectures INNER JOIN lecture_groups ON lectures.id = lecture_groups.lecture_id WHERE group_id=? AND date=? AND duration_id=?";

    private final JdbcTemplate jdbcTemplate;
    private final LectureRowMapper lectureRowMapper;

    public JdbcLectureDao(JdbcTemplate jdbcTemplate, LectureRowMapper lectureRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lectureRowMapper = lectureRowMapper;
    }

    @Override
    public Optional<Lecture> get(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GET_QUERY, lectureRowMapper, id));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Lecture> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, lectureRowMapper);
    }

    @Transactional
    @Override
    public void create(Lecture lecture) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, lecture.getDate());
            preparedStatement.setInt(2, lecture.getCourse().getId());

            preparedStatement.setInt(3, lecture.getAudience().getId());
            preparedStatement.setInt(4, lecture.getDuration().getId());
            preparedStatement.setInt(5, lecture.getTeacher().getId());
            return preparedStatement;
        }, keyHolder);
        lecture.setId((Integer) Objects.requireNonNull(keyHolder.getKeys().get("id")));
        lecture.getGroups().forEach(p -> jdbcTemplate.update(ADD_LECTURE_GROUP, p.getId(), lecture.getId()));
    }

    @Transactional
    @Override
    public void update(Lecture updatedLecture) {
        var groups = getGroupsFromTheLecture(updatedLecture);
        jdbcTemplate.update(UPDATE_QUERY, updatedLecture.getDate(), updatedLecture.getCourse().getId(), updatedLecture.getAudience().getId(),
                updatedLecture.getDuration().getId(), updatedLecture.getTeacher().getId(), updatedLecture.getId());
        groups.stream().filter(p -> !updatedLecture.getGroups().contains(p)).
                forEach(p -> jdbcTemplate.update(DELETE_LECTURE_GROUP, p.getId(), updatedLecture.getId()));
        updatedLecture.getGroups().stream().filter(p -> !groups.contains(p)).
                forEach(p -> jdbcTemplate.update(ADD_LECTURE_GROUP, p.getId(), updatedLecture.getId()));
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public List<Lecture> getByAudienceId(int audienceId) {
        return jdbcTemplate.query(GET_ALL_BY_AUDIENCE, lectureRowMapper, audienceId);
    }

    @Override
    public List<Lecture> getByCourseId(int courseId) {
        return jdbcTemplate.query(GET_ALL_BY_COURSE, lectureRowMapper, courseId);
    }

    @Override
    public List<Lecture> getByDurationId(int durationId) {
        return jdbcTemplate.query(GET_ALL_BY_DURATION, lectureRowMapper, durationId);
    }

    @Override
    public List<Lecture> getByGroupId(int groupId) {
        return jdbcTemplate.query(GET_ALL_BY_GROUP, lectureRowMapper, groupId);
    }

    @Override
    public List<Lecture> getByTeacherId(int teacherId) {
        return jdbcTemplate.query(GET_ALL_BY_TEACHER, lectureRowMapper, teacherId);
    }

    @Override
    public Optional<Lecture> getByGroupDateDuration(int groupId, LocalDate localDate, int durationId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GET_ALL_BY_GROUP_DATE_DURATION, lectureRowMapper, groupId, localDate, durationId));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    private List<Group> getGroupsFromTheLecture(Lecture updatedLecture) {
        var lecture = get(updatedLecture.getId());
        if (lecture.isPresent()) {
            return lecture.get().getGroups();
        } else {
            return new ArrayList<>();
        }
    }
}