package edu.shmonin.university;

import edu.shmonin.university.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
                g. Manage durations
                h. Manage courses
                i. Schedule viewer
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

                case ("g") -> manageDurations();

                case ("h") -> manageCourses();

                case ("i") -> scheduleManager();

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void scheduleManager() {
        var scanner = new Scanner(in);
        var menuText = """
                Select menu item:
                a. For teachers day schedule
                b. For teachers month schedule
                c. For students day schedule
                d. For students month schedule
                q. quit
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> {
                    printTeachers(university.getTeachers());
                    var human = (Human) selectObject(university.getTeachers());
                    var range = getDateRange();
                    printLectures(getLecturesForSchedule(range.getStartDate(), range.getEndDate(), human));
                }

                case ("b") -> {
                    printStudents(university.getStudents());
                    var human = (Human) selectObject(university.getStudents());
                    var range = getDateRange();
                    printLectures(getLecturesForSchedule(range.getStartDate(), range.getEndDate(), human));
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
        var inputKey = scanner.next();
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

    private List<Lecture> getLecturesForSchedule(LocalDate startDate, LocalDate endDate, Human human) {
        return university.
                getLectures().
                stream().
                filter(p -> p.getDate().isAfter(startDate) && p.getDate().isBefore(endDate) && (p.getTeacher().equals(human) || studentInGroups(p.getGroups(), human))).
                collect(Collectors.toList());
    }

    private boolean studentInGroups(List<Group> groups, Human student) {
        for (Group group : groups) {
            if (group.getStudents().contains((Student) student)) {
                return true;
            }
        }
        return false;
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
                case ("a") -> university.getCourses().add(createNewCourse());

                case ("b") -> {
                    printCourses(university.getCourses());
                    out.println("Print number of course");
                    deleteObject(university.getCourses());
                }

                case ("c") -> updateCourse();

                case ("d") -> printCourses(university.getCourses());

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printCourses(List<Course> courses) {
        var serial = new AtomicInteger(1);
        courses
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
                case ("a") -> university.getDurations().add(createNewDuration());

                case ("b") -> {
                    printDurations();
                    deleteObject(university.getDurations());
                }

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

    private Duration createNewDuration() {
        var scanner = new Scanner(in);
        out.println("Print start time of duration(hh:mm):");
        var startTime = LocalTime.parse(scanner.nextLine());
        out.println("Print end time of duration(hh:mm):");
        var endTime = LocalTime.parse(scanner.nextLine());
        return new Duration(startTime, endTime);
    }

    private void manageLectures() {
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
                case ("a") -> university.getLectures().add(createNewLecture());

                case ("b") -> {
                    printLectures(university.getLectures());
                    deleteObject(university.getLectures());
                }

                case ("c") -> updateLecture();

                case ("d") -> printLectures(university.getLectures());

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printLectures(List<Lecture> lectures) {
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

    private void updateLecture() {
        var updatedLecture = (Lecture) selectObject(university.getLectures());
        var lecture = createNewLecture();
        updatedLecture.setDate(lecture.getDate());
        updatedLecture.setCourse(lecture.getCourse());
        updatedLecture.setGroups(lecture.getGroups());
        updatedLecture.setAudience(lecture.getAudience());
        updatedLecture.setDuration(lecture.getDuration());
        updatedLecture.setTeacher(lecture.getTeacher());
    }

    private Lecture createNewLecture() {
        var scanner = new Scanner(in);
        out.println("Print lecture date:");
        var date = LocalDate.parse(scanner.nextLine());
        printCourses(university.getCourses());
        var course = (Course) selectObject(university.getCourses());
        var groups = selectLectureGroups();
        printAudiences();
        var audience = (Audience) selectObject(university.getAudiences());
        printDurations();
        var duration = (Duration) selectObject(university.getDurations());
        printTeachers(university.getTeachers());
        var teacher = (Teacher) selectObject(university.getTeachers());
        return new Lecture(date, course, groups, audience, duration, teacher);
    }

    private List<Group> selectLectureGroups() {
        var groups = new ArrayList<Group>();
        var scanner = new Scanner(in);
        var menuText = """
                GROUPS
                Select menu item:
                a. Add group to the list
                b. Delete group from the list
                c. Print groups list
                q. Close lecture's group manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> addGroupToList(groups);

                case ("b") -> {
                    printGroups(groups);
                    deleteObject(groups);
                }

                case ("c") -> printGroups(groups);

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
        return groups;
    }

    private void addGroupToList(List<Group> groups) {
        var groupList = new ArrayList<>(university.getGroups());
        groupList.removeAll(groups);
        printGroups(groupList);
        groups.add((Group) selectObject(groupList));
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
                case ("a") -> university.getAudiences().add(createNewAudience());

                case ("b") -> deleteObject(university.getAudiences());

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
        var updatedAudience = (Audience) selectObject(university.getAudiences());
        var audience = createNewAudience();
        updatedAudience.setRoomNumber(audience.getRoomNumber());
        updatedAudience.setCapacity(audience.getCapacity());
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
                case ("a") -> university.getGroups().add(createNewGroup());

                case ("b") -> deleteObject(university.getGroups());

                case ("c") -> updateGroup();

                case ("d") -> printGroups(university.getGroups());

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printGroups(List<Group> groups) {
        var serial = new AtomicInteger(1);
        groups.forEach(p -> out.printf("%d. %s%n%s", serial.getAndIncrement(), p.getName(), formatGroupStudents(p)));
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
        var updatedGroup = (Group) selectObject(university.getGroups());
        var audience = createNewGroup();
        updatedGroup.setName(audience.getName());
        updatedGroup.setStudents(audience.getStudents());
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
        var scanner = new Scanner(in);
        var menuText = """
                TEACHERS
                Select menu item:
                a. Add teacher
                b. Delete teacher
                c. Update teacher
                d. Print teachers
                q. Close teacher's manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> university.getTeachers().add(createNewTeacher());

                case ("b") -> deleteObject(university.getTeachers());

                case ("c") -> updateTeacher();

                case ("d") -> printTeachers(university.getTeachers());

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printTeachers(List<Teacher> teachers) {
        var serial = new AtomicInteger(1);
        teachers.forEach(p -> out.printf("%d. %s %s %s %s %s %s %s %s %s%n",
                serial.getAndIncrement(),
                p.getFirstName(),
                p.getLastName(),
                p.getEmail(),
                p.getCountry(),
                p.getGender(),
                p.getPhone(),
                p.getAddress(),
                p.getScientificDegree(),
                p.getBirthdate()));
    }

    private void updateTeacher() {
        var updatedTeacher = (Teacher) selectObject(university.getTeachers());
        var teacher = createNewTeacher();
        updatedTeacher.setFirstName(teacher.getFirstName());
        updatedTeacher.setLastName(teacher.getLastName());
        updatedTeacher.setEmail(teacher.getEmail());
        updatedTeacher.setCountry(teacher.getCountry());
        updatedTeacher.setGender(teacher.getGender());
        updatedTeacher.setPhone(teacher.getPhone());
        updatedTeacher.setAddress(teacher.getAddress());
        updatedTeacher.setScientificDegree(teacher.getScientificDegree());
        updatedTeacher.setBirthdate(teacher.getBirthdate());
        updatedTeacher.setCourses(teacher.getCourses());
        updatedTeacher.setVacations(teacher.getVacations());
    }

    private Teacher createNewTeacher() {
        var scanner = new Scanner(in);
        var teacher = new Teacher();
        out.println("Print teacher's firstname:");
        teacher.setFirstName(scanner.nextLine());
        out.println("Print teacher's lastname:");
        teacher.setLastName(scanner.nextLine());
        out.println("Print teacher's email:");
        teacher.setEmail(scanner.nextLine());
        out.println("Print teacher's country:");
        teacher.setCountry(scanner.nextLine());
        out.println("Print teacher's gender:");
        teacher.setGender(scanner.nextLine());
        out.println("Print teacher's address:");
        teacher.setAddress(scanner.nextLine());
        out.println("Print teacher's phone:");
        teacher.setPhone(scanner.nextLong());
        scanner.nextLine();
        out.println("Print teacher's birthdate(YYYY-MM-DD):");
        teacher.setBirthdate(LocalDate.parse(scanner.nextLine()));
        out.println("Select courses:");
        teacher.setCourses(selectTeacherCourses());
        teacher.setVacations(manageVacations());
        return teacher;
    }

    private List<Vacation> manageVacations() {
        var vacations = new ArrayList<Vacation>();
        var scanner = new Scanner(in);
        var menuText = """
                VACATIONS
                Select menu item:
                a. Add vacation to the list
                b. Delete vacation from the list
                c. Print vacation list
                q. Close teacher's vacation manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> vacations.add(createVacation());

                case ("b") -> {
                    printVacations(vacations);
                    deleteObject(vacations);
                }

                case ("c") -> printVacations(vacations);

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
        return vacations;
    }

    private void printVacations(List<Vacation> vacations) {
        var serial = new AtomicInteger(1);
        vacations
                .forEach(p -> out.println(serial.getAndIncrement() + ". " + p.getStartDate() + " " + p.getEndDate()));
    }

    private Vacation createVacation() {
        var scanner = new Scanner(in);
        out.println("Print start date of vacation(YYYY-MM-DD):");
        var startDate = LocalDate.parse(scanner.nextLine());
        out.println("Print end date of vacation(YYYY-MM-DD):");
        var endDate = LocalDate.parse(scanner.nextLine());
        return new Vacation(startDate, endDate);
    }

    private List<Course> selectTeacherCourses() {
        var courses = new ArrayList<Course>();
        var scanner = new Scanner(in);
        var menuText = """
                COURSES
                Select menu item:
                a. Add course to the list
                b. Delete course from the list
                c. Print courses list
                q. Close teacher's courses manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> addCourseToList(courses);

                case ("b") -> {
                    printCourses(courses);
                    deleteObject(courses);
                }

                case ("c") -> printCourses(courses);

                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
        return courses;
    }

    private void addCourseToList(List<Course> courses) {
        var courseList = new ArrayList<>(university.getCourses());
        courseList.removeAll(courses);
        printCourses(courseList);
        courses.add((Course) selectObject(courseList));
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
                case ("a") -> university.getStudents().add(createNewStudent());

                case ("b") -> deleteObject(university.getStudents());

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
        var updatedStudent = (Student) selectObject(university.getStudents());
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
                case ("a") -> university.getHolidays().add(createNewHoliday());

                case ("b") -> {
                    printHolidays();
                    deleteObject(university.getHolidays());
                }

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
        var updatedHoliday = (Holiday) selectObject(university.getHolidays());
        var holiday = createNewHoliday();
        updatedHoliday.setName(holiday.getName());
        updatedHoliday.setDate(holiday.getDate());
    }

    private Holiday createNewHoliday() {
        var scanner = new Scanner(in);
        out.println("Print name of holiday:");
        var name = scanner.nextLine();
        out.println("Print date of holiday(YYYY-MM-DD):");
        var date = LocalDate.parse(scanner.next());
        return new Holiday(name, date);
    }

    private void deleteObject(List<?> objects) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of audience:");
        var number = scanner.nextInt();
        objects.remove(number - 1);
    }

    private Object selectObject(List<?> objects) {
        var scanner = new Scanner(in);
        out.println("Print number:");
        var number = scanner.nextInt();
        while (number < 1 || number > objects.size()) {
            out.println("Print correct number!");
        }
        return objects.get(number - 1);
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