package edu.shmonin.university.menu;

import edu.shmonin.university.dao.StudentDao;
import edu.shmonin.university.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class StudentManager {

    private StudentDao studentDao;
    private GenderManager genderManager;

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Autowired
    public void setGenderManager(GenderManager genderManager) {
        this.genderManager = genderManager;
    }

    public void manageStudents() {
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
                case ("a") -> studentDao.create(createNewStudent());
                case ("b") -> studentDao.delete(selectId());
                case ("c") -> studentDao.update(updateStudent());
                case ("d") -> printStudents(studentDao.getAll());
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printStudents(List<Student> students) {
        students.forEach(p -> out.printf("%d. %s %s %s %s %s %s %s %s%n",
                p.getStudentId(),
                p.getFirstName(),
                p.getLastName(),
                p.getEmail(),
                p.getCountry(),
                p.getGender(),
                p.getPhone(),
                p.getAddress(),
                p.getBirthDate()));
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
        student.setGender(genderManager.selectGender());
        out.println("Print student's address:");
        student.setAddress(scanner.nextLine());
        out.println("Print student's phone:");
        student.setPhone(scanner.nextLine());
        out.println("Print student's birthdate:");
        student.setBirthDate(LocalDate.parse(scanner.nextLine()));
        return student;
    }

    private Student updateStudent() {
        var id = selectId();
        var student = createNewStudent();
        student.setStudentId(id);
        return student;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print student' id:");
        return scanner.nextInt();
    }

    public Student selectStudent() {
        printStudents(studentDao.getAll());
        return studentDao.get(selectId());
    }
}