package edu.shmonin.university.service;

import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ValidationException;
import edu.shmonin.university.model.Vacation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VacationService implements EntityService<Vacation> {

    private static final Logger log = LoggerFactory.getLogger(VacationService.class);

    private final VacationDao jdbcVacationDao;

    public VacationService(VacationDao jdbcVacationDao) {
        this.jdbcVacationDao = jdbcVacationDao;
    }

    @Override
    public Vacation get(int vacationId) {
        try {
            log.debug("Get vacation with id={}", vacationId);
            return jdbcVacationDao.get(vacationId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the vacation. There is no vacation with id=" + vacationId);
        }
    }

    @Override
    public List<Vacation> getAll() {
        log.debug("Get all vacations");
        return jdbcVacationDao.getAll();
    }

    @Override
    public void create(Vacation vacation) {
        validateVacation(vacation);
        log.debug("Create vacation {}", vacation);
        jdbcVacationDao.create(vacation);
    }

    @Override
    public void update(Vacation vacation) {
        validateVacation(vacation);
        log.debug("Update vacation {}", vacation);
        jdbcVacationDao.update(vacation);
    }

    @Override
    public void delete(int vacationId) {
        try {
            jdbcVacationDao.get(vacationId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Can not find the vacation. There is no vacation with id=" + vacationId);
        }
        log.debug("Delete vacation by id={}", vacationId);
        jdbcVacationDao.delete(vacationId);
    }

    public List<Vacation> getByTeacherId(int teacherId) {
        return jdbcVacationDao.getByTeacherId(teacherId);
    }

    private void validateVacation(Vacation vacation) {
        if (vacation.getStartDate().isBefore(LocalDate.now())) {
            throw new ValidationException("The vacation " + vacation + " did not pass the validity check. Vacation start date mast be after current date");
        }
        if (vacation.getEndDate().isBefore(vacation.getStartDate())) {
            throw new ValidationException("The vacation " + vacation + " did not pass the validity check. Vacation end date mast be after start date");
        }
    }
}