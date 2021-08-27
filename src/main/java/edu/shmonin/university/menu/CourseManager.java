package edu.shmonin.university.menu;

import edu.shmonin.university.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.in;
import static java.lang.System.out;

public class CourseManager {
    public void manageCourses(List<Course> courses) {
        var scanner = new Scanner(in);
        var menuText = """
                COURSES
                Select menu item:
                a. Add course
                b. Delete course
                c. Update course
                d. Print courses
                q. Close courses manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> courses.add(createNewCourse());
                case ("b") -> {
                    printCourses(courses);
                    out.println("Print number of course");
                    deleteCourse(courses);
                }
                case ("c") -> updateCourse(courses);
                case ("d") -> printCourses(courses);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printCourses(List<Course> courses) {
        var serial = new AtomicInteger(1);
        courses
                .forEach(p -> out.println(serial.getAndIncrement() + ". " + p.getName()));
    }

    private void updateCourse(List<Course> courses) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of course to update:");
        var number = scanner.nextInt();
        var updatedDuration = courses.get(number - 1);
        var course = createNewCourse();
        updatedDuration.setName(course.getName());
    }

    private Course createNewCourse() {
        var scanner = new Scanner(in);
        out.println("Print name of the course:");
        var name = scanner.nextLine();
        return new Course(name);
    }

    private void deleteCourse(List<Course> courses) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of course:");
        var number = scanner.nextInt();
        courses.remove(number - 1);
    }

    public Course selectCourse(List<Course> courses) {
        var scanner = new Scanner(in);
        out.println("Print number:");
        var number = scanner.nextInt();
        while (number < 1 || number > courses.size()) {
            out.println("Print correct number of course!");
        }
        return courses.get(number - 1);
    }

    public List<Course> selectCourses(List<Course> sourceCourses) {
        var targetCourses = new ArrayList<Course>();
        var scanner = new Scanner(in);
        var menuText = """
                COURSES
                Select menu item:
                a. Add course to the list
                b. Delete course from the list
                c. Print courses list
                q. Close teacher's courses manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> addCourseToList(targetCourses,sourceCourses);
                case ("b") -> {
                    printCourses(targetCourses);
                    deleteCourse(targetCourses);
                }
                case ("c") -> printCourses(targetCourses);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
        return targetCourses;
    }

    private void addCourseToList(List<Course> targetCourses, List<Course> sourceCourses) {
        var courseList = new ArrayList<>(sourceCourses);
        courseList.removeAll(targetCourses);
        printCourses(courseList);
        targetCourses.add(selectCourse(courseList));
    }
}