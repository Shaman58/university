package edu.shmonin.university.model;

import java.time.LocalDate;

public class Vacation {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public Vacation(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
