package org.aturkov.expense.dao.template;

import org.aturkov.expense.domain.OperationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TemplateRepository extends CrudRepository<TemplateEntity, UUID> {
    Optional<TemplateEntity> findById(UUID id);

    @Override
    List<TemplateEntity> findAll();

    List<TemplateEntity> findAllByIdIn(Collection<UUID> ids);

    List<TemplateEntity> findAllByActiveIsTrue();

    void deleteAllByIdIn(List<UUID> ids);

    boolean existsByName(String name);

    List<TemplateEntity> findByOperationType(OperationType operationType);
}
