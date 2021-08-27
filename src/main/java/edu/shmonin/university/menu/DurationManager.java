package edu.shmonin.university.menu;

import edu.shmonin.university.model.Duration;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.in;
import static java.lang.System.out;

public class DurationManager {
    public void manageDurations(List<Duration> durations) {
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
                case ("a") -> durations.add(createNewDuration());
                case ("b") -> {
                    printDurations(durations);
                    deleteDuration(durations);
                }
                case ("c") -> updateDuration(durations);
                case ("d") -> printDurations(durations);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printDurations(List<Duration> durations) {
        var serial = new AtomicInteger(1);
        durations
                .forEach(p -> out.println(serial.getAndIncrement() + ". " + p.getStartTime() + " " + p.getEndTime()));
    }

    private void updateDuration(List<Duration> durations) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of duration to update:");
        var number = scanner.nextInt();
        var updatedDuration = durations.get(number - 1);
        var duration = createNewDuration();
        updatedDuration.setStartTime(duration.getStartTime());
        updatedDuration.setEndTime(duration.getEndTime());
    }

    private Duration createNewDuration() {
        var scanner = new Scanner(in);
        out.println("Print start time of duration(hh:mm):");
        var startTime = LocalTime.parse(scanner.nextLine());
        out.println("Print end time of duration(hh:mm):");
        var endTime = LocalTime.parse(scanner.nextLine());
        return new Duration(startTime, endTime);
    }

    private void deleteDuration(List<Duration> durations) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of duration:");
        var number = scanner.nextInt();
        durations.remove(number - 1);
    }

    public Duration selectDuration(List<Duration> durations) {
        var scanner = new Scanner(in);
        out.println("Print number:");
        var number = scanner.nextInt();
        while (number < 1 || number > durations.size()) {
            out.println("Print correct number of duration!");
        }
        return durations.get(number - 1);
    }
}
