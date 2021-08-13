package edu.shmonin.university;

import edu.shmonin.university.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.*;

public class MenuRunner {

    private final University university;

    public MenuRunner(University university) {
        this.university = university;
    }

    public void run() {
        var scanner = new Scanner(in);
        var menuText = """
                Select menu item:
                a. Manage holidays
                b. Manage students
                c. Manage teachers
                d. Manage groups
                e. Manage audiences
                f. Manage lectures
                g. Manage vacations
                h. Manage durations
                i. Manage courses
                q. quit
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> manageHolidays();

                case ("b") -> manageStudents();

                case ("c") -> manageTeachers();

                case ("d") -> manageGroups();

                case ("e") -> manageAudiences();

                case ("f") -> manageLectures();

                case ("g") -> manageVacations();

                case ("h") -> manageDurations();

                case ("i") -> manageCourses();

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void manageCourses() {
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
                case ("a") -> addCourse();

                case ("b") -> deleteCourse();

                case ("c") -> updateCourse();

                case ("d") -> printCourses();

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printCourses() {
        var serial = new AtomicInteger(1);
        university.getCourses()
                .forEach(p -> out.println(serial.getAndIncrement() + ". " + p.getName()));
    }

    private void updateCourse() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of course to update:");
        var number = scanner.nextInt();
        var updatedDuration = university.getCourses().get(number - 1);
        var course = createNewCourse();
        updatedDuration.setName(course.getName());
    }

    private void deleteCourse() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of course:");
        var number = scanner.nextInt();
        university.getCourses().remove(number - 1);
    }

    private void addCourse() {
        university.getCourses().add(createNewCourse());
    }

    private Course createNewCourse() {
        var scanner = new Scanner(in);
        out.println("Print name of the course:");
        var name = scanner.nextLine();
        return new Course(name);
    }

    private void manageDurations() {
        var scanner = new Scanner(in);
        var menuText = """
                DURATIONS
                Select menu item:
                a. Add duration
                b. Delete duration
                c. Update duration
                d. Print durations
                q. Close duration manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> addDuration();

                case ("b") -> deleteDuration();

                case ("c") -> updateDuration();

                case ("d") -> printDurations();

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printDurations() {
        var serial = new AtomicInteger(1);
        university.getDurations()
                .forEach(p -> out.println(serial.getAndIncrement() + ". " + p.getStartTime() + " " + p.getEndTime()));
    }

    private void updateDuration() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of duration to update:");
        var number = scanner.nextInt();
        var updatedDuration = university.getDurations().get(number - 1);
        var duration = createNewDuration();
        updatedDuration.setStartTime(duration.getStartTime());
        updatedDuration.setEndTime(duration.getEndTime());
    }

