package org.aturkov.expense.mapper;

import java.util.*;

public abstract class ListDTOMapper<T, S> implements DTOMapper<T, S> {
    public List<S> convertCollection(Collection<T> srcCollection, MapperContext mapperContext) throws Exception {
        if (srcCollection == null)
            return null;
        beforeCollectionConversion(srcCollection, mapperContext);
        List<S> ret = new ArrayList<>();
        for (T src : srcCollection) {
            S converted = this.convert(src, mapperContext);
            if (converted != null)
                ret.add(converted);
        }
        return ret;
    }

    public List<S> convertCollection(Collection<T> srcList) throws Exception {
        if (srcList == null)
            return null;
        return convertCollection(srcList, new MapperContext());
    }

    public Map<UUID, S> convertMap(Map<UUID, T> srcMap, MapperContext mapperContext) throws Exception {
        if (srcMap == null)
            return null;
        beforeCollectionConversion(srcMap.values(), mapperContext);
        Map<UUID, S> ret = new LinkedHashMap<>();
        for (Map.Entry<UUID, T> src : srcMap.entrySet()) {
            ret.put(src.getKey(), this.convert(src.getValue(), mapperContext));
        }
        return ret;
    }

    public void beforeCollectionConversion(Collection<T> srcCollection, MapperContext mapperContext) throws Exception {

    }
}
