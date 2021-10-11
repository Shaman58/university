package edu.shmonin.university;

import edu.shmonin.university.menu.*;
import org.springframework.stereotype.Service;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Service
public class MenuRunner {

    private final HolidayManager holidayManager;
    private final CourseManager courseManager;
    private final StudentManager studentManager;
    private final GroupManager groupManager;
    private final DurationManager durationManager;
    private final TeacherManager teacherManager;
    private final LectureManager lectureManager;
    private final AudienceManager audienceManager;
    private final ScheduleManager scheduleManager;

    public MenuRunner(HolidayManager holidayManager, CourseManager courseManager, StudentManager studentManager,
                      GroupManager groupManager, DurationManager durationManager, TeacherManager teacherManager,
                      LectureManager lectureManager, AudienceManager audienceManager, ScheduleManager scheduleManager) {
        this.holidayManager = holidayManager;
        this.courseManager = courseManager;
        this.studentManager = studentManager;
        this.groupManager = groupManager;
        this.durationManager = durationManager;
        this.teacherManager = teacherManager;
        this.lectureManager = lectureManager;
        this.audienceManager = audienceManager;
        this.scheduleManager = scheduleManager;
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
                case ("i") -> scheduleManager.scheduleManager();
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }
}