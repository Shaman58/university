package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.model.Holiday;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcHolidayDao implements HolidayDao {

    private static final String GET_QUERY = "SELECT * FROM holidays WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM holidays";
    private static final String CREATE_QUERY = "INSERT INTO holidays(name, date) VALUES(?,?) RETURNING id";
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
    public Holiday create(Holiday holiday) {
        var id = jdbcTemplate.queryForObject(CREATE_QUERY, Integer.class, holiday.getName(), holiday.getDate());
        holiday.setId(id);
        return holiday;
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
