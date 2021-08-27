package edu.shmonin.university.menu;

import edu.shmonin.university.model.Holiday;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.in;
import static java.lang.System.out;

public class HolidayManager {
    public void manageHolidays(List<Holiday> holidays) {
        var scanner = new Scanner(in);
        var menuText = """
                HOLIDAYS
                Select menu item:
                a. Add holiday
                b. Delete holiday
                c. Update holiday
                d. Print holidays
                q. Close holiday's manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> holidays.add(createNewHoliday());
                case ("b") -> {
                    printHolidays(holidays);
                    deleteHoliday(holidays);
                }
                case ("c") -> updateHoliday(holidays);
                case ("d") -> printHolidays(holidays);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printHolidays(List<Holiday> holidays) {
        var serial = new AtomicInteger(1);
        holidays
                .forEach(p -> out.println(serial.getAndIncrement() + ". " + p.getName() + " " + p.getDate()));
    }

    private void updateHoliday(List<Holiday> holidays) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of holiday to update:");
        var number = scanner.nextInt();
        var updatedHoliday = holidays.get(number - 1);
        var holiday = createNewHoliday();
        updatedHoliday.setName(holiday.getName());
        updatedHoliday.setDate(holiday.getDate());
    }

    private Holiday createNewHoliday() {
        var scanner = new Scanner(in);
        out.println("Print name of holiday:");
        var name = scanner.nextLine();
        out.println("Print date of holiday(YYYY-MM-DD):");
        var date = LocalDate.parse(scanner.next());
        return new Holiday(name, date);
    }

    private void deleteHoliday(List<Holiday> holidays) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of holiday:");
        var number = scanner.nextInt();
        holidays.remove(number - 1);
    }
}
