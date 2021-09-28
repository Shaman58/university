package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.model.Audience;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcAudienceDao implements AudienceDao {

    private static final String GET_QUERY = "SELECT * FROM audiences WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM audiences";
    private static final String CREATE_QUERY = "INSERT INTO audiences(room_number, capacity) VALUES(?,?) RETURNING id";
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
    public Audience create(Audience audience) {
        var id = jdbcTemplate.queryForObject(CREATE_QUERY, Integer.class, audience.getRoomNumber(), audience.getCapacity());
        audience.setId(id);
        return audience;
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