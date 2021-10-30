package edu.shmonin.university.menu;

import edu.shmonin.university.model.Duration;
import edu.shmonin.university.service.DurationService;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Component
public class DurationManager {

    private final DurationService durationService;

    public DurationManager(DurationService durationService) {
        this.durationService = durationService;
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
                case ("a") -> durationService.create(createNewDuration());
                case ("b") -> durationService.delete(selectId());
                case ("c") -> durationService.update(updateDuration());
                case ("d") -> printDurations(durationService.getAll());
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
        var id = selectId();
        var duration = createNewDuration();
        duration.setId(id);
        return duration;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print duration id:");
        return scanner.nextInt();
    }

    public Duration selectDuration() {
        printDurations(durationService.getAll());
        return durationService.get(selectId());
    }
}