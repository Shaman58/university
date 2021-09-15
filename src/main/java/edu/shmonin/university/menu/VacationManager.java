package edu.shmonin.university.menu;

import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.model.Teacher;
import edu.shmonin.university.model.Vacation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class VacationManager {

    private VacationDao vacationDao;

    @Autowired
    public void setVacationDao(VacationDao vacationDao) {
        this.vacationDao = vacationDao;
    }

    public void printVacations(List<Vacation> vacations) {
        vacations
                .forEach(p -> out.println(p.getId() + ". " + p.getStartDate() + " " + p.getEndDate()));
    }

    public void createVacation(Teacher teacher) {
        var scanner = new Scanner(in);
        out.println("Print start date of vacation(YYYY-MM-DD):");
        var startDate = LocalDate.parse(scanner.nextLine());
        out.println("Print end date of vacation(YYYY-MM-DD):");
        var endDate = LocalDate.parse(scanner.nextLine());
        vacationDao.create(new Vacation(startDate, endDate, teacher));
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print vacation's id:");
        return scanner.nextInt();
    }

    public Vacation selectTeacherVacation(Teacher teacher) {
        var vacations = vacationDao.getTeacherVacations(teacher);
        printVacations(vacations);
        var id = selectId();
        return vacations.stream().filter(p -> p.getId() == id).findAny().orElse(null);
    }
}