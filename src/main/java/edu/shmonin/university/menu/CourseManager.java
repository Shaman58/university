package edu.shmonin.university.menu;

import edu.shmonin.university.dao.JdbcCourseDao;
import edu.shmonin.university.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class CourseManager {

    private JdbcCourseDao jdbcCourseDao;

    @Autowired
    public void setCourseDao(JdbcCourseDao jdbcCourseDao) {
        this.jdbcCourseDao = jdbcCourseDao;
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
                case ("a") -> jdbcCourseDao.create(createNewCourse());
                case ("b") -> jdbcCourseDao.delete(selectId());
                case ("c") -> jdbcCourseDao.update(updateCourse());
                case ("d") -> printCourses(jdbcCourseDao.getAll());
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
        var id = selectId();
        var course = createNewCourse();
        course.setId(id);
        return course;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print course id:");
        return scanner.nextInt();
    }

    public Course selectCourse() {
        printCourses(jdbcCourseDao.getAll());
        return jdbcCourseDao.get(selectId());
    }
}