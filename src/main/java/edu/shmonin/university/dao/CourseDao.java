package edu.shmonin.university.dao;

import edu.shmonin.university.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDao implements Dao<Course> {

    private static final String GET_QUERY = "SELECT * FROM courses WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM courses";
    private static final String CREATE_QUERY = "INSERT INTO courses(name) VALUES(?)";
    private static final String UPDATE_QUERY = "UPDATE courses SET name=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM courses WHERE id=?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Course get(int id) {
        return jdbcTemplate.query(GET_QUERY, new BeanPropertyRowMapper<>(Course.class), id).
                stream().findAny().orElse(null);
    }

    @Override
    public List<Course> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Course.class));
    }

    @Override
    public void create(Course entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getName());
    }

    @Override
    public void update(Course entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getName(), entity.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }
}