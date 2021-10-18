package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.dao.jdbc.rowmapper.LectureRowMapper;
import edu.shmonin.university.model.Lecture;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    private static final String ADD_LECTURE_GROUP = "INSERT INTO lecture_groups(group_id, lecture_id) VALUES (?,?)";
    private static final String DELETE_LECTURE_GROUP = "DELETE FROM lecture_groups WHERE group_id=? AND lecture_id=?";

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
        lecture.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        lecture.getGroups().forEach(p -> jdbcTemplate.update(ADD_LECTURE_GROUP, p.getId(), lecture.getId()));
    }

    @Transactional
    @Override
    public void update(Lecture updatedLecture) {
        var groups = get(updatedLecture.getId()).getGroups();
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
}