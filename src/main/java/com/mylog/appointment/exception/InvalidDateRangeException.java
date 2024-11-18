package com.mylog.appointment.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String message) {
        super(message);
    }

    public InvalidDateRangeException(String message,Throwable e) {
        super(message,e);
    }
}
