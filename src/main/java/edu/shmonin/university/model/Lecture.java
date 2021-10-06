package edu.shmonin.university.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Lecture {

    private int id;
    private LocalDate date;
    private Course course;
    private List<Group> groups;
    private Audience audience;
    private Duration duration;
    private Teacher teacher;

    public Lecture() {
    }

    public Lecture(LocalDate date, Course course, List<Group> groups, Audience audience, Duration duration, Teacher teacher) {
        this.date = date;
        this.course = course;
        this.groups = groups;
        this.audience = audience;
        this.duration = duration;
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lecture)) return false;

        var lecture = (Lecture) o;

        if (!Objects.equals(date, lecture.date)) return false;
        if (!Objects.equals(course, lecture.course)) return false;
        if (!Objects.equals(groups, lecture.groups)) return false;
        if (!Objects.equals(audience, lecture.audience)) return false;
        if (!Objects.equals(duration, lecture.duration)) return false;
        return Objects.equals(teacher, lecture.teacher);
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (course != null ? course.hashCode() : 0);
        result = 31 * result + (groups != null ? groups.hashCode() : 0);
        result = 31 * result + (audience != null ? audience.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        return result;
    }
}