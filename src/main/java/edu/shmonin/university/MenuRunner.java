package edu.shmonin.university;

import edu.shmonin.university.menu.*;

import java.time.LocalDate;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

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
                g. Manage durations
                h. Manage courses
                i. Schedule viewer
                q. quit
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> new HolidayManager().manageHolidays(university.getHolidays());
                case ("b") -> new StudentManager().manageStudents(university.getStudents());
                case ("c") -> new TeacherManager().manageTeachers(university.getTeachers(), university.getCourses());
                case ("d") -> new GroupManager().manageGroups(university.getGroups(), university.getStudents());
                case ("e") -> new AudienceManager().manageAudiences(university.getAudiences());
                case ("f") -> new LectureManager().manageLectures(university.getLectures(), university.getCourses(), university.getAudiences(), university.getDurations(), university.getTeachers(), university.getGroups());
                case ("g") -> new DurationManager().manageDurations(university.getDurations());
                case ("h") -> new CourseManager().manageCourses(university.getCourses());
                case ("i") -> scheduleManager();
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void scheduleManager() {
        var teacherManager = new TeacherManager();
        var lectureManager = new LectureManager();
        var studentManager = new StudentManager();
        var scanner = new Scanner(in);
        var menuText = """
                Select menu item:
                a. For teachers
                b. For students
                q. quit
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> {
                    teacherManager.printTeachers(university.getTeachers());
                    var teacher = teacherManager.selectTeacher(university.getTeachers());
                    var range = getDateRange();
                    lectureManager.printLectures(lectureManager.getLecturesForSchedule(university.getLectures(), range.getStartDate(), range.getEndDate(), teacher));
                }
                case ("b") -> {
                    studentManager.printStudents(university.getStudents());
                    var student = studentManager.selectStudent(university.getStudents());
                    var range = getDateRange();
                    lectureManager.printLectures(lectureManager.getLecturesForSchedule(university.getLectures(), range.getStartDate(), range.getEndDate(), student));
                }
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private DateRange getDateRange() {
        var scanner = new Scanner(in);
        var menuText = """
                Select menu item:
                a. For day
                b. For month
                q. quit
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.nextLine();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> {
                    out.println("Print date(YYYY-MM-DD):");
                    var date = LocalDate.parse(scanner.nextLine());
                    return new DateRange(date, date.plusDays(1));
                }
                case ("b") -> {
                    out.println("Print start date(YYYY-MM-DD:");
                    var date = LocalDate.parse(scanner.nextLine());
                    return new DateRange(date, date.plusMonths(1));
                }
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
        throw new NullPointerException();
    }
}

class DateRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}