package edu.shmonin.university.model;

import java.time.LocalDate;

public class Holiday {

    private int holidayId;
    private String name;
    private LocalDate date;

    public Holiday() {
    }

    public Holiday(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }

    public int getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
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
}
