package edu.shmonin.university.model;

import java.time.LocalTime;
import java.util.Objects;

public class Duration {

    private int id;
    private LocalTime startTime;
    private LocalTime endTime;

    public Duration() {
    }

    public Duration(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Duration)) return false;

        var duration = (Duration) o;

        if (!Objects.equals(startTime, duration.startTime)) return false;
        return Objects.equals(endTime, duration.endTime);
    }

    @Override
    public int hashCode() {
        int result = startTime != null ? startTime.hashCode() : 0;
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "startTime=" + startTime + ", endTime=" + endTime;
    }
}