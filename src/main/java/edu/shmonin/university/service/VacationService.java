package edu.shmonin.university.service;

import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.exception.EntityNotFoundException;
import edu.shmonin.university.exception.ValidationException;
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
        var vacation = vacationDao.get(vacationId);
        if (vacation.isEmpty()) {
            throw new EntityNotFoundException("Can not find the vacation. There is no vacation with id=" + vacationId);
        }
        log.debug("Get vacation with id={}", vacationId);
        return vacation.get();
    }

    @Override
    public List<Vacation> getAll() {
        log.debug("Get all vacations");
        return vacationDao.getAll();
    }

    @Override
    public void create(Vacation vacation) {
        validateVacation(vacation);
        log.debug("Create vacation {}", vacation);
        vacationDao.create(vacation);
    }

    @Override
    public void update(Vacation vacation) {
        validateVacation(vacation);
        log.debug("Update vacation {}", vacation);
        vacationDao.update(vacation);
    }

    @Override
    public void delete(int vacationId) {
        if (vacationDao.get(vacationId).isEmpty()) {
            throw new EntityNotFoundException("Can not find the vacation. There is no vacation with id=" + vacationId);
        }
        log.debug("Delete vacation by id={}", vacationId);
        vacationDao.delete(vacationId);
    }

    public List<Vacation> getByTeacherId(int teacherId) {
        return vacationDao.getByTeacherId(teacherId);
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