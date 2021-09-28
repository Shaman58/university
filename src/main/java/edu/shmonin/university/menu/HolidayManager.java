package edu.shmonin.university.menu;

import edu.shmonin.university.dao.jdbc.JdbcHolidayDao;
import edu.shmonin.university.model.Holiday;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Component
public class HolidayManager {

    private final JdbcHolidayDao jdbcHolidayDao;

    public HolidayManager(JdbcHolidayDao jdbcHolidayDao) {
        this.jdbcHolidayDao = jdbcHolidayDao;
    }

    public void manageHolidays() {
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
                case ("a") -> jdbcHolidayDao.create(createNewHoliday());
                case ("b") -> jdbcHolidayDao.delete(selectId());
                case ("c") -> jdbcHolidayDao.update(updateHoliday());
                case ("d") -> printHolidays(jdbcHolidayDao.getAll());
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printHolidays(List<Holiday> holidays) {
        holidays
                .forEach(p -> out.println(p.getId() + ". " + p.getName() + " " + p.getDate()));
    }

    private Holiday createNewHoliday() {
        var scanner = new Scanner(in);
        out.println("Print name of holiday:");
        var name = scanner.nextLine();
        out.println("Print date of holiday(YYYY-MM-DD):");
        var date = LocalDate.parse(scanner.next());
        return new Holiday(name, date);
    }

    private Holiday updateHoliday() {
        var id = selectId();
        var holiday = createNewHoliday();
        holiday.setId(id);
        return holiday;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print holiday's id:");
        return scanner.nextInt();
    }
}