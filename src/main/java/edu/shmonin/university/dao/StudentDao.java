package edu.shmonin.university.dao;

import edu.shmonin.university.model.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {
    List<Student> getByGroupId(int groupId);
}