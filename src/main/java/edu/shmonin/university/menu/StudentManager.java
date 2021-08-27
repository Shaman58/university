package edu.shmonin.university.menu;

import edu.shmonin.university.model.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.in;
import static java.lang.System.out;

public class StudentManager {
    public void manageStudents(List<Student> students) {
        var scanner = new Scanner(in);
        var menuText = """
                STUDENTS
                Select menu item:
                a. Add student
                b. Delete student
                c. Update student
                d. Print students
                q. Close student's manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> students.add(createNewStudent());
                case ("b") -> deleteStudent(students);
                case ("c") -> updateStudent(students);
                case ("d") -> printStudents(students);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printStudents(List<Student> students) {
        var serial = new AtomicInteger(1);
        students.forEach(p -> out.printf("%d. %s %s %s %s %s %s %s %s%n",
                serial.getAndIncrement(),
                p.getFirstName(),
                p.getLastName(),
                p.getEmail(),
                p.getCountry(),
                p.getGender(),
                p.getPhone(),
                p.getAddress(),
                p.getBirthDate()));
    }

    private void updateStudent(List<Student> students) {
        var updatedStudent = selectStudent(students);
        var student = createNewStudent();
        updatedStudent.setFirstName(student.getFirstName());
        updatedStudent.setLastName(student.getLastName());
        updatedStudent.setEmail(student.getEmail());
        updatedStudent.setCountry(student.getCountry());
        updatedStudent.setGender(student.getGender());
        updatedStudent.setPhone(student.getPhone());
        updatedStudent.setAddress(student.getAddress());
        updatedStudent.setBirthDate(student.getBirthDate());
    }

    public Student createNewStudent() {
        var scanner = new Scanner(in);
        var student = new Student();
        out.println("Print student's firstname:");
        student.setFirstName(scanner.nextLine());
        out.println("Print student's lastname:");
        student.setLastName(scanner.nextLine());
        out.println("Print student's email:");
        student.setEmail(scanner.nextLine());
        out.println("Print student's country:");
        student.setCountry(scanner.nextLine());
        out.println("Print student's gender:");
        student.setGender(new GenderManager().selectGender());
        out.println("Print student's address:");
        student.setAddress(scanner.nextLine());
        out.println("Print student's phone:");
        student.setPhone(scanner.nextLine());
        out.println("Print student's birthdate:");
        student.setBirthDate(LocalDate.parse(scanner.nextLine()));
        return student;
    }

    private void deleteStudent(List<Student> students) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of audience:");
        var number = scanner.nextInt();
        students.remove(number - 1);
    }

    public Student selectStudent(List<Student> students) {
        var scanner = new Scanner(in);
        out.println("Print number:");
        var number = scanner.nextInt();
        while (number < 1 || number > students.size()) {
            out.println("Print correct number of student!");
        }
        return students.get(number - 1);
    }
}
