package org.aturkov.expense.mapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class SimpleDTOMapper<T, S> extends ListDTOMapper<T, S> {
    private final Class<S> type;

    public SimpleDTOMapper() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        Type dstType = pt.getActualTypeArguments()[1];
        if (dstType instanceof Class<?>)
            type = (Class) dstType;
        else
            type = (Class)((ParameterizedType) dstType).getRawType();
    }

    public S convert(T src) throws Exception {
        return convert(src, new MapperContext());
    }

    public S convert(T src, MapperContext mapperContext) throws Exception {
        if (hideMode(mapperContext))
            return null;
        if (src == null)
            return null;
//        String objectCacheId = getObjectCacheId(src);
//        S dst = mapperContext.getFromCache(type, objectCacheId);
//        if (dst != null)
//            return dst;
        S dst = type.getDeclaredConstructor().newInstance();
        map(src, dst, mapperContext);
//        mapperContext.putToCache(type, objectCacheId, dst);
        return dst;
    }

    public boolean hideMode(MapperContext mapperContext) {
        return false;
    }
}
