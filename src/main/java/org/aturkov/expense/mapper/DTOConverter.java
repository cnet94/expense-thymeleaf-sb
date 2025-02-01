package org.aturkov.expense.mapper;

public interface DTOConverter<S, D> {
    D convert(S src, MapperContext mapperContext) throws Exception;
}
