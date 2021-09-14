package edu.shmonin.university.menu;

import edu.shmonin.university.model.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class LectureManager {
    public void manageLectures(List<Lecture> lectures, List<Course> courses, List<Audience> audiences, List<Duration> durations, List<Teacher> teachers, List<Group> groups) {
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
                case ("a") -> lectures.add(createNewLecture(courses, audiences, durations, teachers, groups));
                case ("b") -> {
                    printLectures(lectures);
                    deleteLecture(lectures);
                }
                case ("c") -> updateLecture(lectures, courses, audiences, durations, teachers, groups);
                case ("d") -> printLectures(lectures);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printLectures(List<Lecture> lectures) {
        var serial = new AtomicInteger(1);
        lectures.forEach(p -> out.printf("%d. %s %s %s %s %s %s %s%n",
                serial.getAndIncrement(),
                p.getDate(),
                p.getCourse().getName(),
                p.getAudience().getRoomNumber(),
                p.getDuration().getStartTime(),
                p.getDuration().getEndTime(),
                p.getTeacher().getFirstName(),
                p.getTeacher().getLastName()
        ));
    }

    private void updateLecture(List<Lecture> lectures, List<Course> courses, List<Audience> audiences, List<Duration> durations, List<Teacher> teachers, List<Group> groups) {
        var updatedLecture = selectLecture(lectures);
        var lecture = createNewLecture(courses, audiences, durations, teachers, groups);
        updatedLecture.setDate(lecture.getDate());
        updatedLecture.setCourse(lecture.getCourse());
        updatedLecture.setGroups(lecture.getGroups());
        updatedLecture.setAudience(lecture.getAudience());
        updatedLecture.setDuration(lecture.getDuration());
        updatedLecture.setTeacher(lecture.getTeacher());
    }

    private Lecture createNewLecture(List<Course> courses, List<Audience> audiences, List<Duration> durations, List<Teacher> teachers, List<Group> groups) {
        var scanner = new Scanner(in);
        var courseManager = new CourseManager();
        var durationManager = new DurationManager();
        var audienceManager = new AudienceManager();
        var teacherManager = new TeacherManager();
        var groupManager = new GroupManager();
        out.println("Print lecture date:");
        var date = LocalDate.parse(scanner.nextLine());
        courseManager.printCourses(courses);
        var course = new Course();//courseManager.selectCourse(courses);
        var targetGroups = groupManager.getLectureGroups(groups);
        audienceManager.printAudiences(audiences);
        var audience = audienceManager.selectAudience(audiences);
        durationManager.printDurations(durations);
        var duration = durationManager.selectDuration(durations);
        teacherManager.printTeachers(teachers);
        var teacher = teacherManager.selectTeacher(teachers);
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