package edu.shmonin.university.exception;

public class StudentsLimitReachedException extends ServiceException {

    public StudentsLimitReachedException(String message) {
        super(message);
    }
}
