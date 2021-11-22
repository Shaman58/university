package edu.shmonin.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Holiday {

    private int id;
    private String name;
    private LocalDate date;

    public Holiday() {
    }

    public Holiday(String name, LocalDate date) {
        this.name = name;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Holiday)) return false;

        var holiday = (Holiday) o;

        if (!Objects.equals(name, holiday.name)) return false;
        return Objects.equals(date, holiday.date);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "name=" + name + " date=" + date;
    }
}
