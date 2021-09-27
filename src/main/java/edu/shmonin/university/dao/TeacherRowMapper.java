package edu.shmonin.university.dao;

import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.ScientificDegree;
import edu.shmonin.university.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TeacherRowMapper implements RowMapper<Teacher> {

    private final JdbcCourseDao jdbcCourseDao;
    private final JdbcVacationDao jdbcVacationDao;

    @Autowired
    public TeacherRowMapper(JdbcCourseDao jdbcCourseDao, JdbcVacationDao jdbcVacationDao) {
        this.jdbcCourseDao = jdbcCourseDao;
        this.jdbcVacationDao = jdbcVacationDao;
    }

    @Override
    public Teacher mapRow(ResultSet resultSet, int i) throws SQLException {
        var teacher = new Teacher();
        teacher.setId(resultSet.getInt("id"));
        teacher.setFirstName(resultSet.getString("first_name"));
        teacher.setLastName(resultSet.getString("last_name"));
        teacher.setEmail(resultSet.getString("email"));
        teacher.setCountry(resultSet.getString("country"));
        teacher.setGender(Gender.valueOf(resultSet.getString("gender")));
        teacher.setPhone(resultSet.getString("phone"));
        teacher.setAddress(resultSet.getString("address"));
        teacher.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        teacher.setScientificDegree(ScientificDegree.valueOf(resultSet.getString("scientific_degree")));
        teacher.setCourses(jdbcCourseDao.getTeacherCourses(teacher.getId()));
        teacher.setVacations(jdbcVacationDao.getTeacherVacations(teacher.getId()));
        return teacher;
    }
}
