package edu.shmonin.university;

import edu.shmonin.university.model.*;

import java.util.ArrayList;
import java.util.List;

public class University {

    private List<Teacher> teachers;
    private List<Student> students;
    private List<Course> courses;
    private List<Group> groups;
    private List<Audience> audiences;
    private List<Lecture> lectures;
    private List<Holiday> holidays;
    private List<Duration> durations;

    {
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

    public void setDurations(List<Duration> durations) {
        this.durations = durations;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Audience> getAudiences() {
        return audiences;
    }

    public void setAudiences(List<Audience> audiences) {
        this.audiences = audiences;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

}