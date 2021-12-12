package edu.shmonin.university.exception;

public class GroupLimitReachedException extends ServiceException {

    public GroupLimitReachedException(String message) {
        super(message);
    }
}
