package edu.shmonin.university.dao;

import edu.shmonin.university.model.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureDao implements Dao<Lecture> {

    private static final String GET_QUERY = "SELECT * FROM lectures WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM lectures";
    private static final String CREATE_QUERY = "INSERT INTO lectures(date, course_id, audience_id, duration_id, teacher_id) VALUES(?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE lectures SET date=?, course_id=?, audience_id=?, duration_id=?, teacher_id=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM lectures WHERE id=?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Lecture get(int id) {
        return jdbcTemplate.query(GET_QUERY, new BeanPropertyRowMapper<>(Lecture.class)).
                stream().findAny().orElse(null);
    }

    @Override
    public List<Lecture> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Lecture.class));
    }

    @Override
    public void create(Lecture entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getDate(), entity.getCourse().getCourseId(), entity.getAudience().getId(),
                entity.getDuration().getId(), entity.getTeacher().getId());
    }

    @Override
    public void update(Lecture entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getDate(), entity.getCourse().getCourseId(), entity.getAudience().getId(),
                entity.getDuration().getId(), entity.getTeacher().getId(), entity.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}
