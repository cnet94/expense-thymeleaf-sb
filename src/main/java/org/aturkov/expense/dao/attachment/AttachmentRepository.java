package org.aturkov.expense.dao.attachment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttachmentRepository extends CrudRepository<AttachmentEntity, UUID> {
}
