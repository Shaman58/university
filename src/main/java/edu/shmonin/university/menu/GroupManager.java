package edu.shmonin.university.menu;

import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.in;
import static java.lang.System.out;

public class GroupManager {
    public void manageGroups(List<Group> groups, List<Student> students) {
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
                case ("a") -> groups.add(createNewGroup(groups, students));
                case ("b") -> deleteGroup(groups);
                case ("c") -> updateGroup(groups, students);
                case ("d") -> printGroups(groups);
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

    private String formatGroupStudents(Group group) {
        var result = "";
        var serial = new AtomicInteger(1);
        for (Student student : group.getStudents()) {
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

    private void updateGroup(List<Group> groups, List<Student> students) {
        var updatedGroup = selectGroup(groups);
        var group = createNewGroup(groups, students);
        updatedGroup.setName(group.getName());
        updatedGroup.setStudents(group.getStudents());
    }

    private Group createNewGroup(List<Group> groups, List<Student> students) {
        var scanner = new Scanner(in);
        var studentManager = new StudentManager();
        var studentsInGroup = new ArrayList<Student>();
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
                    var student = studentManager.createNewStudent();
                    students.add(student);
                    studentsInGroup.add(student);
                }
                case ("b") -> {
                    var ableStudents = new ArrayList<>(students);
                    ableStudents.removeAll(studentsInGroup);
                    studentManager.printStudents(ableStudents);
                    var targetStudent = studentManager.selectStudent(ableStudents);
                    studentsInGroup.add(targetStudent);
                    groups.forEach(p -> p.getStudents().remove(targetStudent));
                }
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
        return new Group(name, studentsInGroup);
    }

    private void deleteGroup(List<Group> groups) {
        var scanner = new Scanner(in);
        out.println("Print sequence number of audience:");
        var number = scanner.nextInt();
        groups.remove(number - 1);
    }

    private Group selectGroup(List<Group> groups) {
        var scanner = new Scanner(in);
        out.println("Print number:");
        var number = scanner.nextInt();
        while (number < 1 || number > groups.size()) {
            out.println("Print correct number o group!");
        }
        return groups.get(number - 1);
    }

    public List<Group> selectLectureGroups(List<Group> sourceGroups) {
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
                case ("a") -> addGroupToList(targetGroups, sourceGroups);
                case ("b") -> {
                    printGroups(targetGroups);
                    deleteGroup(targetGroups);
                }
                case ("c") -> printGroups(targetGroups);
                default -> out.println("Input the right letter!");
            }
            out.println(menuText);
            inputKey = scanner.next();
        }
        return targetGroups;
    }

    private void addGroupToList(List<Group> targetGroups, List<Group> sourceGroups) {
        var groupList = new ArrayList<>(sourceGroups);
        groupList.removeAll(targetGroups);
        printGroups(groupList);
        targetGroups.add(selectGroup(groupList));
    }
}