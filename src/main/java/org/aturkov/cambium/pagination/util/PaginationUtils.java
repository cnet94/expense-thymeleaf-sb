package org.aturkov.cambium.pagination.util;

import org.aturkov.cambium.pagination.PaginationResult;
import org.aturkov.cambium.pagination.SimplePagination;
import org.aturkov.expense.exception.ServiceException;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.aturkov.expense.exception.ErrorCodeExpense.PAGINATION_ERROR;
import static org.aturkov.expense.exception.ErrorCodeExpense.PAGINATION_LIMIT_ERROR;

public class PaginationUtils {
    public static final int DEFAULT_VALUE_OFFSET = 0;
    public static final int DEFAULT_VALUE_LIMIT = 10;
    public static final String SORT_UNSORTED = "unsorted";

    public static Sort sortType(boolean sortAsc, String sortField) {
        if (sortField.equals(SORT_UNSORTED))
            return Sort.unsorted();
        return Sort.by(sortAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
    }

    public static Pageable pagination(int page, int size) {
        return PageRequest.of(page, size);
    }

    public static Pageable pagination(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    public static SimplePagination convertPagableInSimplePagination(Pageable pageable) {
        return new SimplePagination().setOffset((int) pageable.getOffset()).setLimit(pageable.getPageSize());
    }

    public static Pageable pageableOffset(SimplePagination pagination) throws ServiceException {
        if (pagination.getLimit() < 1)
            throw new ServiceException(PAGINATION_LIMIT_ERROR.getMessage());
        if (pagination.getOffset() % pagination.getLimit() > 0)
            throw new ServiceException(PAGINATION_ERROR.getMessage());
        Sort sort = sortType(pagination.isSortAsc(), pagination.getSortField());
        return sort.isUnsorted()
                ? PageRequest.of(pagination.getOffset() / pagination.getLimit(), pagination.getLimit())
                : PageRequest.of(pagination.getOffset() / pagination.getLimit(), pagination.getLimit(), sort);
    }

    public static <T> PaginationResult<T> convertInPaginationResult(SimplePagination pagination) throws ServiceException {
        Page<T> emptyPage = new PageImpl<>(new ArrayList<>(), pageableOffset(pagination), 0);
        return convertInPaginationResult(emptyPage, pagination);
    }

    public static <T> PaginationResult<T> convertInPaginationResult(Page<T> page, SimplePagination pagination) {
        PaginationResult<T> result = new PaginationResult<>();
        result
                .setList(page.getContent())
                .setTotal(page.getTotalElements())
                .setOffset(pagination.getOffset())
                .setLimit(pagination.getLimit());
        return result;
    }

    public static <T> PaginationResult<T> convertInPaginationResult(Page<T> page, SimplePagination pagination, Function<T, Boolean> filterFunction) {
        PaginationResult<T> result = new PaginationResult<>();
        result
                .setList(page.getContent().stream().filter(filterFunction::apply).toList())
                .setTotal(page.getTotalElements())
                .setOffset(pagination.getOffset())
                .setLimit(pagination.getLimit());
        return result;
    }

    public static <T> PaginationResult<T> convertInPaginationResult(List<T> list, SimplePagination pagination, long total) throws ServiceException {
        Page<T> page = new PageImpl<>(list, pageableOffset(pagination), total);
        return convertInPaginationResult(page, pagination);
    }
}
