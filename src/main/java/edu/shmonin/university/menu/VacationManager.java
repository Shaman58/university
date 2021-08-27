package edu.shmonin.university.menu;

import edu.shmonin.university.model.Vacation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.in;
import static java.lang.System.out;

public class VacationManager {
    public List<Vacation> manageVacations() {
        var vacations = new ArrayList<Vacation>();
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
                case ("a") -> vacations.add(createVacation());
                case ("b") -> {
                    printVacations(vacations);
                    deleteVacation(vacations);
                }
                case ("c") -> printVacations(vacations);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
        return vacations;
    }

    private void printVacations(List<Vacation> vacations) {
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

    private void deleteVacation(List<Vacation> vacations) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of vacation:");
        var number = scanner.nextInt();
        vacations.remove(number - 1);
    }
}
