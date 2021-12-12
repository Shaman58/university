package edu.shmonin.university.service;

import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.exception.DateNotAvailableException;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.model.Vacation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VacationService implements EntityService<Vacation> {

    private static final Logger log = LoggerFactory.getLogger(VacationService.class);

    private final VacationDao vacationDao;

    public VacationService(VacationDao vacationDao) {
        this.vacationDao = vacationDao;
    }

    @Override
    public Vacation get(int vacationId) {
        log.debug("Get vacation with id={}", vacationId);
        return vacationDao.get(vacationId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find vacation by id=" + vacationId));
    }

    @Override
    public List<Vacation> getAll() {
        log.debug("Get all vacations");
        return vacationDao.getAll();
    }

    @Override
    public void create(Vacation vacation) {
        log.debug("Create vacation {}", vacation);
        validateVacation(vacation);
        vacationDao.create(vacation);
    }

    @Override
    public void update(Vacation vacation) {
        log.debug("Update vacation {}", vacation);
        validateVacation(vacation);
        vacationDao.update(vacation);
    }

    @Override
    public void delete(int vacationId) {
        log.debug("Delete vacation by id={}", vacationId);
        this.get(vacationId);
        vacationDao.delete(vacationId);
    }

    public List<Vacation> getByTeacherId(int teacherId) {
        log.debug("Get vacations with teacher id={}", teacherId);
        return vacationDao.getByTeacherId(teacherId);
    }

    private void validateVacation(Vacation vacation) {
        if (vacation.getStartDate().isBefore(LocalDate.now())) {
            throw new DateNotAvailableException("Vacation start date mast be after current date");
        }
        if (vacation.getEndDate().isBefore(vacation.getStartDate())) {
            throw new DateNotAvailableException("Vacation end date mast be after start date");
        }
    }
}