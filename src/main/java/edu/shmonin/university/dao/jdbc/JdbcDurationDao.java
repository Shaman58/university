package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.DurationDao;
import edu.shmonin.university.model.Duration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcDurationDao implements DurationDao {

    private static final String GET_QUERY = "SELECT * FROM durations WHERE id=?";
    private static final String GET_COUNT_QUERY = "SELECT COUNT(*) FROM durations";
    private static final String GET_ALL_QUERY = "SELECT * FROM durations";
    private static final String CREATE_QUERY = "INSERT INTO durations(start_time, end_time) VALUES (?,?)";
    private static final String UPDATE_QUERY = "UPDATE durations SET start_time=?, end_time=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM durations WHERE id=?";
    private static final String GET_PAGE_QUERY = "SELECT * FROM durations order by start_time LIMIT ? OFFSET ?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Duration> durationRowMapper;

    public JdbcDurationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.durationRowMapper = BeanPropertyRowMapper.newInstance(Duration.class);
    }

    @Override
    public Optional<Duration> get(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_QUERY, durationRowMapper, id));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Duration> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, durationRowMapper);
    }

    @Override
    public Page<Duration> getAll(Pageable pageable) {
        int durationQuantity = jdbcTemplate.queryForObject(GET_COUNT_QUERY, Integer.class);
        var durations = jdbcTemplate.query(GET_PAGE_QUERY, durationRowMapper,
                pageable.getPageSize(), pageable.getOffset());
        return new PageImpl<>(durations, pageable, durationQuantity);
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
        duration.setId((Integer) Objects.requireNonNull(keyHolder.getKeys().get("id")));
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