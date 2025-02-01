package org.aturkov.expense.service;

import lombok.Data;

@Data
public class ChangesHelper {
    private String oldValue;
    private String newValue;

    public void changeValue(Object oldValue, Object newValue) {
        if (!oldValue.toString().equals(newValue.toString())) {

        }
    }
}
