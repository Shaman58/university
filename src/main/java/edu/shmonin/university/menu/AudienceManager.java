package edu.shmonin.university.menu;

import edu.shmonin.university.dao.AudienceDao;
import edu.shmonin.university.model.Audience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class AudienceManager {

    private AudienceDao audienceDao;

    @Autowired
    public void setAudienceDao(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
    }

    public void manageAudiences() {
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
                case ("a") -> audienceDao.create(createNewAudience());
                case ("b") -> audienceDao.delete(selectId());
                case ("c") -> audienceDao.update(updateAudience());
                case ("d") -> printAudiences(audienceDao.getAll());
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printAudiences(List<Audience> audiences) {
        audiences
                .forEach(p -> out.println(p.getId() + ". " + p.getRoomNumber() + " capacity " + p.getCapacity()));
    }


    private Audience createNewAudience() {
        var scanner = new Scanner(in);
        out.println("Print audience number:");
        var number = scanner.nextInt();
        out.println("Print audience capacity:");
        var capacity = scanner.nextInt();
        return new Audience(number, capacity);
    }

    private Audience updateAudience() {
        var scanner = new Scanner(in);
        out.println("Print audience id:");
        var id = scanner.nextInt();
        var audience = createNewAudience();
        audience.setId(id);
        return audience;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print audience's id:");
        return scanner.nextInt();
    }

    public Audience selectAudience(List<Audience> audiences) {
        var scanner = new Scanner(in);
        out.println("Print room number:");
        var number = scanner.nextInt();
        while (number < 1 || number > audiences.size()) {
            out.println("Print correct number of audience!");
        }
        return audiences.get(number - 1);
    }
}