package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.model.Holiday;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcHolidayDao implements HolidayDao {

    private static final String GET_QUERY = "SELECT * FROM holidays WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM holidays";
    private static final String CREATE_QUERY = "INSERT INTO holidays(name, date) VALUES(?,?)";
    private static final String UPDATE_QUERY = "UPDATE holidays SET name=?, date=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM holidays WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Holiday> holidayRowMapper;

    public JdbcHolidayDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.holidayRowMapper = BeanPropertyRowMapper.newInstance(Holiday.class);
    }

    @Override
    public Holiday get(int id) {
        return jdbcTemplate.queryForObject(GET_QUERY, holidayRowMapper, id);
    }

    @Override
    public List<Holiday> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, holidayRowMapper);
    }

    @Override
    public void create(Holiday holiday) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, holiday.getName());
            preparedStatement.setObject(2, holiday.getDate());
            return preparedStatement;
        }, keyHolder);
        holiday.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void update(Holiday holiday) {
        jdbcTemplate.update(UPDATE_QUERY, holiday.getName(), holiday.getDate(), holiday.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}