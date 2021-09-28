package edu.shmonin.university.dao.rowmapper;

import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class StudentRowMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        var student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));
        student.setEmail(resultSet.getString("email"));
        student.setCountry(resultSet.getString("country"));
        student.setGender(Gender.valueOf(resultSet.getString("gender")));
        student.setPhone(resultSet.getString("phone"));
        student.setAddress(resultSet.getString("address"));
        student.setBirthDate(resultSet.getObject("birth_date", LocalDate.class));
        return student;
    }
}