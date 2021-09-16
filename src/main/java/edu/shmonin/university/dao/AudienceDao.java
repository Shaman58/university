package edu.shmonin.university.dao;

import edu.shmonin.university.model.Audience;
import edu.shmonin.university.model.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AudienceDao implements Dao<Audience> {

    private static final String GET_QUERY = "SELECT * FROM audiences WHERE audience_id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM audiences";
    private static final String CREATE_QUERY = "INSERT INTO audiences(room_number, capacity) VALUES(?,?)";
    private static final String UPDATE_QUERY = "UPDATE audiences SET room_number=?,capacity=? WHERE audience_id=?";
    private static final String DELETE_QUERY = "DELETE FROM audiences WHERE audience_id=?";
    private static final String GET_LECTURE_AUDIENCE = "SELECT * FROM audiences NATURAL JOIN lectures WHERE lecture_id=?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Audience get(int id) {
        return jdbcTemplate.query(GET_QUERY, new BeanPropertyRowMapper<>(Audience.class), id).
                stream().findAny().orElse(null);
    }

    @Override
    public List<Audience> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Audience.class));
    }

    @Override
    public void create(Audience entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getRoomNumber(), entity.getCapacity());
    }

    @Override
    public void update(Audience entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getCapacity(), entity.getRoomNumber(), entity.getAudienceId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    public Audience getLectureAudience(Lecture lecture) {
        return jdbcTemplate.query(GET_LECTURE_AUDIENCE, new BeanPropertyRowMapper<>(Audience.class), lecture.getLectureId()).
                stream().findAny().orElse(null);
    }
}