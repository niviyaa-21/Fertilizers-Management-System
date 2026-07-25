package com.examly.springapp.exception;

public class FertilizerNotFoundException extends RuntimeException {
    public FertilizerNotFoundException(String message) {
        super(message);
    }
}