    private void deleteDuration() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of duration:");
        var number = scanner.nextInt();
        university.getDurations().remove(number - 1);
    }

    private void addDuration() {
        university.getDurations().add(createNewDuration());
    }

    private Duration createNewDuration() {
        var scanner = new Scanner(in);
        out.println("Print start time of duration(hh:mm):");
        var startTime = LocalTime.parse(scanner.nextLine());
        out.println("Print end time of duration(hh:mm):");
        var endTime = LocalTime.parse(scanner.nextLine());
        return new Duration(startTime, endTime);
    }

    private void manageVacations() {
    }

    private void manageLectures() {
    }

    private void manageAudiences() {
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
                case ("a") -> addAudience();

                case ("b") -> deleteAudience();

                case ("c") -> updateAudience();

                case ("d") -> printAudiences();

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printAudiences() {
        var serial = new AtomicInteger(1);
        university.getAudiences()
                .forEach(p -> out.println(serial.getAndIncrement() + ". " + p.getRoomNumber() + " capacity " + p.getCapacity()));
    }

    private void updateAudience() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of audience to update:");
        var number = scanner.nextInt();
        var updatedAudience = university.getAudiences().get(number - 1);
        var audience = createNewAudience();
        updatedAudience.setRoomNumber(audience.getRoomNumber());
        updatedAudience.setCapacity(audience.getCapacity());
    }

    private void deleteAudience() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of audience:");
        var number = scanner.nextInt();
        university.getAudiences().remove(number - 1);
    }

    private void addAudience() {
        university.getAudiences().add(createNewAudience());
    }

    private Audience createNewAudience() {
        var scanner = new Scanner(in);
        out.println("Print audience number:");
        var number = scanner.nextInt();
        out.println("Print audience capacity:");
        var capacity = scanner.nextInt();
        return new Audience(number, capacity);
    }

    private void manageGroups() {
        var scanner = new Scanner(in);
        var menuText = """
                GROUPS
                Select menu item:
                a. Add group
                b. Delete group
                c. Update group
                d. Print groups
                q. Close group's manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> addGroup();

                case ("b") -> deleteGroup();

                case ("c") -> updateGroup();

                case ("d") -> printGroups();

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printGroups() {
        var serial = new AtomicInteger(1);
        university.getGroups()
                .forEach(p -> out.printf("%d. %s%n%s", serial.getAndIncrement(), p.getName(), formatGroupStudents(p)));
    }

    private String formatGroupStudents(Group p) {
        var result = "";
        var serial = new AtomicInteger(1);
        for (Student student : p.getStudents()) {
            result = result.concat(String.format("  %d. %s %s %s %s %s %s %s %s%n",
                    serial.getAndIncrement(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getCountry(),
                    student.getGender(),
                    student.getPhone(),
                    student.getAddress(),
                    student.getBirthdate()));
        }
        return result;
    }

    private void updateGroup() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of group to update:");
        var number = scanner.nextInt();
        var updatedGroup = university.getGroups().get(number - 1);
        var audience = createNewGroup();
        updatedGroup.setName(audience.getName());
        updatedGroup.setStudents(audience.getStudents());
    }

    private void deleteGroup() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of group:");
        var number = scanner.nextInt();
        university.getGroups().remove(number - 1);
    }

    private void addGroup() {
        university.getGroups().add(createNewGroup());
    }

    private Group createNewGroup() {
        var scanner = new Scanner(in);
        var students = new ArrayList<Student>();
        out.println("Print name of group:");
        var name = scanner.nextLine();
        var menuText = """
                Select menu item:
                a. Add new student to the group
                b. Add student to the group
                q. quit
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> {
                    var student = createNewStudent();
                    university.getStudents().add(student);
                    students.add(student);
                }

                case ("b") -> {
                    out.println("Select student:");
                    var ableStudents = new ArrayList<>(university.getStudents());
                    ableStudents.removeAll(students);
                    printStudents(ableStudents);
                    var index = scanner.nextInt();
                    var targetStudent = ableStudents.get(index - 1);
                    students.add(targetStudent);
                    university.getGroups().forEach(p -> p.getStudents().remove(targetStudent));
                }

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
        return new Group(name, students);
    }

    private void manageTeachers() {
    }

    private void manageStudents() {
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
                case ("a") -> addStudent();

                case ("b") -> deleteStudent();

                case ("c") -> updateStudent();

                case ("d") -> printStudents(university.getStudents());

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printStudents(List<Student> students) {
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
                p.getBirthdate()));
    }

    private void updateStudent() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of student to update:");
        var number = scanner.nextInt();
        var updatedStudent = university.getStudents().get(number - 1);
        var student = createNewStudent();
        updatedStudent.setFirstName(student.getFirstName());
        updatedStudent.setLastName(student.getLastName());
        updatedStudent.setEmail(student.getEmail());
        updatedStudent.setCountry(student.getCountry());
        updatedStudent.setGender(student.getGender());
        updatedStudent.setPhone(student.getPhone());
        updatedStudent.setAddress(student.getAddress());
        updatedStudent.setBirthdate(student.getBirthdate());
    }

    private void deleteStudent() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of student:");
        var number = scanner.nextInt();
        university.getStudents().remove(number - 1);
    }

    private void addStudent() {
        university.getStudents().add(createNewStudent());
    }

    private Student createNewStudent() {
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
        student.setGender(scanner.nextLine());
        out.println("Print student's address:");
        student.setAddress(scanner.nextLine());
        out.println("Print student's phone:");
        student.setPhone(scanner.nextLong());
        scanner.nextLine();
        out.println("Print student's birthdate:");
        student.setBirthdate(LocalDate.parse(scanner.nextLine()));
        return student;
    }

    private void manageHolidays() {
        var scanner = new Scanner(in);
        var menuText = """
                HOLIDAYS
                Select menu item:
                a. Add holiday
                b. Delete holiday
                c. Update holiday
                d. Print holidays
                q. Close holiday's manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> addHoliday();

                case ("b") -> deleteHoliday();

                case ("c") -> updateHoliday();

                case ("d") -> printHolidays();

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printHolidays() {
        var serial = new AtomicInteger(1);
        university.getHolidays()
                .forEach(p -> out.println(serial.getAndIncrement() + ". " + p.getName() + " " + p.getDate()));
    }

    private void updateHoliday() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of holiday to update:");
        var number = scanner.nextInt();
        var updatedHoliday = university.getHolidays().get(number - 1);
        var holiday = createNewHoliday();
        updatedHoliday.setName(holiday.getName());
        updatedHoliday.setDate(holiday.getDate());
    }

    private void deleteHoliday() {
        var scanner = new Scanner(in);
        out.println("Print sequence number of holiday:");
        var number = scanner.nextInt();
        university.getHolidays().remove(number - 1);
    }

    private void addHoliday() {
        university.getHolidays().add(createNewHoliday());
    }

    private Holiday createNewHoliday() {
        var scanner = new Scanner(in);
        out.println("Print name of holiday:");
        var name = scanner.nextLine();
        out.println("Print date of holiday(YYYY-MM-DD):");
        var date = LocalDate.parse(scanner.next());
        return new Holiday(name, date);
    }

}