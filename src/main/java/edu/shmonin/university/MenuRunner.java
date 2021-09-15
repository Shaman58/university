package edu.shmonin.university;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.menu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class MenuRunner {

    private HolidayManager holidayManager;
    private CourseManager courseManager;
    private StudentManager studentManager;
    private GroupManager groupManager;
    private DurationManager durationManager;
    private TeacherManager teacherManager;
    private LectureManager lectureManager;
    private AudienceManager audienceManager;

    @Autowired
    public void setHolidayManager(HolidayManager holidayManager) {
        this.holidayManager = holidayManager;
    }

    @Autowired
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    @Autowired
    public void setStudentManager(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    @Autowired
    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Autowired
    public void setDurationManager(DurationManager durationManager) {
        this.durationManager = durationManager;
    }

    @Autowired
    public void setTeacherManager(TeacherManager teacherManager) {
        this.teacherManager = teacherManager;
    }

    @Autowired
    public void setLectureManager(LectureManager lectureManager) {
        this.lectureManager = lectureManager;
    }

    @Autowired
    public void setAudienceManager(AudienceManager audienceManager) {
        this.audienceManager = audienceManager;
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
                case ("a") -> holidayManager.manageHolidays();
                case ("b") -> studentManager.manageStudents();
                case ("c") -> teacherManager.manageTeachers();
                case ("d") -> groupManager.manageGroups();
                case ("e") -> audienceManager.manageAudiences();
                case ("f") -> lectureManager.manageLectures();
                case ("g") -> durationManager.manageDurations();
                case ("h") -> courseManager.manageCourses();
                //case ("i") -> scheduleManager();
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

//    private void scheduleManager() {
//        var teacherManager = new TeacherManager();
//        var lectureManager = new LectureManager();
//        var studentManager = new StudentManager();
//        var scanner = new Scanner(in);
//        var menuText = """
//                Select menu item:
//                a. For teachers
//                b. For students
//                q. quit
//                Input menu letter:""";
//        out.println(menuText);
//        var inputKey = scanner.next();
//        while (!inputKey.equals("q")) {
//            switch (inputKey) {
//                case ("a") -> {
//                    teacherManager.printTeachers(university.getTeachers());
//                    var teacher = teacherManager.selectTeacher(university.getTeachers());
//                    var range = getDateRange();
//                    lectureManager.printLectures(lectureManager.getLecturesForSchedule(university.getLectures(), range.getStartDate(), range.getEndDate(), teacher));
//                }
//                case ("b") -> {
//                    studentManager.printStudents(university.getStudents());
//                    var student = studentManager.selectStudent(university.getStudents());
//                    var range = getDateRange();
//                    lectureManager.printLectures(lectureManager.getLecturesForSchedule(university.getLectures(), range.getStartDate(), range.getEndDate(), student));
//                }
//                default -> out.println("Input the right letter!");
//            }
//            out.println(menuText);
//            inputKey = scanner.next();
//        }
//    }
//
//    private DateRange getDateRange() {
//        var scanner = new Scanner(in);
//        var menuText = """
//                Select menu item:
//                a. For day
//                b. For month
//                q. quit
//                Input menu letter:""";
//        out.println(menuText);
//        var inputKey = scanner.nextLine();
//        while (!inputKey.equals("q")) {
//            switch (inputKey) {
//                case ("a") -> {
//                    out.println("Print date(YYYY-MM-DD):");
//                    var date = LocalDate.parse(scanner.nextLine());
//                    return new DateRange(date, date.plusDays(1));
//                }
//                case ("b") -> {
//                    out.println("Print start date(YYYY-MM-DD:");
//                    var date = LocalDate.parse(scanner.nextLine());
//                    return new DateRange(date, date.plusMonths(1));
//                }
//                default -> out.println("Input the right letter!");
//            }
//            out.println(menuText);
//            inputKey = scanner.next();
//        }
//        throw new NullPointerException();
//    }
}

//class DateRange {
//    private final LocalDate startDate;
//    private final LocalDate endDate;
//
//    public DateRange(LocalDate startDate, LocalDate endDate) {
//        this.startDate = startDate;
//        this.endDate = endDate;
//    }
//
//    public LocalDate getStartDate() {
//        return startDate;
//    }
//
//    public LocalDate getEndDate() {
//        return endDate;
//    }
//}