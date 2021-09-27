package edu.shmonin.university.dao;

import edu.shmonin.university.model.Course;
import edu.shmonin.university.model.Teacher;

public interface TeacherDao extends Dao<Teacher> {
    void addTeacherCourse(Course course, Teacher teacher);
}