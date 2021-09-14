package edu.shmonin.university.menu;

import edu.shmonin.university.dao.DurationDao;
import edu.shmonin.university.model.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class DurationManager {

    private DurationDao durationDao;

    @Autowired
    public void setDurationDao(DurationDao durationDao) {
        this.durationDao = durationDao;
    }

    public void manageDurations() {
        var scanner = new Scanner(in);
        var menuText = """
                DURATIONS
                Select menu item:
                a. Add duration
                b. Delete duration
                c. Update duration
                d. Print durations
                q. Close duration manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> durationDao.create(createNewDuration());
                case ("b") -> durationDao.delete(selectId());
                case ("c") -> durationDao.update(updateDuration());
                case ("d") -> printDurations(durationDao.getAll());
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printDurations(List<Duration> durations) {
        durations
                .forEach(p -> out.println(p.getId() + ". " + p.getStartTime() + " " + p.getEndTime()));
    }

    private Duration createNewDuration() {
        var scanner = new Scanner(in);
        out.println("Print start time of duration(hh:mm):");
        var startTime = LocalTime.parse(scanner.nextLine());
        out.println("Print end time of duration(hh:mm):");
        var endTime = LocalTime.parse(scanner.nextLine());
        return new Duration(startTime, endTime);
    }

    private Duration updateDuration() {
        var scanner = new Scanner(in);
        out.println("Print duration id to update:");
        var id = scanner.nextInt();
        var duration = createNewDuration();
        duration.setId(id);
        return duration;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print duration id:");
        return scanner.nextInt();
    }

    public Duration selectDuration(List<Duration> durations) {
        var scanner = new Scanner(in);
        out.println("Print number:");
        var number = scanner.nextInt();
        return durations.get(number - 1);
    }
}
