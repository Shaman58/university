package edu.shmonin.university.menu;

import edu.shmonin.university.dao.jdbc.JdbcVacationDao;
import edu.shmonin.university.model.Teacher;
import edu.shmonin.university.model.Vacation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Component
public class VacationManager {

    private final JdbcVacationDao jdbcVacationDao;

    public VacationManager(JdbcVacationDao jdbcVacationDao) {
        this.jdbcVacationDao = jdbcVacationDao;
    }

    public void printVacations(List<Vacation> vacations) {
        vacations
                .forEach(p -> out.println(p.getId() + ". " + p.getStartDate() + " " + p.getEndDate()));
    }

    public Vacation createVacation() {
        var scanner = new Scanner(in);
        out.println("Print start date of vacation(YYYY-MM-DD):");
        var startDate = LocalDate.parse(scanner.nextLine());
        out.println("Print end date of vacation(YYYY-MM-DD):");
        var endDate = LocalDate.parse(scanner.nextLine());
        return new Vacation(startDate, endDate);
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print vacation's id:");
        return scanner.nextInt();
    }

    public Vacation selectTeacherVacation(Teacher teacher) {
        var vacations = jdbcVacationDao.getByTeacherId(teacher.getId());
        printVacations(vacations);
        var id = selectId();
        return vacations.stream().filter(p -> p.getId() == id).findAny().orElse(null);
    }
}