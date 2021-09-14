package edu.shmonin.university.menu;

import edu.shmonin.university.dao.CourseDao;
import edu.shmonin.university.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class CourseManager {

    private CourseDao courseDao;

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public void manageCourses() {
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
                case ("a") -> courseDao.create(createNewCourse());
                case ("b") -> courseDao.delete(selectId());
                case ("c") -> courseDao.update(updateCourse());
                case ("d") -> printCourses(courseDao.getAll());
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printCourses(List<Course> courses) {
        courses.forEach(p -> out.println(p.getId() + ". " + p.getName()));
    }

    private Course createNewCourse() {
        var scanner = new Scanner(in);
        out.println("Print name of the course:");
        var name = scanner.nextLine();
        return new Course(name);
    }

    private Course updateCourse() {
        var scanner = new Scanner(in);
        out.println("Print course id:");
        var id = scanner.nextInt();
        var course = createNewCourse();
        course.setId(id);
        return course;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print course id:");
        return scanner.nextInt();
    }

    public Course selectCourse(List<Course> courses) {
        var scanner = new Scanner(in);
        out.println("Print course number:");
        var number = scanner.nextInt();
        while (number < 1 || number > courses.size()) {
            out.println("Print correct number of course!");
        }
        return courses.get(number - 1);
    }
}