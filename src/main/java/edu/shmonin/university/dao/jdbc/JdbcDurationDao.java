package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.DurationDao;
import edu.shmonin.university.model.Duration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcDurationDao implements DurationDao {

    private static final String GET_QUERY = "SELECT * FROM durations WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM durations";
    private static final String CREATE_QUERY = "INSERT INTO durations(start_time, end_time) VALUES (?,?)";
    private static final String UPDATE_QUERY = "UPDATE durations SET start_time=?, end_time=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM durations WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Duration> durationRowMapper;

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
    public void create(Duration duration) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, duration.getStartTime());
            preparedStatement.setObject(2, duration.getEndTime());
            return preparedStatement;
        }, keyHolder);
        duration.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
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