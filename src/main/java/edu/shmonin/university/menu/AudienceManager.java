package edu.shmonin.university.menu;

import edu.shmonin.university.model.Audience;
import edu.shmonin.university.service.AudienceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Component
public class AudienceManager {

    private final AudienceService audienceService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AudienceManager(AudienceService audienceService) {
        this.audienceService = audienceService;
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
                case ("a") -> {
                    try {
                        audienceService.create(createNewAudience());
                        logger.info("The audience has been created");
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                case ("b") -> {
                    try {
                        audienceService.delete(selectId());
                        logger.info("The audience has been deleted");
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                case ("c") -> {
                    try {
                        audienceService.update(updateAudience());
                        logger.info("The audience has been updated");
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                case ("d") -> {
                    try {
                        var audiences = audienceService.getAll();
                        logger.info("The audiences were been got");
                        printAudiences(audiences);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
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
        var id = selectId();
        var audience = createNewAudience();
        audience.setId(id);
        return audience;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print audience's id:");
        return scanner.nextInt();
    }

    public Audience selectAudience() {
        printAudiences(audienceService.getAll());
        return audienceService.get(selectId());
    }
}