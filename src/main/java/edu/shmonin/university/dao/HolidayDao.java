package edu.shmonin.university.dao;

import edu.shmonin.university.model.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HolidayDao implements Dao<Holiday> {

    private static final String GET_QUERY = "SELECT * FROM holidays WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM holidays";
    private static final String CREATE_QUERY = "INSERT INTO holidays(name, date) VALUES(?,?)";
    private static final String UPDATE_QUERY = "UPDATE holidays SET name=?, date=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM holidays WHERE id=?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Holiday get(int id) {
        return jdbcTemplate.query(GET_QUERY, new BeanPropertyRowMapper<>(Holiday.class), id).
                stream().findAny().orElse(null);
    }

    @Override
    public List<Holiday> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Holiday.class));
    }

    @Override
    public void create(Holiday entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getName(), entity.getDate());
    }

    @Override
    public void update(Holiday entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getName(), entity.getDate(), entity.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}
