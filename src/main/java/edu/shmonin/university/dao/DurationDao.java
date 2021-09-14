package edu.shmonin.university.dao;

import edu.shmonin.university.model.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DurationDao implements Dao<Duration> {

    private static final String GET_QUERY = "SELECT * FROM durations WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM durations";
    private static final String CREATE_QUERY = "INSERT INTO durations(start_time, end_time) VALUES (?,?)";
    private static final String UPDATE_QUERY = "UPDATE durations SET start_time=?, end_time=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM durations WHERE id=?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Duration get(int id) {
        return jdbcTemplate.query(GET_QUERY, new BeanPropertyRowMapper<>(Duration.class), id).
                stream().findAny().orElse(null);
    }

    @Override
    public List<Duration> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Duration.class));
    }

    @Override
    public void create(Duration entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getStartTime(), entity.getEndTime());
    }

    @Override
    public void update(Duration entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getStartTime(), entity.getEndTime(), entity.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}
