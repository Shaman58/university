package edu.shmonin.university.menu;

import edu.shmonin.university.model.Course;
import edu.shmonin.university.model.Teacher;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.in;
import static java.lang.System.out;

public class TeacherManager {
    public void manageTeachers(List<Teacher> teachers, List<Course> courses) {
        var scanner = new Scanner(in);
        var menuText = """
                TEACHERS
                Select menu item:
                a. Add teacher
                b. Delete teacher
                c. Update teacher
                d. Print teachers
                q. Close teacher's manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> teachers.add(createNewTeacher(courses));
                case ("b") -> deleteTeacher(teachers);
                case ("c") -> updateTeacher(teachers, courses);
                case ("d") -> printTeachers(teachers);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printTeachers(List<Teacher> teachers) {
        var serial = new AtomicInteger(1);
        teachers.forEach(p -> out.printf("%d. %s %s %s %s %s %s %s %s %s%n",
                serial.getAndIncrement(),
                p.getFirstName(),
                p.getLastName(),
                p.getEmail(),
                p.getCountry(),
                p.getGender(),
                p.getPhone(),
                p.getAddress(),
                p.getScientificDegree(),
                p.getBirthDate()));
    }

    private void updateTeacher(List<Teacher> teachers, List<Course> courses) {
        var updatedTeacher = selectTeacher(teachers);
        var teacher = createNewTeacher(courses);
        updatedTeacher.setFirstName(teacher.getFirstName());
        updatedTeacher.setLastName(teacher.getLastName());
        updatedTeacher.setEmail(teacher.getEmail());
        updatedTeacher.setCountry(teacher.getCountry());
        updatedTeacher.setGender(teacher.getGender());
        updatedTeacher.setPhone(teacher.getPhone());
        updatedTeacher.setAddress(teacher.getAddress());
        updatedTeacher.setScientificDegree(teacher.getScientificDegree());
        updatedTeacher.setBirthDate(teacher.getBirthDate());
        updatedTeacher.setCourses(teacher.getCourses());
        updatedTeacher.setVacations(teacher.getVacations());
    }

    private Teacher createNewTeacher(List<Course> courses) {
        var scanner = new Scanner(in);
        var teacher = new Teacher();
        var vacationManager = new VacationManager();
        var courseManager = new CourseManager();
        out.println("Print teacher's firstname:");
        teacher.setFirstName(scanner.nextLine());
        out.println("Print teacher's lastname:");
        teacher.setLastName(scanner.nextLine());
        out.println("Print teacher's email:");
        teacher.setEmail(scanner.nextLine());
        out.println("Print teacher's country:");
        teacher.setCountry(scanner.nextLine());
        out.println("Select teacher's gender:");
        teacher.setGender(new GenderManager().selectGender());
        out.println("Print teacher's address:");
        teacher.setAddress(scanner.nextLine());
        out.println("Print teacher's phone:");
        teacher.setPhone(scanner.nextLine());
        out.println("Select teacher's scientific degree:");
        teacher.setScientificDegree(new ScientificDegreeManager().selectScientificDegree());
        out.println("Print teacher's birthdate(YYYY-MM-DD):");
        teacher.setBirthDate(LocalDate.parse(scanner.nextLine()));
        out.println("Select courses:");
        teacher.setCourses(courseManager.selectCourses(courses));
        teacher.setVacations(vacationManager.manageVacations());
        return teacher;
    }

    private void deleteTeacher(List<Teacher> teachers) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of teacher:");
        var number = scanner.nextInt();
        teachers.remove(number - 1);
    }

    public Teacher selectTeacher(List<Teacher> teachers) {
        var scanner = new Scanner(in);
        out.println("Print number:");
        var number = scanner.nextInt();
        while (number < 1 || number > teachers.size()) {
            out.println("Print correct number of teacher!");
        }
        return teachers.get(number - 1);
    }
}