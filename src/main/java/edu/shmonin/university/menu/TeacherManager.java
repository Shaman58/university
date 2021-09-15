package edu.shmonin.university.menu;

import edu.shmonin.university.dao.CourseDao;
import edu.shmonin.university.dao.TeacherDao;
import edu.shmonin.university.dao.VacationDao;
import edu.shmonin.university.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

@Repository
public class TeacherManager {

    private TeacherDao teacherDao;
    private CourseDao courseDao;
    private VacationDao vacationDao;
    private CourseManager courseManager;
    private GenderManager genderManager;
    private ScientificDegreeManager scientificDegreeManager;
    private VacationManager vacationManager;

    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Autowired
    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Autowired
    public void setVacationDao(VacationDao vacationDao) {
        this.vacationDao = vacationDao;
    }

    @Autowired
    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    @Autowired
    public void setGenderManager(GenderManager genderManager) {
        this.genderManager = genderManager;
    }

    @Autowired
    public void setScientificDegreeManager(ScientificDegreeManager scientificDegreeManager) {
        this.scientificDegreeManager = scientificDegreeManager;
    }

    @Autowired
    public void setVacationManager(VacationManager vacationManager) {
        this.vacationManager = vacationManager;
    }

    public void manageTeachers() {
        var scanner = new Scanner(in);
        var menuText = """
                TEACHERS
                Select menu item:
                a. Add teacher
                b. Delete teacher
                c. Update teacher
                d. Print teachers
                e. Set course to the teacher
                f. Add vacation to the teacher
                g. Print teacher's vacations
                h. Delete teacher's vacation
                i. Print teacher's courses
                q. Close teacher's manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> teacherDao.create(createNewTeacher());
                case ("b") -> teacherDao.delete(selectTeacher().getId());
                case ("c") -> teacherDao.update(updateTeacher());
                case ("d") -> printTeachers(teacherDao.getAll());
                case ("e") -> teacherDao.addCourseTeacher(courseManager.selectCourse(), selectTeacher());
                case ("f") -> vacationManager.createVacation(selectTeacher());
                case ("g") -> vacationManager.printVacations(vacationDao.getTeacherVacations(selectTeacher()));
                case ("h") -> vacationDao.delete(vacationManager.selectTeacherVacation(selectTeacher()).getId());
                case ("i") -> courseManager.printCourses(courseDao.getTeacherCourses(selectTeacher()));
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    public void printTeachers(List<Teacher> teachers) {
        teachers.forEach(p -> out.printf("%d. %s %s %s %s %s %s %s %s %s%n",
                p.getId(),
                p.getFirstName(),
                p.getLastName(),
                p.getEmail(),
                p.getCountry(),
                p.getGender(),
                p.getPhone(),
                p.getAddress(),
                p.getScientificDegree(),
                p.getBirthDate()));
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
        out.println("Select teacher's gender:");
        teacher.setGender(genderManager.selectGender());
        out.println("Print teacher's address:");
        teacher.setAddress(scanner.nextLine());
        out.println("Print teacher's phone:");
        teacher.setPhone(scanner.nextLine());
        out.println("Select teacher's scientific degree:");
        teacher.setScientificDegree(scientificDegreeManager.selectScientificDegree());
        out.println("Print teacher's birthdate(YYYY-MM-DD):");
        teacher.setBirthDate(LocalDate.parse(scanner.nextLine()));
        return teacher;
    }

    private Teacher updateTeacher() {
        var id = selectId();
        var teacher = createNewTeacher();
        teacher.setId(id);
        return teacher;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print teacher's id:");
        return scanner.nextInt();
    }

    public Teacher selectTeacher() {
        printTeachers(teacherDao.getAll());
        return teacherDao.get(selectId());
    }
}