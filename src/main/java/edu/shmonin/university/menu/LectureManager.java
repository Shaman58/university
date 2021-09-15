package edu.shmonin.university.menu;

import edu.shmonin.university.dao.LectureDao;
import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Lecture;
import edu.shmonin.university.model.Student;
import edu.shmonin.university.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

    private Lecture selectLecture(List<Lecture> lectures) {
        var scanner = new Scanner(in);
        out.println("Print number:");
        var number = scanner.nextInt();
        while (number < 1 || number > lectures.size()) {
            out.println("Print correct number of lecture!");
        }
        return lectures.get(number - 1);
    }

    private void deleteLecture(List<Lecture> lectures) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of lecture:");
        var number = scanner.nextInt();
        lectures.remove(number - 1);
    }

    public List<Lecture> getLecturesForSchedule(List<Lecture> lectures, LocalDate startDate, LocalDate endDate, Teacher teacher) {
        return lectures.
                stream().
                filter(p -> p.getDate().isAfter(startDate) && p.getDate().isBefore(endDate) && (p.getTeacher().equals(teacher))).
                collect(Collectors.toList());
    }

    public List<Lecture> getLecturesForSchedule(List<Lecture> lectures, LocalDate startDate, LocalDate endDate, Student student) {
        return lectures.
                stream().
                filter(p -> p.getDate().isAfter(startDate) && p.getDate().isBefore(endDate) && (studentInGroups(p.getGroups(), student))).
                collect(Collectors.toList());
    }

    private boolean studentInGroups(List<Group> groups, Student student) {
        return true;//groups.stream().anyMatch(p -> p.getStudents().contains(student));
    }
}