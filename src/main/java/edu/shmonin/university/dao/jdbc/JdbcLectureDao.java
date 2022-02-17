package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.jdbc.rowmapper.LectureRowMapper;
import edu.shmonin.university.model.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcLectureDao implements LectureDao {

    private static final String GET_QUERY = "SELECT * FROM lectures WHERE id=?";
    private static final String GET_COUNT_QUERY = "SELECT COUNT(*) FROM lectures";
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
    private static final String GET_BY_GROUP_DATE_DURATION = "SELECT id,date,course_id,audience_id,duration_id,teacher_id FROM lectures INNER JOIN lecture_groups ON lectures.id = lecture_groups.lecture_id WHERE group_id=? AND date=? AND duration_id=?";
    private static final String GET_BY_TEACHER_DATE_DURATION = "SELECT id,date,course_id,audience_id,duration_id,teacher_id FROM lectures  WHERE teacher_id=? AND date=? AND duration_id=?";
    private static final String GET_BY_AUDIENCE_DATE_DURATION = "SELECT id,date,course_id,audience_id,duration_id,teacher_id FROM lectures  WHERE teacher_id=? AND date=? AND duration_id=?";
    private static final String GET_PAGE_QUERY = "SELECT * FROM lectures ORDER BY date OFFSET ? LIMIT ?";
    private static final String GET_COUNT_BY_GROUP_AND_PERIOD = "SELECT COUNT(*) FROM lectures INNER JOIN lecture_groups ON lectures.id = lecture_groups.lecture_id WHERE group_id=? AND date BETWEEN ? AND ?";
    private static final String GET_BY_GROUP_AND_PERIOD_PAGE_QUERY = "SELECT * FROM lectures INNER JOIN lecture_groups ON lectures.id = lecture_groups.lecture_id WHERE group_id=? AND date BETWEEN ? AND ? ORDER BY date OFFSET ? LIMIT ?";
    private static final String GET_COUNT_BY_TEACHER_AND_PERIOD = "SELECT COUNT(*) FROM lectures WHERE teacher_id=? AND date BETWEEN ? AND ?";
    private static final String GET_BY_TEACHER_AND_PERIOD_PAGE_QUERY = "SELECT * FROM lectures WHERE teacher_id=? AND date BETWEEN ? AND ? ORDER BY date OFFSET ? LIMIT ?";

    private final JdbcTemplate jdbcTemplate;
    private final LectureRowMapper lectureRowMapper;

    public JdbcLectureDao(JdbcTemplate jdbcTemplate, LectureRowMapper lectureRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lectureRowMapper = lectureRowMapper;
    }

    @Override
    public Optional<Lecture> get(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_QUERY, lectureRowMapper, id));
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
    public Page<Lecture> getAll(Pageable pageable) {
        int lectureQuantity = jdbcTemplate.queryForObject(GET_COUNT_QUERY, Integer.class);
        var lectures = jdbcTemplate.query(GET_PAGE_QUERY, lectureRowMapper,
                pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(lectures, pageable, lectureQuantity);
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
        var groups = this.get(updatedLecture.getId()).orElse(new Lecture()).getGroups();
        jdbcTemplate.update(UPDATE_QUERY, updatedLecture.getDate(), updatedLecture.getCourse().getId(), updatedLecture.getAudience().getId(),
                updatedLecture.getDuration().getId(), updatedLecture.getTeacher().getId(), updatedLecture.getId());
        groups.stream().filter(p -> !updatedLecture.getGroups().contains(p))
                .forEach(p -> jdbcTemplate.update(DELETE_LECTURE_GROUP, p.getId(), updatedLecture.getId()));
        updatedLecture.getGroups().stream().filter(p -> !groups.contains(p))
                .forEach(p -> jdbcTemplate.update(ADD_LECTURE_GROUP, p.getId(), updatedLecture.getId()));
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
    public Page<Lecture> getByGroupIdAndPeriod(Pageable pageable, int groupId, LocalDate startDate, LocalDate endDate) {
        int lectureQuantity = jdbcTemplate.queryForObject(GET_COUNT_BY_GROUP_AND_PERIOD, Integer.class, groupId, startDate, endDate);
        var lectures = jdbcTemplate.query(GET_BY_GROUP_AND_PERIOD_PAGE_QUERY, lectureRowMapper, groupId, startDate, endDate,
                pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(lectures, pageable, lectureQuantity);
    }

    @Override
    public List<Lecture> getByTeacherId(int teacherId) {
        return jdbcTemplate.query(GET_ALL_BY_TEACHER, lectureRowMapper, teacherId);
    }

    @Override
    public Page<Lecture> getByTeacherIdAndPeriod(Pageable pageable, int teacherId, LocalDate startDate, LocalDate endDate) {
        int lectureQuantity = jdbcTemplate.queryForObject(GET_COUNT_BY_TEACHER_AND_PERIOD, Integer.class, teacherId, startDate, endDate);
        var lectures = jdbcTemplate.query(GET_BY_TEACHER_AND_PERIOD_PAGE_QUERY, lectureRowMapper, teacherId, startDate, endDate,
                pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(lectures, pageable, lectureQuantity);
    }

    @Override
    public Optional<Lecture> getByGroupIdAndDateAndDurationId(int groupId, LocalDate date, int durationId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_BY_GROUP_DATE_DURATION, lectureRowMapper, groupId, date, durationId));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Lecture> getByTeacherIdAndDateAndDurationId(int teacherId, LocalDate date, int durationId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_BY_TEACHER_DATE_DURATION, lectureRowMapper, teacherId, date, durationId));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Lecture> getByAudienceIdAndDateAndDurationId(int audienceId, LocalDate date, int durationId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_BY_AUDIENCE_DATE_DURATION, lectureRowMapper, audienceId, date, durationId));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
}