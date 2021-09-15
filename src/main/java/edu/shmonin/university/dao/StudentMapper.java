package edu.shmonin.university.dao;

import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class StudentMapper implements RowMapper<Student> {

    private GroupDao groupDao;

    @Autowired
    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override//как избавиться от дублирующего кода и вприципе от маппера?
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        var student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));
        student.setEmail(resultSet.getString("email"));
        student.setAddress(resultSet.getString("address"));
        student.setCountry(resultSet.getString("country"));
        student.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        student.setGender(Gender.valueOf(resultSet.getString("gender")));
        student.setPhone(resultSet.getString("phone"));
        student.setGroup(groupDao.get(resultSet.getInt("group_id")));
        return student;
    }
}
