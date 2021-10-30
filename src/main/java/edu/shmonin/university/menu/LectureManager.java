package edu.shmonin.university.menu;

import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.service.LectureService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Component
public class LectureManager {

    private final CourseManager courseManager;
    private final AudienceManager audienceManager;
    private final TeacherManager teacherManager;
    private final DurationManager durationManager;
    private final GroupManager groupManager;
    private final LectureService lectureService;

    public LectureManager(CourseManager courseManager, AudienceManager audienceManager, TeacherManager teacherManager,
                          DurationManager durationManager, GroupManager groupManager, LectureService lectureService) {
        this.courseManager = courseManager;
        this.audienceManager = audienceManager;
        this.teacherManager = teacherManager;
        this.durationManager = durationManager;
        this.groupManager = groupManager;
        this.lectureService = lectureService;
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
                case ("a") -> lectureService.create(createNewLecture());
                case ("b") -> lectureService.delete(selectId());
                case ("c") -> lectureService.update(updateLecture());
                case ("d") -> printLectures(lectureService.getAll());
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printLectures(List<Lecture> lectures) {
        lectures.forEach(p -> out.printf("%d. %s %s %s %s %s %s %s%n",
                p.getId(),
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
        lecture.setId(id);
        return lecture;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print course id:");
        return scanner.nextInt();
    }
}