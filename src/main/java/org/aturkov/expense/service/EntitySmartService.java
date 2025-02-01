package org.aturkov.expense.service;

public class EntitySmartService {
    public enum EntityValidateMode {
        none,
        afterRead,
        beforeSave
    }
}
