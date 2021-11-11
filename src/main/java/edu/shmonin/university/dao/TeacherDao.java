package edu.shmonin.university.dao;

import edu.shmonin.university.model.Teacher;

import java.util.List;

public interface TeacherDao extends Dao<Teacher> {
    List<Teacher> getByCourseId(int courseId);
}