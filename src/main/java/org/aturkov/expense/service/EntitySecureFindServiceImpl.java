package org.aturkov.expense.service;

import org.aturkov.expense.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public abstract class EntitySecureFindServiceImpl<T> implements EntitySecureFindService<T> {
    //todo drop logger
    private static final Logger log = LoggerFactory.getLogger(EntitySecureFindServiceImpl.class);

    public T safeFindEntity(UUID id, CrudRepository<T, UUID> crudRepository, FindMode findMode) throws ServiceException {
        Optional<T> entity = crudRepository.findById(id);
        switch (findMode) {
            case ifNullLogError -> {
                if (entity.isEmpty()) {
                    log.info("Entity with id[{}] not found", id);
                    return null;
                }
            }
            case ifNullThrowsError -> {
                if (entity.isEmpty()) {
                    log.info("Entity with id[{}] not found", id);
                    throw new ServiceException("Entity not found");
                }
            }
        }
        return entity.get();
    }

    public enum FindMode {
        ifNullLogError,
        ifNullThrowsError
    }
}
