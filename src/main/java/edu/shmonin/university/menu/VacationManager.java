package edu.shmonin.university.menu;

import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.model.Vacation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class VacationManager {

    private VacationDao vacationDao;

    @Autowired
    public void setVacationDao(VacationDao vacationDao) {
        this.vacationDao = vacationDao;
    }

    public void manageVacations() {
        var scanner = new Scanner(in);
        var menuText = """
                VACATIONS
                Select menu item:
                a. Add vacation to the list
                b. Delete vacation from the list
                c. Print vacation list
                q. Close teacher's vacation manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> vacationDao.create(createVacation());
                case ("b") -> vacationDao.delete(selectId());
                case ("c") -> printVacations(vacationDao.getAll());
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printVacations(List<Vacation> vacations) {
        var serial = new AtomicInteger(1);
        vacations
                .forEach(p -> out.println(serial.getAndIncrement() + ". " + p.getStartDate() + " " + p.getEndDate()));
    }

    private Vacation createVacation() {
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

    public Vacation selectVacation(List<Vacation> vacations) {
        var scanner = new Scanner(in);
        out.println("Print vacation's id:");
        var id = scanner.nextInt();
        return vacations.stream().filter(p -> p.getId() == id).findAny().orElse(null);
    }
}