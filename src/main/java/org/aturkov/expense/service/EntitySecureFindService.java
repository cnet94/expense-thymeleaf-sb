package org.aturkov.expense.service;

import org.aturkov.expense.exception.ServiceException;

public interface EntitySecureFindService<T> {
    boolean validateEntity(T entity, EntitySmartService.EntityValidateMode entityValidateMode) throws ServiceException;
}
