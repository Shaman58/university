package edu.shmonin.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Vacation {

    private int id;
    private LocalDate startDate;
    private LocalDate endDate;

    public Vacation(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vacation)) return false;

        var vacation = (Vacation) o;

        if (!Objects.equals(startDate, vacation.startDate)) return false;
        return Objects.equals(endDate, vacation.endDate);
    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}