package edu.shmonin.university.dao;

import edu.shmonin.university.model.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcDurationDao implements DurationDao {

    private static final String GET_QUERY = "SELECT * FROM durations WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM durations";
    private static final String CREATE_QUERY = "INSERT INTO durations(start_time, end_time) VALUES (?,?) RETURNING id";
    private static final String UPDATE_QUERY = "UPDATE durations SET start_time=?, end_time=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM durations WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Duration> durationRowMapper;

    @Autowired
    public JdbcDurationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.durationRowMapper = BeanPropertyRowMapper.newInstance(Duration.class);
    }

    @Override
    public Duration get(int id) {
        return jdbcTemplate.queryForObject(GET_QUERY, durationRowMapper, id);
    }

    @Override
    public List<Duration> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, durationRowMapper);
    }

    @Override
    public Duration create(Duration duration) {
        var id = jdbcTemplate.queryForObject(CREATE_QUERY,Integer.class, duration.getStartTime(), duration.getEndTime());
        duration.setId(id);
        return duration;
    }

    @Override
    public void update(Duration duration) {
        jdbcTemplate.update(UPDATE_QUERY, duration.getStartTime(), duration.getEndTime(), duration.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}