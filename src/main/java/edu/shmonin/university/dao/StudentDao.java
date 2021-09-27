package edu.shmonin.university.dao;

import edu.shmonin.university.model.Group;
import edu.shmonin.university.model.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {

    void addStudentToTheGroup(Student student, Group group);

    List<Student> selectStudentsRelatedToTheGroup(int id);
}