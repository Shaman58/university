package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.GroupDao;
import edu.shmonin.university.dao.jdbc.rowmapper.GroupRowMapper;
import edu.shmonin.university.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcGroupDao implements GroupDao {

    private static final String GET_QUERY = "SELECT * FROM groups WHERE id=?";
    private static final String GET_COUNT_QUERY = " SELECT COUNT(*) FROM groups";
    private static final String GET_ALL_QUERY = "SELECT * FROM groups";
    private static final String CREATE_QUERY = "INSERT INTO groups(name) VALUES(?)";
    private static final String UPDATE_QUERY = "UPDATE groups SET name=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM groups WHERE id=?";
    private static final String GET_LECTURE_GROUPS_QUERY =
            "SELECT id,name FROM groups INNER JOIN lecture_groups ON groups.id = lecture_groups.group_id WHERE lecture_id =?";
    private static final String GET_PAGE_QUERY = "SELECT * FROM groups order by name OFFSET ? LIMIT ?";

    private final JdbcTemplate jdbcTemplate;
    private GroupRowMapper groupRowMapper;

    public JdbcGroupDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setGroupRowMapper(GroupRowMapper groupRowMapper) {
        this.groupRowMapper = groupRowMapper;
    }

    @Override
    public Optional<Group> get(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_QUERY, groupRowMapper, id));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, groupRowMapper);
    }

    @Override
    public Page<Group> getAll(Pageable pageable) {
        int groupsQuantity = jdbcTemplate.queryForObject(GET_COUNT_QUERY, Integer.class);
        var groups = jdbcTemplate.query(GET_PAGE_QUERY, groupRowMapper,
                pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(groups, pageable, groupsQuantity);
    }

    @Override
    public void create(Group group) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, group.getName());
            return preparedStatement;
        }, keyHolder);
        group.setId((Integer) Objects.requireNonNull(keyHolder.getKeys().get("id")));
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
    public List<Group> getByLectureId(int lectureId) {
        return jdbcTemplate.query(GET_LECTURE_GROUPS_QUERY, groupRowMapper, lectureId);
    }
}