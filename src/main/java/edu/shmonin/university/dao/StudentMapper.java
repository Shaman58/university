package edu.shmonin.university.dao;

import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper implements RowMapper<Student> {
    @Override
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
        return student;
    }
}
