package edu.shmonin.university.exception;

public class ChainedEntityException extends RuntimeException {

    public ChainedEntityException(String message) {
        super(message);
    }
}
