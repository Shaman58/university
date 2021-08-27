package edu.shmonin.university.menu;

import edu.shmonin.university.model.ScientificDegree;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class ScientificDegreeManager {
    public ScientificDegree selectScientificDegree() {
        var scanner = new Scanner(in);
        var menuText = """
                DEGREES
                Select menu item:
                a. Bachelor
                b. Specialist
                c. Candidate
                d. Doctor
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        ScientificDegree degree = null;
        while (degree == null) {
            switch (inputKey) {
                case ("a") -> degree = ScientificDegree.BACHELOR;
                case ("b") -> degree = ScientificDegree.SPECIALIST;
                case ("c") -> degree = ScientificDegree.CANDIDATE;
                case ("d") -> degree = ScientificDegree.DOCTOR;
                default -> out.println("Input the right letter!");
            }
            inputKey = scanner.nextLine();
        }
        return degree;
    }
}
