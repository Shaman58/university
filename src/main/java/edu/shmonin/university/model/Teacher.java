package edu.shmonin.university.model;

import java.util.List;
import java.util.Objects;

public class Teacher extends Human {

    private ScientificDegree scientificDegree;
    private List<Course> courses;

    public ScientificDegree getScientificDegree() {
        return scientificDegree;
    }

    public void setScientificDegree(ScientificDegree scientificDegree) {
        this.scientificDegree = scientificDegree;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        var teacher = (Teacher) o;

        if (scientificDegree != teacher.scientificDegree) return false;
        return Objects.equals(courses, teacher.courses);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (scientificDegree != null ? scientificDegree.hashCode() : 0);
        result = 31 * result + (courses != null ? courses.hashCode() : 0);
        return result;
    }
}