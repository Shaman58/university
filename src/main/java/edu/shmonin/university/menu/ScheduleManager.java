package edu.shmonin.university.menu;

import edu.shmonin.university.dao.jdbc.JdbcLectureDao;
import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.model.Student;
import edu.shmonin.university.model.Teacher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.System.in;
import static java.lang.System.out;

@Component
public class ScheduleManager {

    private final JdbcLectureDao jdbcLectureDao;
    private final TeacherManager teacherManager;
    private final StudentManager studentManager;
    private final LectureManager lectureManager;


    public ScheduleManager(JdbcLectureDao jdbcLectureDao, TeacherManager teacherManager,
                           StudentManager studentManager, LectureManager lectureManager) {
        this.jdbcLectureDao = jdbcLectureDao;
        this.teacherManager = teacherManager;
        this.studentManager = studentManager;
        this.lectureManager = lectureManager;
    }

    public void scheduleManager() {
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
                    var teacher = teacherManager.selectTeacher();
                    var range = getDateRange();
                    lectureManager.printLectures(getLecturesForSchedule(jdbcLectureDao.getAll(), range.getStartDate(),
                            range.getEndDate(), teacher));
                }
                case ("b") -> {
                    var student = studentManager.selectStudent();
                    var range = getDateRange();
                    lectureManager.printLectures(getLecturesForSchedule(jdbcLectureDao.getAll(), range.getStartDate(),
                            range.getEndDate(), student));
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

    private List<Lecture> getLecturesForSchedule(List<Lecture> lectures, LocalDate startDate, LocalDate endDate, Teacher teacher) {
        return lectures.
                stream().
                filter(p -> p.getDate().isAfter(startDate) && p.getDate().isBefore(endDate) && (p.getTeacher().equals(teacher))).
                collect(Collectors.toList());
    }

    private List<Lecture> getLecturesForSchedule(List<Lecture> lectures, LocalDate startDate, LocalDate endDate, Student student) {
        return lectures.
                stream().
                filter(p -> p.getDate().isAfter(startDate) && p.getDate().isBefore(endDate) && (studentInGroups(p.getGroups(), student))).
                collect(Collectors.toList());
    }

    private boolean studentInGroups(List<Group> groups, Student student) {
        return groups.stream().anyMatch(p -> p.getStudents().contains(student));
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