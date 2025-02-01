package org.aturkov.expense.mapper;

public interface DTOMapper<S, D> extends DTOConverter<S, D> {
    void map(S src, D dst, MapperContext mapperContext) throws Exception;
}
