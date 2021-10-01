package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.rowmapper.LectureRowMapper;
import edu.shmonin.university.model.Lecture;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcLectureDao implements LectureDao {

    private static final String GET_QUERY = "SELECT * FROM lectures WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM lectures";
    private static final String CREATE_QUERY = "INSERT INTO lectures(date, course_id, audience_id, duration_id, teacher_id) VALUES(?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE lectures SET date=?, course_id=?, audience_id=?, duration_id=?, teacher_id=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM lectures WHERE id=?";
    private static final String ADD_GROUP_LECTURE = "INSERT INTO groups_lectures(group_id, lecture_id) VALUES (?,?)";
    private static final String DELETE_GROUP_LECTURE = "DELETE FROM groups_lectures WHERE lecture_id=?";

    private final JdbcTemplate jdbcTemplate;
    private final LectureRowMapper lectureRowMapper;

    public JdbcLectureDao(JdbcTemplate jdbcTemplate, LectureRowMapper lectureRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lectureRowMapper = lectureRowMapper;
    }

    @Override
    public Lecture get(int id) {
        return jdbcTemplate.queryForObject(GET_QUERY, lectureRowMapper, id);
    }

    @Override
    public List<Lecture> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, lectureRowMapper);
    }

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
        lecture.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        lecture.getGroups().forEach(p -> jdbcTemplate.update(ADD_GROUP_LECTURE, p.getId(), lecture.getId()));
    }

    @Override
    public void update(Lecture lecture) {
        jdbcTemplate.update(UPDATE_QUERY, lecture.getDate(), lecture.getCourse().getId(), lecture.getAudience().getId(),
                lecture.getDuration().getId(), lecture.getTeacher().getId(), lecture.getId());
        jdbcTemplate.update(DELETE_GROUP_LECTURE, lecture.getId());
        lecture.getGroups().forEach(p -> jdbcTemplate.update(ADD_GROUP_LECTURE, p.getId(), lecture.getId()));
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}