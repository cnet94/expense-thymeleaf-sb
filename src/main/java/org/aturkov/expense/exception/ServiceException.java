package org.aturkov.expense.exception;

public class ServiceException extends Exception{
    public ServiceException(String message) {
        super(message);
    }
}
