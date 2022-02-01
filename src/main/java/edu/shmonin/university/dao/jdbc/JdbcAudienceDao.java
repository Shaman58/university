package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.model.Audience;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class JdbcAudienceDao implements AudienceDao {

    private static final String GET_QUERY = "SELECT * FROM audiences WHERE id=?";
    private static final String GET_COUNT_QUERY = "SELECT COUNT(*) FROM audiences";
    private static final String GET_ALL_QUERY = "SELECT * FROM audiences";
    private static final String CREATE_QUERY = "INSERT INTO audiences(room_number, capacity) VALUES(?,?)";
    private static final String UPDATE_QUERY = "UPDATE audiences SET room_number=?,capacity=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM audiences WHERE id=?";
    private static final String GET_PAGE_QUERY = "SELECT * FROM audiences order by room_number OFFSET ? LIMIT ?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Audience> audienceRowMapper;

    public JdbcAudienceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.audienceRowMapper = BeanPropertyRowMapper.newInstance(Audience.class);
    }

    @Override
    public Optional<Audience> get(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_QUERY, audienceRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Audience> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, audienceRowMapper);
    }

    @Override
    public Page<Audience> getAllSortedPaginated(Pageable pageable) {
        int audienceQuantity = jdbcTemplate.queryForObject(GET_COUNT_QUERY, Integer.class);
        var audiences = jdbcTemplate.query(GET_PAGE_QUERY, audienceRowMapper,
                pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(audiences, pageable, audienceQuantity);
    }

    @Override
    public void create(Audience audience) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, audience.getRoomNumber());
            preparedStatement.setInt(2, audience.getCapacity());
            return preparedStatement;
        }, keyHolder);
        audience.setId((Integer) Objects.requireNonNull(keyHolder.getKeys().get("id")));
    }

    @Override
    public void update(Audience audience) {
        jdbcTemplate.update(UPDATE_QUERY, audience.getRoomNumber(), audience.getCapacity(), audience.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}