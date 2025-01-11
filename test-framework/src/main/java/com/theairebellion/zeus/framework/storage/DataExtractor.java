package com.theairebellion.zeus.framework.storage;

public interface DataExtractor<T> {

    Enum<?> getSubKey();

    Enum<?> getKey();

    T extract(Object rawObject);

}
