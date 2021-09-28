package edu.shmonin.university.menu;

import edu.shmonin.university.dao.jdbc.JdbcGroupDao;
import edu.shmonin.university.dao.jdbc.JdbcStudentDao;
import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Student;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.in;
import static java.lang.System.out;

@Component
public class GroupManager {

    private final JdbcGroupDao jdbcGroupDao;
    private final JdbcStudentDao jdbcStudentDao;
    private final StudentManager studentManager;

    public GroupManager(JdbcGroupDao jdbcGroupDao, JdbcStudentDao jdbcStudentDao, StudentManager studentManager) {
        this.jdbcGroupDao = jdbcGroupDao;
        this.jdbcStudentDao = jdbcStudentDao;
        this.studentManager = studentManager;
    }

    public void manageGroups() {
        var scanner = new Scanner(in);
        var menuText = """
                GROUPS
                Select menu item:
                a. Add group
                b. Delete group
                c. Update group
                d. Print groups
                e. Add student to the group
                q. Close group's manager
                Input menu letter:""";
        out.println(menuText);
        var inputKey = scanner.next();
        while (!inputKey.equals("q")) {
            switch (inputKey) {
                case ("a") -> jdbcGroupDao.create(createNewGroup());
                case ("b") -> jdbcGroupDao.delete(selectId());
                case ("c") -> jdbcGroupDao.update(updateGroup());
                case ("d") -> printGroupsWithStudents(jdbcGroupDao.getAll());
                case ("e") -> jdbcStudentDao.addStudentToTheGroup(studentManager.selectStudent(), selectGroup());
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
    }

    private void printGroups(List<Group> groups) {
        groups.forEach(p -> out.printf("%d. %s%n", p.getId(), p.getName()));
    }

    private void printGroupsWithStudents(List<Group> groups) {
        groups.forEach(p -> out.printf("%d. %s%n%s", p.getId(),
                p.getName(), formatGroupStudents(p)));
    }

    private Group createNewGroup() {
        var scanner = new Scanner(in);
        out.println("Print name of group:");
        var name = scanner.nextLine();
        return new Group(name);
    }

    private Group updateGroup() {
        var id = selectId();
        var group = createNewGroup();
        group.setId(id);
        return group;
    }

    private int selectId() {
        var scanner = new Scanner(in);
        out.println("Print group's id:");
        return scanner.nextInt();
    }

    public Group selectGroup() {
        printGroups(jdbcGroupDao.getAll());
        return jdbcGroupDao.get(selectId());
    }

    private String formatGroupStudents(Group group) {
        var result = "";
        var serial = new AtomicInteger(1);
        var students = jdbcStudentDao.getByGroupId(group.getId());
        for (Student student : students) {
            result = result.concat(String.format("  %d. %s %s %s %s %s %s %s %s%n",
                    serial.getAndIncrement(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getCountry(),
                    student.getGender(),
                    student.getPhone(),
                    student.getAddress(),
                    student.getBirthDate()));
        }
        return result;
    }

    public List<Group> getLectureGroups() {
        var targetGroups = new ArrayList<Group>();
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
                case ("a") -> targetGroups.add(selectGroup());
                case ("b") -> deleteGroup(targetGroups);
                case ("c") -> printGroups(targetGroups);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
        return targetGroups;
    }

    private void deleteGroup(List<Group> groups) {
        printGroups(groups);
        var id = selectId();
        groups.remove(jdbcGroupDao.get(id));
    }
}