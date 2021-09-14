package edu.shmonin.university.dao;

import edu.shmonin.university.model.Vacation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VacationDao implements Dao<Vacation> {

    private static final String GET_QUERY = "SELECT * FROM vacations WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM vacations";
    private static final String CREATE_QUERY = "INSERT INTO vacations(start_date, end_date) VALUES (?,?)";
    private static final String UPDATE_QUERY = "UPDATE vacations SET start_date=?, end_date=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM vacations WHERE id=?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Vacation get(int id) {
        return jdbcTemplate.query(GET_QUERY, new BeanPropertyRowMapper<>(Vacation.class), id).
                stream().findAny().orElse(null);
    }

    @Override
    public List<Vacation> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Vacation.class));
    }

    @Override
    public void create(Vacation entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getStartDate(), entity.getEndDate());
    }

    @Override
    public void update(Vacation entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getStartDate(), entity.getEndDate(), entity.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}