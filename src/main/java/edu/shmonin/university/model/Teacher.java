package edu.shmonin.university.model;

import java.util.List;
import java.util.Objects;

public class Teacher extends Human{

    private ScientificDegree scientificDegree;
    private List<Course> courses;
    private List<Vacation> vacations;

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

    public List<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        var teacher = (Teacher) o;

        if (scientificDegree != teacher.scientificDegree) return false;
        if (!Objects.equals(courses, teacher.courses)) return false;
        return Objects.equals(vacations, teacher.vacations);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (scientificDegree != null ? scientificDegree.hashCode() : 0);
        result = 31 * result + (courses != null ? courses.hashCode() : 0);
        result = 31 * result + (vacations != null ? vacations.hashCode() : 0);
        return result;
    }
}