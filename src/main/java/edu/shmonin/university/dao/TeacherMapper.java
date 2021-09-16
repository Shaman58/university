package edu.shmonin.university.dao;

import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.ScientificDegree;
import edu.shmonin.university.model.Teacher;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TeacherMapper implements RowMapper<Teacher> {

    @Override//как избавиться от дублирующего кода и вприципе от маппера?
    public Teacher mapRow(ResultSet resultSet, int i) throws SQLException {
        var teacher = new Teacher();
        teacher.setTeacherId(resultSet.getInt("teacher_id"));
        teacher.setFirstName(resultSet.getString("first_name"));
        teacher.setLastName(resultSet.getString("last_name"));
        teacher.setEmail(resultSet.getString("email"));
        teacher.setAddress(resultSet.getString("address"));
        teacher.setCountry(resultSet.getString("country"));
        teacher.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        teacher.setGender(Gender.valueOf(resultSet.getString("gender")));
        teacher.setPhone(resultSet.getString("phone"));
        teacher.setScientificDegree(ScientificDegree.valueOf(resultSet.getString("scientific_degree")));
        return teacher;
    }
}
