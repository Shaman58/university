package edu.shmonin.university.service;

import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.model.Vacation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VacationService implements EntityService<Vacation> {

    private final VacationDao jdbcVacationDao;

    public VacationService(VacationDao jdbcVacationDao) {
        this.jdbcVacationDao = jdbcVacationDao;
    }

    @Override
    public Vacation get(int vacationId) {
        return jdbcVacationDao.get(vacationId);
    }

    @Override
    public List<Vacation> getAll() {
        return jdbcVacationDao.getAll();
    }

    @Override
    public void create(Vacation vacation) {
        if (validateVacation(vacation)) {
            jdbcVacationDao.create(vacation);
        }
    }

    @Override
    public void update(Vacation vacation) {
        if (validateVacation(vacation)) {
            jdbcVacationDao.create(vacation);
        }
    }

    @Override
    public void delete(int vacationId) {
        jdbcVacationDao.delete(vacationId);
    }

    public List<Vacation> getByTeacherId(int teacherId) {
        return jdbcVacationDao.getByTeacherId(teacherId);
    }

    private boolean validateVacation(Vacation vacation) {
        return vacation.getStartDate().isAfter(LocalDate.now())
               && vacation.getEndDate().isAfter(vacation.getStartDate());
    }
}