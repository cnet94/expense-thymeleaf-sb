package org.aturkov.expense;

public class ServiceException extends Exception{
    public ServiceException(String message) {
        super(message);
    }
}
