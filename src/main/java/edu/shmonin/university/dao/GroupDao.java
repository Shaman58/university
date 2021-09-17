package edu.shmonin.university.dao;

import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GroupDao implements Dao<Group> {

    private static final String GET_QUERY = "SELECT * FROM groups WHERE group_id=?";
    private static final String GET_ALL_QUERY = "SELECT * FROM groups";
    private static final String CREATE_QUERY = "INSERT INTO groups(name) VALUES(?)";
    private static final String UPDATE_QUERY = "UPDATE groups SET name=? WHERE group_id=?";
    private static final String DELETE_QUERY = "DELETE FROM groups WHERE group_id=?";
    private static final String GET_LECTURE_GROUPS_QUERY =
            "SELECT group_id, name FROM groups NATURAL JOIN groups_lectures WHERE lecture_id=?";

    private JdbcTemplate jdbcTemplate;
    private StudentDao studentDao;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Group get(int id) {
        return jdbcTemplate.query(GET_QUERY, new BeanPropertyRowMapper<>(Group.class), id).
                stream().peek(p -> p.setStudents(studentDao.selectStudentsRelatedToTheGroup(p))).
                findAny().orElse(null);
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new BeanPropertyRowMapper<>(Group.class)).
                stream().peek(p -> p.setStudents(studentDao.selectStudentsRelatedToTheGroup(p))).
                collect(Collectors.toList());
    }

    @Override
    public void create(Group entity) {
        jdbcTemplate.update(CREATE_QUERY, entity.getName());
    }

    @Override
    public void update(Group entity) {
        jdbcTemplate.update(UPDATE_QUERY, entity.getName(), entity.getGroupId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    public List<Group> getLectureGroups(Lecture lecture) {
        return jdbcTemplate.query(GET_LECTURE_GROUPS_QUERY, new BeanPropertyRowMapper<>(Group.class), lecture.getLectureId());
    }
}