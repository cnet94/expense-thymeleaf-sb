package org.aturkov.expense.mapper;

public interface SimpleMapper<M, N> {
    N map(M entity);
}
