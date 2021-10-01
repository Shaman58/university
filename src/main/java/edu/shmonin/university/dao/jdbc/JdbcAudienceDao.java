package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.model.Audience;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcAudienceDao implements AudienceDao {

    private static final String GET_QUERY = "SELECT * FROM audiences WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM audiences";
    private static final String CREATE_QUERY = "INSERT INTO audiences(room_number, capacity) VALUES(?,?)";
    private static final String UPDATE_QUERY = "UPDATE audiences SET room_number=?,capacity=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM audiences WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Audience> audienceRowMapper;

    public JdbcAudienceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.audienceRowMapper = BeanPropertyRowMapper.newInstance(Audience.class);
    }

    @Override
    public Audience get(int id) {
        return jdbcTemplate.queryForObject(GET_QUERY, audienceRowMapper, id);
    }

    @Override
    public List<Audience> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, audienceRowMapper);
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
        audience.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
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