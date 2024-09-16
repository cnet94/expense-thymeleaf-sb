package org.aturkov.expense.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends CrudRepository<ExpenseEntity, UUID> {
    Optional<ExpenseEntity> findById(UUID id);

    @Override
    List<ExpenseEntity> findAll();
}
