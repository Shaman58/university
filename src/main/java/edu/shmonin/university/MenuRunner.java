package edu.shmonin.university;

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
    private ScheduleManager scheduleManager;

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

    @Autowired
    public void setScheduleManager(ScheduleManager scheduleManager) {
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