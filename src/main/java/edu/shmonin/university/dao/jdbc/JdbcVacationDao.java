package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.VacationDao;
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
    private static final String CREATE_QUERY = "INSERT INTO vacations(start_date, end_date, teacher_id) VALUES (?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE vacations SET start_date=?, end_date=?, teacher_id=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM vacations WHERE id=?";
    private static final String GET_TEACHER_VACATIONS_QUERY =
            "SELECT id,start_date,end_date FROM vacations WHERE teacher_id=?";

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
            preparedStatement.setObject(3, vacation.getTeacher() == null ? null : vacation.getTeacher().getId());
            return preparedStatement;
        }, keyHolder);
        vacation.setId((Integer) Objects.requireNonNull(keyHolder.getKeys().get("id")));
    }

    @Override
    public void update(Vacation vacation) {
        jdbcTemplate.update(UPDATE_QUERY, vacation.getStartDate(), vacation.getEndDate(), vacation.getTeacher() == null ? null : vacation.getTeacher().getId(), vacation.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public List<Vacation> getByTeacherId(int teacherId) {
        return jdbcTemplate.query(GET_TEACHER_VACATIONS_QUERY, vacationRowMapper, teacherId);
    }
}