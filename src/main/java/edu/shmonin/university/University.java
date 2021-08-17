package edu.shmonin.university;

import edu.shmonin.university.model.*;

import java.util.ArrayList;
import java.util.List;

public class University {

    private final List<Teacher> teachers;
    private final List<Student> students;
    private final List<Course> courses;
    private final List<Group> groups;
    private final List<Audience> audiences;
    private final List<Lecture> lectures;
    private final List<Holiday> holidays;
    private final List<Duration> durations;

    public University() {
        teachers = new ArrayList<>();
        students = new ArrayList<>();
        courses = new ArrayList<>();
        groups = new ArrayList<>();
        audiences = new ArrayList<>();
        lectures = new ArrayList<>();
        holidays = new ArrayList<>();
        durations = new ArrayList<>();
    }

    public List<Duration> getDurations() {
        return durations;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Audience> getAudiences() {
        return audiences;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }
}