package org.aturkov.expense.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseDetailAttachmentRepository extends CrudRepository<ExpenseDetailAttachmentEntity, UUID> {
}
