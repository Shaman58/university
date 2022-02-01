package edu.shmonin.university.dao.jdbc;

import edu.shmonin.university.dao.HolidayDao;
import edu.shmonin.university.model.Holiday;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class JdbcHolidayDao implements HolidayDao {

    private static final String GET_QUERY = "SELECT * FROM holidays WHERE id=?";
    private static final String GET_COUNT_QUERY = " SELECT COUNT(*) FROM holidays";
    private static final String GET_ALL_QUERY = "SELECT * FROM holidays";
    private static final String CREATE_QUERY = "INSERT INTO holidays(name, date) VALUES(?,?)";
    private static final String UPDATE_QUERY = "UPDATE holidays SET name=?, date=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM holidays WHERE id=?";
    private static final String GET_BY_DATE_QUERY = "SELECT * FROM holidays WHERE date=?";
    private static final String GET_PAGE_QUERY = "SELECT * FROM holidays order by name OFFSET ? LIMIT ?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Holiday> holidayRowMapper;

    public JdbcHolidayDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.holidayRowMapper = BeanPropertyRowMapper.newInstance(Holiday.class);
    }

    @Override
    public Optional<Holiday> get(int id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_QUERY, holidayRowMapper, id));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Holiday> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, holidayRowMapper);
    }

    @Override
    public Page<Holiday> getAllSortedPaginated(Pageable pageable) {
        int holidaysQuantity = jdbcTemplate.queryForObject(GET_COUNT_QUERY, Integer.class);
        var holidays = jdbcTemplate.query(GET_PAGE_QUERY, holidayRowMapper,
                pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(holidays, pageable, holidaysQuantity);
    }

    @Override
    public void create(Holiday holiday) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, holiday.getName());
            preparedStatement.setObject(2, holiday.getDate());
            return preparedStatement;
        }, keyHolder);
        holiday.setId((Integer) Objects.requireNonNull(keyHolder.getKeys().get("id")));
    }

    @Override
    public void update(Holiday holiday) {
        jdbcTemplate.update(UPDATE_QUERY, holiday.getName(), holiday.getDate(), holiday.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public Optional<Holiday> getByDate(LocalDate localDate) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GET_BY_DATE_QUERY, holidayRowMapper, localDate));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}