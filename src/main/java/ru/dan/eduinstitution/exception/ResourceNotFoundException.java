package ru.dan.eduinstitution.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String format) {
        super(format);
    }
}
