package edu.shmonin.university.dao;

import edu.shmonin.university.model.Course;

import java.util.List;

public interface CourseDao extends Dao<Course> {

    List<Course> getByTeacherId(int teacherId);
}