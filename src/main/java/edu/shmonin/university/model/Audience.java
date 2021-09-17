package edu.shmonin.university.model;

public class Audience {

    private int audienceId;
    private int roomNumber;
    private int capacity;

    public Audience() {
    }

    public Audience(int id, int roomNumber, int capacity) {
        this.audienceId = id;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public Audience(int roomNumber, int capacity) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public int getAudienceId() {
        return audienceId;
    }

    public void setAudienceId(int audienceId) {
        this.audienceId = audienceId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Audience)) return false;

        var audience = (Audience) o;

        if (roomNumber != audience.roomNumber) return false;
        return capacity == audience.capacity;
    }

    @Override
    public int hashCode() {
        int result = roomNumber;
        result = 31 * result + capacity;
        return result;
    }
}
