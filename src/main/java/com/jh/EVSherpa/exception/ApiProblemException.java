package com.jh.EVSherpa.exception;

public class ApiProblemException extends RuntimeException{
    public ApiProblemException() {
        super();
    }

    public ApiProblemException(String message) {
        super(message);
    }
}
