package edu.shmonin.university.menu;

import edu.shmonin.university.model.Gender;
import org.springframework.stereotype.Component;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Component
public class GenderManager {
    public Gender selectGender() {
        var scanner = new Scanner(in);
        var menuText = """
                GENDERS
                Select menu item:
                a. Male
                b. Female
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        Gender gender = null;
        while (gender == null) {
            switch (inputKey) {
                case ("a") -> gender = Gender.MALE;
                case ("b") -> gender = Gender.FEMALE;
                default -> out.println("Input the right letter!");
            }
            inputKey = scanner.nextLine();
        }
        return gender;
    }
}