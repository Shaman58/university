package edu.shmonin.university.dao.rowmapper;

import edu.shmonin.university.dao.jdbc.JdbcCourseDao;
import edu.shmonin.university.dao.jdbc.JdbcVacationDao;
import edu.shmonin.university.model.Gender;
import edu.shmonin.university.model.ScientificDegree;
import edu.shmonin.university.model.Teacher;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class TeacherRowMapper implements RowMapper<Teacher> {

    private final JdbcCourseDao jdbcCourseDao;
    private final JdbcVacationDao jdbcVacationDao;

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
        teacher.setBirthDate(resultSet.getObject("birth_date", LocalDate.class));
        teacher.setScientificDegree(ScientificDegree.valueOf(resultSet.getString("scientific_degree")));
        teacher.setCourses(jdbcCourseDao.getByTeacherId(teacher.getId()));
        teacher.setVacations(jdbcVacationDao.getTeacherVacations(teacher.getId()));
        return teacher;
    }
}