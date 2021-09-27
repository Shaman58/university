package edu.shmonin.university.dao;

import edu.shmonin.university.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class GroupRowMapper implements RowMapper<Group> {

    private final JdbcStudentDao jdbcStudentDao;

    @Autowired
    public GroupRowMapper(JdbcStudentDao jdbcStudentDao) {
        this.jdbcStudentDao = jdbcStudentDao;
    }

    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        var group = new Group();
        group.setId(resultSet.getInt("id"));
        group.setName(resultSet.getString("name"));
        group.setStudents(jdbcStudentDao.selectStudentsRelatedToTheGroup(group.getId()));
        return group;
    }
}
