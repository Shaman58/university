package edu.shmonin.university.menu;

import edu.shmonin.university.model.Audience;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.in;
import static java.lang.System.out;

public class AudienceManager {
    public void manageAudiences(List<Audience> audiences) {
        var scanner = new Scanner(in);
        var menuText = """
                AUDIENCES
                Select menu item:
                a. Add audience
                b. Delete audience
                c. Update audience
                d. Print audience
                q. Close audience's manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> audiences.add(createNewAudience());
                case ("b") -> deleteAudience(audiences);
                case ("c") -> updateAudience(audiences);
                case ("d") -> printAudiences(audiences);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printAudiences(List<Audience> audiences) {
        var serial = new AtomicInteger(1);
        audiences
                .forEach(p -> out.println(serial.getAndIncrement() + ". " + p.getRoomNumber() + " capacity " + p.getCapacity()));
    }

    private void updateAudience(List<Audience> audiences) {
        var updatedAudience = selectAudience(audiences);
        var audience = createNewAudience();
        updatedAudience.setRoomNumber(audience.getRoomNumber());
        updatedAudience.setCapacity(audience.getCapacity());
    }

    private Audience createNewAudience() {
        var scanner = new Scanner(in);
        out.println("Print audience number:");
        var number = scanner.nextInt();
        out.println("Print audience capacity:");
        var capacity = scanner.nextInt();
        return new Audience(number, capacity);
    }

    private void deleteAudience(List<Audience> audiences) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of audience:");
        var number = scanner.nextInt();
        audiences.remove(number - 1);
    }

    public Audience selectAudience(List<Audience> audiences) {
        var scanner = new Scanner(in);
        out.println("Print number:");
        var number = scanner.nextInt();
        while (number < 1 || number > audiences.size()) {
            out.println("Print correct number of audience!");
        }
        return audiences.get(number - 1);
    }
}
