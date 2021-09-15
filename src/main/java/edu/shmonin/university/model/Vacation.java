package edu.shmonin.university.model;

import java.time.LocalDate;

public class Vacation {

    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Teacher teacher;

    public Vacation(LocalDate startDate, LocalDate endDate, Teacher teacher) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacher = teacher;
    }

    public Vacation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}