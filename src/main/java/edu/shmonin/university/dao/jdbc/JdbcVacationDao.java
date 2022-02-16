package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.model.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcVacationDao implements VacationDao {

    private static final String GET_QUERY =
            "SELECT id,start_date,end_date FROM vacations WHERE id=?";
    private static final String GET_COUNT_QUERY = "SELECT COUNT(*) FROM vacations WHERE teacher_id=?";
    private static final String GET_ALL_QUERY =
            "SELECT id,start_date,end_date FROM vacations";
    private static final String CREATE_QUERY = "INSERT INTO vacations(start_date, end_date, teacher_id) VALUES (?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE vacations SET start_date=?, end_date=?, teacher_id=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM vacations WHERE id=?";
    private static final String GET_TEACHER_VACATIONS_QUERY =
            "SELECT id,start_date,end_date FROM vacations WHERE teacher_id=?";
    private static final String GET_TEACHER_DATE_VACATIONS_QUERY =
            "SELECT id,start_date,end_date FROM vacations WHERE teacher_id=? AND start_date<=? AND end_date>=?";
    private static final String GET_PAGE_QUERY = "SELECT * FROM vacations order by start_date OFFSET ? LIMIT ?";
    private static final String GET_PAGE_BY_TEACHER_ID_QUERY = "SELECT * FROM vacations WHERE teacher_id=? order by start_date OFFSET ? LIMIT ?";
    private static final String GET_TEACHER_YEAR_VACATIONS_QUERY = "SELECT * FROM vacations WHERE teacher_id=? AND start_date BETWEEN ? AND ?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Vacation> vacationRowMapper;

    public JdbcVacationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.vacationRowMapper = BeanPropertyRowMapper.newInstance(Vacation.class);
    }

    @Override
    public Optional<Vacation> get(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_QUERY, vacationRowMapper, id));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
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
    public Page<Vacation> getAll(Pageable pageable) {
        int vacationQuantity = jdbcTemplate.queryForObject(GET_COUNT_QUERY, Integer.class);
        var vacations = jdbcTemplate.query(GET_PAGE_QUERY, vacationRowMapper,
                pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(vacations, pageable, vacationQuantity);
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

    @Override
    public Optional<Vacation> getByTeacherIdAndDate(int teacherId, LocalDate date) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_TEACHER_DATE_VACATIONS_QUERY, vacationRowMapper, teacherId, date, date));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Vacation> getByTeacherId(Pageable pageable, int teacherId) {
        int vacationQuantity = jdbcTemplate.queryForObject(GET_COUNT_QUERY, Integer.class, teacherId);
        var vacations = jdbcTemplate.query(GET_PAGE_BY_TEACHER_ID_QUERY, vacationRowMapper, teacherId,
                pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(vacations, pageable, vacationQuantity);
    }

    @Override
    public List<Vacation> getByTeacherIdAndDateBetween(int teacherId, LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(GET_TEACHER_YEAR_VACATIONS_QUERY, vacationRowMapper, teacherId, startDate, endDate);
    }
}