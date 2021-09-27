package edu.shmonin.university.dao;

import edu.shmonin.university.model.Teacher;
import edu.shmonin.university.model.Vacation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcVacationDao implements VacationDao {

    private static final String GET_QUERY =
            "SELECT id,start_date,end_date FROM vacations WHERE id=?";
    private static final String GET_ALL_QUERY =
            "SELECT id,start_date,end_date FROM vacations";
    private static final String CREATE_QUERY = "INSERT INTO vacations(start_date, end_date) VALUES (?,?) RETURNING id";
    private static final String UPDATE_QUERY = "UPDATE vacations SET start_date=?, end_date=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM vacations WHERE id=?";
    private static final String GET_TEACHER_VACATIONS_QUERY =
            "SELECT id,start_date,end_date FROM vacations WHERE teacher_id=?";
    private static final String SET_VACATION_TEACHER_QUERY = "UPDATE vacations SET teacher_id=? WHERE start_date=? AND end_date=?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Vacation> vacationRowMapper;

    @Autowired
    public JdbcVacationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.vacationRowMapper = BeanPropertyRowMapper.newInstance(Vacation.class);
    }

    @Override
    public Vacation get(int id) {
        return jdbcTemplate.queryForObject(GET_QUERY, vacationRowMapper, id);
    }

    @Override
    public List<Vacation> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, vacationRowMapper);
    }

    @Override
    public Vacation create(Vacation vacation) {
        var id = jdbcTemplate.queryForObject(CREATE_QUERY, Integer.class, vacation.getStartDate(), vacation.getEndDate());
        vacation.setId(id);
        return vacation;
    }

    @Override
    public void update(Vacation vacation) {
        jdbcTemplate.update(UPDATE_QUERY, vacation.getStartDate(), vacation.getEndDate(), vacation.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    public List<Vacation> getTeacherVacations(int teacherId) {
        return jdbcTemplate.query(GET_TEACHER_VACATIONS_QUERY, vacationRowMapper, teacherId);
    }

    @Override
    public void setTeacherVacation(Vacation vacation, Teacher teacher) {
        jdbcTemplate.update(SET_VACATION_TEACHER_QUERY, teacher.getId(), vacation.getStartDate(), vacation.getEndDate());
    }
}