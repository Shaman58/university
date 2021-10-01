package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.model.Teacher;
import edu.shmonin.university.model.Vacation;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcVacationDao implements VacationDao {

    private static final String GET_QUERY =
            "SELECT id,start_date,end_date FROM vacations WHERE id=?";
    private static final String GET_ALL_QUERY =
            "SELECT id,start_date,end_date FROM vacations";
    private static final String CREATE_QUERY = "INSERT INTO vacations(start_date, end_date) VALUES (?,?)";
    private static final String UPDATE_QUERY = "UPDATE vacations SET start_date=?, end_date=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM vacations WHERE id=?";
    private static final String GET_TEACHER_VACATIONS_QUERY =
            "SELECT id,start_date,end_date FROM vacations WHERE teacher_id=?";
    private static final String SET_VACATION_TEACHER_QUERY = "UPDATE vacations SET teacher_id=? WHERE start_date=? AND end_date=?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Vacation> vacationRowMapper;

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
    public void create(Vacation vacation) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, vacation.getStartDate());
            preparedStatement.setObject(2, vacation.getEndDate());
            return preparedStatement;
        }, keyHolder);
        vacation.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
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