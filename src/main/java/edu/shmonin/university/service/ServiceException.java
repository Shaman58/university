package edu.shmonin.university.service;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}