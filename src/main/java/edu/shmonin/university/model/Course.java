package edu.shmonin.university.model;

import java.util.Objects;

public class Course {

    private int id;
    private String name;

    public Course() {
    }

    public Course(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;

        var course = (Course) o;

        if (id != course.id) return false;
        return Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Course{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}