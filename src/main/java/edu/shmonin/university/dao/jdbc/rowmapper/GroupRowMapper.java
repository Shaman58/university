package edu.shmonin.university.dao.jdbc.rowmapper;

import edu.shmonin.university.dao.jdbc.JdbcStudentDao;
import edu.shmonin.university.model.Group;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupRowMapper implements RowMapper<Group> {

    private final JdbcStudentDao jdbcStudentDao;

    public GroupRowMapper(JdbcStudentDao jdbcStudentDao) {
        this.jdbcStudentDao = jdbcStudentDao;
    }

    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        var group = new Group();
        group.setId(resultSet.getInt("id"));
        group.setName(resultSet.getString("name"));
        group.setStudents(jdbcStudentDao.getByGroupId(group.getId()));
        group.getStudents().forEach(student -> student.setGroup(group));
        return group;
    }
}