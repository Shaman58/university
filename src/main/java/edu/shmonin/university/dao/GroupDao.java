package edu.shmonin.university.dao;

import edu.shmonin.university.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupDao implements Dao<Group> {

    private static final String GET_QUERY = "SELECT * FROM groups WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM groups";
    private static final String CREATE_QUERY = "INSERT INTO groups(name) VALUES(?)";
    private static final String UPDATE_QUERY = "UPDATE groups SET name=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM groups WHERE id=?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Group get(int id) {
        return jdbcTemplate.query(GET_QUERY, new BeanPropertyRowMapper<>(Group.class), id).
                stream().findAny().orElse(null);
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Group.class));
    }

    @Override
    public void create(Group entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getName());
    }

    @Override
    public void update(Group entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getName(), entity.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}
