package org.aturkov.expense.service.item;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.item.ItemEntity;
import org.aturkov.expense.dao.item.ItemRepository;
import org.aturkov.expense.dao.itemgroup.ItemGroupEntity;
import org.aturkov.expense.dao.itemgroup.ItemGroupRepository;
import org.aturkov.expense.exception.ErrorCodeExpense;
import org.aturkov.expense.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemGroupService {
    private static final Logger log = LoggerFactory.getLogger(ItemGroupService.class);
    private final ItemGroupRepository itemGroupRepository;

    public List<ItemGroupEntity> findItemGroups() {
        return itemGroupRepository.findAll();
    }

//    public ItemEntity createItem(ItemEntity entity) {
//        entity
//                .setStatus(ItemEntity.Status.active);
//        return itemRepository.save(entity);
//    }
//
//    public void deleteItem(UUID id) throws Exception {
//        try {
//            itemRepository.deleteById(id);
//        } catch (RuntimeException e) {
//            log.error(e.getMessage());
//            throw new Exception(String.format(ErrorCodeExpense.ITEM_DELETE_FAILED.getMessage(), id), e);
//        }
//    }
//
//    public void archiveItem(UUID id) throws ServiceException {
//        throw new ServiceException("Not implemented");
//    }
}
