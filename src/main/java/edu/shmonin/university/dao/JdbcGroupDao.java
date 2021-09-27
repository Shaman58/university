package edu.shmonin.university.dao;

import edu.shmonin.university.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcGroupDao implements GroupDao {

    private static final String GET_QUERY = "SELECT * FROM groups WHERE id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM groups";
    private static final String CREATE_QUERY = "INSERT INTO groups(name) VALUES(?) RETURNING id";
    private static final String UPDATE_QUERY = "UPDATE groups SET name=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM groups WHERE id=?";
    private static final String GET_LECTURE_GROUPS_QUERY =
            "SELECT id,name FROM groups INNER JOIN groups_lectures ON groups.id = groups_lectures.group_id WHERE lecture_id =?";

    private final JdbcTemplate jdbcTemplate;
    private final GroupRowMapper groupRowMapper;

    @Autowired
    public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupRowMapper groupRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupRowMapper = groupRowMapper;
    }

    @Override
    public Group get(int id) {
        return jdbcTemplate.queryForObject(GET_QUERY, groupRowMapper, id);
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, groupRowMapper);
    }

    @Override
    public Group create(Group group) {
        var id = jdbcTemplate.queryForObject(CREATE_QUERY, Integer.class, group.getName());
        group.setId(id);
        return group;
    }

    @Override
    public void update(Group group) {
        jdbcTemplate.update(UPDATE_QUERY, group.getName(), group.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public List<Group> getLectureGroups(int lectureId) {
        return jdbcTemplate.query(GET_LECTURE_GROUPS_QUERY, groupRowMapper, lectureId);
    }
}