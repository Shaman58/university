package edu.shmonin.university.menu;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class LectureManager {

    private CourseManager courseManager;
    private AudienceManager audienceManager;
    private TeacherManager teacherManager;
    private DurationManager durationManager;
    private GroupManager groupManager;
    private LectureDao lectureDao;

    @Autowired
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    @Autowired
    public void setAudienceManager(AudienceManager audienceManager) {
        this.audienceManager = audienceManager;
    }

    @Autowired
    public void setTeacherManager(TeacherManager teacherManager) {
        this.teacherManager = teacherManager;
    }

    @Autowired
    public void setDurationManager(DurationManager durationManager) {
        this.durationManager = durationManager;
    }

    @Autowired
    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Autowired
    public void setLectureDao(LectureDao lectureDao) {
        this.lectureDao = lectureDao;
    }

    public void manageLectures() {
        var scanner = new Scanner(in);
        var menuText = """
                LECTURES
                Select menu item:
                a. Add lecture
                b. Delete lecture
                c. Update lecture
                d. Print lecture
                q. Close lecture's manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> lectureDao.create(createNewLecture());
                case ("b") -> lectureDao.delete(selectId());
                case ("c") -> lectureDao.update(updateLecture());
                case ("d") -> printLectures(lectureDao.getAll());
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printLectures(List<Lecture> lectures) {
        lectures.forEach(p -> out.printf("%d. %s %s %s %s %s %s %s%n",
                p.getLectureId(),
                p.getDate(),
                p.getCourse().getName(),
                p.getAudience().getRoomNumber(),
                p.getDuration().getStartTime(),
                p.getDuration().getEndTime(),
                p.getTeacher().getFirstName(),
                p.getTeacher().getLastName()
        ));
    }

    private Lecture createNewLecture() {
        var scanner = new Scanner(in);
        out.println("Print lecture date:");
        var date = LocalDate.parse(scanner.nextLine());
        var course = courseManager.selectCourse();
        var targetGroups = groupManager.getLectureGroups();
        var audience = audienceManager.selectAudience();
        var duration = durationManager.selectDuration();
        var teacher = teacherManager.selectTeacher();
        return new Lecture(date, course, targetGroups, audience, duration, teacher);
    }

    private Lecture updateLecture() {
        var id = selectId();
        var lecture = createNewLecture();
        lecture.setLectureId(id);
        return lecture;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print course id:");
        return scanner.nextInt();
    }
}