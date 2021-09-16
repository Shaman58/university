package edu.shmonin.university.dao;

import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class StudentMapper implements RowMapper<Student> {

    @Override//как избавиться от дублирующего кода?
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        var student = new Student();
        student.setStudentId(resultSet.getInt("student_id"));
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