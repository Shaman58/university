package edu.shmonin.university.model;

import java.time.LocalDate;
import java.util.List;

public class Lecture {

    private LocalDate date;
    private Course course;
    private List<Group> groups;
    private Audience audience;
    private Duration duration;
    private Teacher teacher;

    public Lecture(LocalDate date, Course course, List<Group> groups, Audience audience, Duration duration, Teacher teacher) {
        this.date = date;
        this.course = course;
        this.groups = groups;
        this.audience = audience;
        this.duration = duration;
        this.teacher = teacher;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}